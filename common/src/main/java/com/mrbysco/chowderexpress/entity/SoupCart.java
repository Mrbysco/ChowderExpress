package com.mrbysco.chowderexpress.entity;

import com.mrbysco.chowderexpress.Constants;
import com.mrbysco.chowderexpress.platform.Services;
import com.mrbysco.chowderexpress.registration.CartRegistry;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoupCart extends AbstractMinecart {
	private static final EntityDataAccessor<Optional<SoupData>> SOUP_DATA = SynchedEntityData.defineId(SoupCart.class, Services.PLATFORM.getSoupSerializer());
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
	public EntityType<?> getType() {
		return CartRegistry.SOUP_CART.get();
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (this.isVehicle()) {
			return InteractionResult.PASS;
		} else if (!this.level().isClientSide()) {
			this.playerRotationOffset = this.rotationOffset;
			ItemStack stack = player.getItemInHand(hand);
			if (stack.is(Constants.SOUPS) || stack.is(Items.BOWL)) {
				return doSoupInteraction(player, hand, stack);
			} else {
				return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
			}
		} else {
			return InteractionResult.SUCCESS;
		}
	}

	public InteractionResult doSoupInteraction(Player player, InteractionHand hand, ItemStack stack) {
		if (stack.isEmpty()) {
			return InteractionResult.FAIL;
		}
		if (stack.is(Constants.SOUPS)) {
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
			ItemStack soupStack = getSoupData().get().stack().create();
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
			player.sendOverlayMessage(Component.literal("Mm soup"));
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
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		SoupData soupData = null;
		Optional<SoupData> optionalData = input.read("soupData", SoupData.CODEC);
		if (optionalData.isPresent()) {
			soupData = optionalData.get();
		}
		setSoupData(soupData);

		if (soupData == null) {
			this.setSoupAmount(0);
		} else {
			this.setSoupAmount(input.getFloatOr("SoupAmount", 0));
		}

		this.suspiciousStewEffects.effects().clear();
		Optional<SuspiciousStewEffects> effects = input.read("ActiveEffects", SuspiciousStewEffects.CODEC);
		effects.ifPresent(stewEffects -> this.suspiciousStewEffects.effects().addAll(stewEffects.effects()));
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		if (this.getSoupData().isPresent()) {
			output.store("soupData", SoupData.CODEC, this.getSoupData().get());
		}
		output.putFloat("SoupAmount", this.getSoupAmount());
		if (!this.suspiciousStewEffects.effects().isEmpty()) {
			output.store("ActiveEffects", SuspiciousStewEffects.CODEC, this.suspiciousStewEffects);
		}
	}

	@Override
	public void activateMinecart(ServerLevel level, int x, int y, int z, boolean isPowered) {
		if (isPowered) {
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
		if (this.level().getGameTime() % 20 == 0) {
			Entity entity = this.getFirstPassenger();
			if (entity instanceof Player player && hasPassenger(player) && random.nextBoolean() && getSoupData().isPresent()) {
				SoupData soupData = getSoupData().get();
				if (!suspiciousStewEffects.effects().isEmpty() && getSoupAmount() >= 0.5F) {
					for (SuspiciousStewEffects.Entry entry : suspiciousStewEffects.effects()) {
						player.addEffect(entry.createEffectInstance(), this);
					}
					float newAmount = getSoupAmount() - 0.5F;
					if (newAmount <= 0) {
						this.suspiciousStewEffects.effects().clear();
						this.setSoupData(null);
					} else {
						this.setSoupAmount(newAmount);
					}
					this.playSound(SoundEvents.GENERIC_DRINK.value(), 0.5F, this.level().getRandom().nextFloat() * 0.1F + 0.9F);
				} else if (player.getFoodData().needsFood() && getSoupAmount() >= 0.25F) {
					player.getFoodData().eat(Math.min(1, (int) (soupData.nutrition() / 2.0F)), soupData.saturationModifier() / 2.0F);
					float newAmount = getSoupAmount() - 0.25F;
					if (newAmount <= 0) {
						this.setSoupData(null);
					} else {
						this.setSoupAmount(newAmount);
					}
					this.playSound(SoundEvents.GENERIC_DRINK.value(), 0.5F, this.level().getRandom().nextFloat() * 0.1F + 0.9F);
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
		double d0 = this.getYRot();
		Vec3 vec3 = this.position();
		super.tick();
		double d1 = (this.getYRot() - d0) % 360.0;
		if (this.level().isClientSide() && vec3.distanceTo(this.position()) > 0.01) {
			this.rotationOffset += (float)d1;
			this.rotationOffset %= 360.0F;
		}
	}

	@Override
	protected void positionRider(Entity p_478277_, Entity.MoveFunction p_482199_) {
		super.positionRider(p_478277_, p_482199_);
		if (this.level().isClientSide() && p_478277_ instanceof Player player && player.shouldRotateWithMinecart() && useExperimentalMovement(this.level())) {
			float f = (float) Mth.rotLerp(0.5, (double)this.playerRotationOffset, (double)this.rotationOffset);
			player.setYRot(player.getYRot() - (f - this.playerRotationOffset));
			this.playerRotationOffset = f;
		}
	}
}
