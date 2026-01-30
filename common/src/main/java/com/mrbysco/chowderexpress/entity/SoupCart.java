package com.mrbysco.chowderexpress.entity;

import com.mrbysco.chowderexpress.Constants;
import com.mrbysco.chowderexpress.platform.Services;
import com.mrbysco.chowderexpress.registration.CartRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoupCart extends Minecart {
	private static final EntityDataAccessor<Optional<SoupData>> SOUP_DATA = SynchedEntityData.defineId(SoupCart.class, Services.PLATFORM.getSoupSerializer());
	private static final EntityDataAccessor<Float> SOUP_AMOUNT = SynchedEntityData.defineId(SoupCart.class, EntityDataSerializers.FLOAT);
	private final SuspiciousStewEffects suspiciousStewEffects = new SuspiciousStewEffects(new ArrayList<>());


	public SoupCart(EntityType<?> type, Level level) {
		super(type, level);
	}

	public SoupCart(Level level, double x, double y, double z) {
		super(level, x, y, z);
	}

	@Override
	public EntityType<?> getType() {
		return CartRegistry.SOUP_CART.get();
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (this.isVehicle()) {
			return InteractionResult.PASS;
		} else if (!this.level().isClientSide) {
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

					setSoupData(new SoupData(stack.copy(), Services.PLATFORM.getFoodProperties(stack, player)));
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
		if (stack.getItem() instanceof SuspiciousStewItem) {
			if (stack.has(DataComponents.SUSPICIOUS_STEW_EFFECTS)) {
				mobEffects = new SuspiciousStewEffects(stack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS).effects());
			}
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
		if (tag.contains("soupData", 10)) {
			soupData = SoupData.readSoupData(tag.getCompound("soupData"), this.registryAccess());
		}
		setSoupData(soupData);

		if (soupData == null) {
			this.setSoupAmount(0);
		} else {
			this.setSoupAmount(tag.getFloat("SoupAmount"));
		}

		this.suspiciousStewEffects.effects().clear();
		SuspiciousStewEffects.CODEC.parse(NbtOps.INSTANCE, tag.get("ActiveEffects"))
				.resultOrPartial(Constants.LOGGER::error)
				.ifPresent(suspiciousStewEffects -> this.suspiciousStewEffects.effects().addAll(suspiciousStewEffects.effects()));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.getSoupData().isPresent()) {
			tag.put("soupData", SoupData.writeSoupData(this.getSoupData().get(), this.registryAccess()));
		}
		tag.putFloat("SoupAmount", this.getSoupAmount());
		if (!this.suspiciousStewEffects.effects().isEmpty()) {
			SuspiciousStewEffects.CODEC.encodeStart(NbtOps.INSTANCE, this.suspiciousStewEffects)
					.resultOrPartial(Constants.LOGGER::error)
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
	protected void moveAlongTrack(BlockPos pos, BlockState state) {
		super.moveAlongTrack(pos, state);
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
					this.playSound(SoundEvents.GENERIC_DRINK, 0.5F, this.level().random.nextFloat() * 0.1F + 0.9F);
				} else if (player.getFoodData().needsFood() && getSoupAmount() >= 0.25F) {
					player.getFoodData().eat(Math.min(1, (int) (soupData.nutrition() / 2.0F)), soupData.saturationModifier() / 2.0F);
					float newAmount = getSoupAmount() - 0.25F;
					if (newAmount <= 0) {
						this.setSoupData(null);
					} else {
						this.setSoupAmount(newAmount);
					}
					this.playSound(SoundEvents.GENERIC_DRINK, 0.5F, this.level().random.nextFloat() * 0.1F + 0.9F);
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
	public Type getMinecartType() {
		return Type.RIDEABLE;
	}
}
