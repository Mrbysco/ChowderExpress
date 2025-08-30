package com.mrbysco.chowderexpress.entity;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.registry.CartDataSerializers;
import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoupCart extends AbstractMinecart {
	private static final EntityDataAccessor<Optional<SoupData>> SOUP_DATA = SynchedEntityData.defineId(SoupCart.class, CartDataSerializers.SOUP_DATA.get());
	private static final EntityDataAccessor<Float> SOUP_AMOUNT = SynchedEntityData.defineId(SoupCart.class, EntityDataSerializers.FLOAT);
	private final SuspiciousStewEffects suspiciousStewEffects = new SuspiciousStewEffects(new ArrayList<>());

	private float rotationOffset;
	private float playerRotationOffset;

	public SoupCart(EntityType<?> type, Level level) {
		super(type, level);
	}

	public SoupCart(EntityType<?> type, Level level, double x, double y, double z) {
		super(type, level, x, y, z);
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		InteractionResult ret = super.interact(player, hand);
		if (ret.consumesAction()) return ret;
		if (!player.isSecondaryUseActive() && !this.isVehicle()) {
			this.playerRotationOffset = this.rotationOffset;
			if (!this.level().isClientSide) {
				ItemStack stack = player.getItemInHand(hand);
				if (stack.is(ChowderExpress.SOUPS) || stack.is(Items.BOWL)) {
					return doSoupInteraction(player, hand, stack);
				} else {
					return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
				}
			} else {
				return InteractionResult.SUCCESS;
			}
		} else {
			return InteractionResult.PASS;
		}
	}

	public InteractionResult doSoupInteraction(Player player, InteractionHand hand, ItemStack stack) {
		if (stack.isEmpty()) {
			return InteractionResult.FAIL;
		}
		if (stack.is(ChowderExpress.SOUPS)) {
			SuspiciousStewEffects suspiciousStewEffects = getStewEffects(stack);
			List<SuspiciousStewEffects.Entry> mobEffects = suspiciousStewEffects.effects();
			if (getSoupData().isEmpty()) {
				if (setSoupAmount(1)) {
					this.maybePlaySound(player);

					setSoupData(new SoupData(stack.copy(), stack.get(DataComponents.FOOD)));
					if (!mobEffects.isEmpty()) {
						mobEffects.forEach(this::addEffect);
					}

					if (!player.getAbilities().instabuild) {
						stack.shrink(1);
						player.addItem(new ItemStack(Items.BOWL));
					}
					this.level().playSound(null, blockPosition(), CartRegistry.EMPTY_BOWL.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
					return InteractionResult.SUCCESS;
				}
			} else if (getSoupData().get().stack().is(stack.getItem())) {
				if (mobEffects.size() == this.suspiciousStewEffects.effects().size()) {
					if (setSoupAmount(getSoupAmount() + 1)) {
						this.maybePlaySound(player);
						if (!player.getAbilities().instabuild) {
							stack.shrink(1);
							player.addItem(new ItemStack(Items.BOWL));
						}
						this.level().playSound(null, blockPosition(), CartRegistry.EMPTY_BOWL.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
						return InteractionResult.SUCCESS;
					}
				}
			}
		}
		if (stack.is(Items.BOWL) && getSoupData().isPresent() && getSoupAmount() >= 1.0) {
			ItemStack soupStack = getSoupData().get().stack().copy();
			if (setSoupAmount(getSoupAmount() - 1.0F)) {
				stack.shrink(1);
				if (soupStack.is(Items.SUSPICIOUS_STEW)) {
					soupStack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, this.suspiciousStewEffects);
				}

				player.addItem(soupStack);
				this.level().playSound(null, blockPosition(), CartRegistry.FILL_BOWL.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
			}
		}

		return InteractionResult.PASS;
	}

	public void maybePlaySound(Player player) {
		if (random.nextDouble() <= 0.05) {
			player.displayClientMessage(Component.literal("Mm soup"), true);
			this.level().playSound(null, blockPosition(), CartRegistry.MM_SOUP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
		}
	}

	private SuspiciousStewEffects getStewEffects(ItemStack stack) {
		SuspiciousStewEffects mobEffects = new SuspiciousStewEffects(new ArrayList<>());
		if (stack.has(DataComponents.SUSPICIOUS_STEW_EFFECTS)) {
			mobEffects = new SuspiciousStewEffects(stack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS).effects());
		}
		return mobEffects;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(SOUP_DATA, Optional.empty());
		builder.define(SOUP_AMOUNT, 0.0F);
	}

	public Optional<SoupData> getSoupData() {
		return this.entityData.get(SOUP_DATA);
	}

	public void setSoupData(@Nullable SoupData soupData) {
		this.entityData.set(SOUP_DATA, Optional.ofNullable(soupData));
	}

	public float getSoupAmount() {
		float amount = this.entityData.get(SOUP_AMOUNT);
		if (getSoupData().isEmpty() && amount > 0) {
			setSoupAmount(0);
			return 0;
		}
		return amount;
	}

	public boolean setSoupAmount(float amount) {
		if (amount <= 8) {
			if (amount == 0) {
				this.setSoupData(null);
			}
			this.entityData.set(SOUP_AMOUNT, amount);
			return true;
		}
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		SoupData soupData = null;
		Optional<SoupData> optionalData = tag.read("soupData", SoupData.CODEC,
				this.registryAccess().createSerializationContext(NbtOps.INSTANCE));
		if (optionalData.isPresent()) {
			soupData = optionalData.get();
		}
		setSoupData(soupData);

		if (soupData == null) {
			this.setSoupAmount(0);
		} else {
			this.setSoupAmount(tag.getFloatOr("SoupAmount", 0));
		}

		this.suspiciousStewEffects.effects().clear();
		SuspiciousStewEffects.CODEC.parse(NbtOps.INSTANCE, tag.get("ActiveEffects"))
				.resultOrPartial(ChowderExpress.LOGGER::error)
				.ifPresent(suspiciousStewEffects -> this.suspiciousStewEffects.effects().addAll(suspiciousStewEffects.effects()));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.getSoupData().isPresent()) {
			tag.store("soupData", SoupData.CODEC,
					this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getSoupData().get());
		}
		tag.putFloat("SoupAmount", this.getSoupAmount());
		if (!this.suspiciousStewEffects.effects().isEmpty()) {
			SuspiciousStewEffects.CODEC.encodeStart(NbtOps.INSTANCE, this.suspiciousStewEffects)
					.resultOrPartial(ChowderExpress.LOGGER::error)
					.ifPresent(effects -> tag.put("ActiveEffects", effects));
		}
	}

	public void activateMinecart(int x, int y, int z, boolean receivingPower) {
		if (receivingPower) {
			if (this.isVehicle()) {
				this.ejectPassengers();
			}

			if (this.getHurtTime() == 0) {
				this.setHurtDir(-this.getHurtDir());
				this.setHurtTime(10);
				this.setDamage(50.0F);
				this.markHurt();
			}
		}
	}

	@Override
	protected void moveAlongTrack(ServerLevel level) {
		super.moveAlongTrack(level);
		if (level.getGameTime() % 20 == 0) {
			Entity entity = this.getFirstPassenger();
			if (entity instanceof Player player && hasPassenger(player) && random.nextBoolean() && getSoupData().isPresent()) {
				SoupData soupData = getSoupData().get();
				if (!suspiciousStewEffects.effects().isEmpty() && getSoupAmount() > 0.5F) {
					for (SuspiciousStewEffects.Entry entry : suspiciousStewEffects.effects()) {
						player.addEffect(entry.createEffectInstance());
					}
					this.setSoupAmount(getSoupAmount() - 0.5F);
					this.playSound(SoundEvents.GENERIC_DRINK.value(), 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
				} else if (player.getFoodData().needsFood() && getSoupAmount() > 0.25F) {
					player.getFoodData().eat(Math.min(1, (int) (soupData.nutrition() / 2.0F)), soupData.saturationModifier() / 2.0F);
					this.setSoupAmount(getSoupAmount() - 0.25F);
					this.playSound(SoundEvents.GENERIC_DRINK.value(), 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
				}
			}
		}
	}

	public boolean addEffect(SuspiciousStewEffects.Entry effect) {
		if (!this.suspiciousStewEffects.effects().contains(effect)) {
			return this.suspiciousStewEffects.effects().add(effect);
		} else {
			return false;
		}
	}

	@Override
	protected Item getDropItem() {
		return CartRegistry.SOUP_CART_ITEM.get();
	}

	@Override
	public ItemStack getPickResult() {
		return new ItemStack(getDropItem());
	}

	@Override
	public boolean isRideable() {
		return true;
	}

	@Override
	public void tick() {
		double d0 = (double) this.getYRot();
		Vec3 vec3 = this.position();
		super.tick();
		double d1 = ((double) this.getYRot() - d0) % 360.0;
		if (this.level().isClientSide && vec3.distanceTo(this.position()) > 0.01) {
			this.rotationOffset += (float) d1;
			this.rotationOffset %= 360.0F;
		}
	}

	@Override
	protected void positionRider(Entity p_361111_, Entity.MoveFunction p_365490_) {
		super.positionRider(p_361111_, p_365490_);
		if (this.level().isClientSide && p_361111_ instanceof Player player && player.shouldRotateWithMinecart() && useExperimentalMovement(this.level())) {
			float f = (float) Mth.rotLerp(0.5, (double) this.playerRotationOffset, (double) this.rotationOffset);
			player.setYRot(player.getYRot() - (f - this.playerRotationOffset));
			this.playerRotationOffset = f;
		}
	}
}
