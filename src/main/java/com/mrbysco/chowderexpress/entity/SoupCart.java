package com.mrbysco.chowderexpress.entity;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.registry.CartDataSerializers;
import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SoupCart extends AbstractMinecart {
	private static final EntityDataAccessor<Optional<SoupData>> SOUP_DATA = SynchedEntityData.defineId(SoupCart.class, CartDataSerializers.SOUP_DATA.get());
	private static final EntityDataAccessor<Float> SOUP_AMOUNT = SynchedEntityData.defineId(SoupCart.class, EntityDataSerializers.FLOAT);
	private final List<SuspiciousEffectHolder.EffectEntry> effects = new ArrayList<>();


	public SoupCart(EntityType<?> type, Level level) {
		super(type, level);
	}

	public SoupCart(EntityType<?> type, Level level, double x, double y, double z) {
		super(type, level, x, y, z);
	}

	public InteractionResult interact(Player player, InteractionHand hand) {
		InteractionResult ret = super.interact(player, hand);
		if (ret.consumesAction()) return ret;
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (this.isVehicle()) {
			return InteractionResult.PASS;
		} else if (!this.level().isClientSide) {
			ItemStack stack = player.getItemInHand(hand);
			if (stack.is(ChowderExpress.SOUPS) || stack.is(Items.BOWL)) {
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
		if (stack.is(ChowderExpress.SOUPS)) {
			ResourceLocation soupLocation = BuiltInRegistries.ITEM.getKey(stack.getItem());
			if (soupLocation != null) {
				List<SuspiciousEffectHolder.EffectEntry> mobEffects = getStewEffects(stack);
				if (getSoupData().isEmpty()) {
					if (setSoupAmount(1)) {
						this.maybePlaySound(player);

						setSoupData(new SoupData(stack.copy(), stack.getFoodProperties(player)));
						if (!mobEffects.isEmpty()) {
							effects.addAll(mobEffects);
						}

						if (!player.getAbilities().instabuild) {
							stack.shrink(1);
							player.addItem(new ItemStack(Items.BOWL));
						}
						this.level().playSound(null, blockPosition(), CartRegistry.EMPTY_BOWL.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
						return InteractionResult.SUCCESS;
					}
				} else if (getSoupData().get().getLocation().equals(soupLocation)) {
					if (mobEffects.size() == this.effects.size()) {
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
		}
		if (stack.is(Items.BOWL) && getSoupData().isPresent() && getSoupAmount() >= 1.0) {
			ItemStack soupStack = getSoupData().get().getStack().copy();
			if (setSoupAmount(getSoupAmount() - 1.0F)) {
				stack.shrink(1);
				if (soupStack.is(Items.SUSPICIOUS_STEW)) {
					SuspiciousStewItem.saveMobEffects(soupStack, this.effects);
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

	private List<SuspiciousEffectHolder.EffectEntry> getStewEffects(ItemStack stack) {
		List<SuspiciousEffectHolder.EffectEntry> mobEffects = new ArrayList<>();
		if (stack.getItem() instanceof SuspiciousStewItem) {
			CompoundTag tag = stack.getTag();
			listPotionEffects(tag, instance -> mobEffects.add(instance));
		}
		return mobEffects;
	}

	private void listPotionEffects(CompoundTag tag, Consumer<SuspiciousEffectHolder.EffectEntry> consumer) {
		if (tag != null && tag.contains("effects", 9)) {
			SuspiciousEffectHolder.EffectEntry.LIST_CODEC
					.parse(NbtOps.INSTANCE, tag.getList("effects", 10))
					.result()
					.ifPresent(entries -> entries.forEach(consumer));
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(SOUP_DATA, Optional.empty());
		this.entityData.define(SOUP_AMOUNT, 0.0F);
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
			soupData = SoupData.readSoupData(tag.getCompound("soupData"));
		}
		setSoupData(soupData);

		if (soupData == null) {
			this.setSoupAmount(0);
		} else {
			this.setSoupAmount(tag.getFloat("SoupAmount"));
		}

		SuspiciousEffectHolder.EffectEntry.LIST_CODEC
				.parse(NbtOps.INSTANCE, tag.getList("ActiveEffects", 10))
				.result()
				.ifPresent(effects -> this.effects.addAll(effects));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.getSoupData().isPresent()) {
			tag.put("soupData", SoupData.writeSoupData(this.getSoupData().get()));
		}
		tag.putFloat("SoupAmount", this.getSoupAmount());
		if (!this.effects.isEmpty()) {
			SuspiciousEffectHolder.EffectEntry.LIST_CODEC
					.encodeStart(NbtOps.INSTANCE, this.effects)
					.result()
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
				if (!effects.isEmpty() && getSoupAmount() > 0.5F) {
					for (SuspiciousEffectHolder.EffectEntry entry : effects) {
						player.addEffect(entry.createEffectInstance());
					}
					this.setSoupAmount(getSoupAmount() - 0.5F);
					this.playSound(SoundEvents.GENERIC_DRINK, 0.5F, this.level().random.nextFloat() * 0.1F + 0.9F);
				} else if (player.getFoodData().needsFood() && getSoupAmount() > 0.25F) {
					player.getFoodData().eat(Math.min(1, (int) (soupData.getNutrition() / 2.0F)), soupData.getSaturationModifier() / 2.0F);
					this.setSoupAmount(getSoupAmount() - 0.25F);
					this.playSound(SoundEvents.GENERIC_DRINK, 0.5F, this.level().random.nextFloat() * 0.1F + 0.9F);
				}
			}
		}
	}

	public boolean addEffect(SuspiciousEffectHolder.EffectEntry effect) {
		if (!this.effects.contains(effect)) {
			return this.effects.add(effect);
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
