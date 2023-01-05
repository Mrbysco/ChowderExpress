package com.mrbysco.chowderexpress.entity;

import com.google.common.collect.Maps;
import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.registry.CartDataSerializers;
import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SoupCart extends AbstractMinecart {
	private static final EntityDataAccessor<Optional<SoupData>> SOUP_DATA = SynchedEntityData.defineId(SoupCart.class, CartDataSerializers.SOUP_DATA.get());
	private static final EntityDataAccessor<Float> SOUP_AMOUNT = SynchedEntityData.defineId(SoupCart.class, EntityDataSerializers.FLOAT);
	private final Map<MobEffect, MobEffectInstance> effects = Maps.newHashMap();


	public SoupCart(EntityType<?> type, Level level) {
		super(type, level);
	}

	public SoupCart(EntityType<?> type, Level level, double x, double y, double z) {
		super(type, level, x, y, z);
	}

	public SoupCart(PlayMessages.SpawnEntity spawnEntity, Level level) {
		this(CartRegistry.SOUP_CART.get(), level);
	}

	public InteractionResult interact(Player player, InteractionHand hand) {
		InteractionResult ret = super.interact(player, hand);
		if (ret.consumesAction()) return ret;
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (this.isVehicle()) {
			return InteractionResult.PASS;
		} else if (!this.level.isClientSide) {
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
			ResourceLocation soupLocation = ForgeRegistries.ITEMS.getKey(stack.getItem());
			if (soupLocation != null) {
				Map<MobEffect, MobEffectInstance> mobEffects = getStewEffects(stack);
				if (getSoupData().isEmpty()) {
					if (setSoupAmount(1)) {
						this.maybePlaySound(player);

						setSoupData(new SoupData(stack, stack.getFoodProperties(null)));
						if (!effects.isEmpty()) {
							effects.putAll(mobEffects);
						}

						stack.shrink(1);
						player.addItem(new ItemStack(Items.BOWL));
						level.playSound(null, blockPosition(), CartRegistry.EMPTY_BOWL.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
						return InteractionResult.SUCCESS;
					}
				} else if (getSoupData().get().getLocation().equals(soupLocation)) {
					if (mobEffects.keySet().equals(effects.keySet())) {
						if (setSoupAmount(getSoupAmount() + 1)) {
							this.maybePlaySound(player);
							stack.shrink(1);
							player.addItem(new ItemStack(Items.BOWL));
							level.playSound(null, blockPosition(), CartRegistry.EMPTY_BOWL.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
							return InteractionResult.SUCCESS;
						}
					}
				}
			}
		}
		if (stack.is(Items.BOWL) && getSoupData().isPresent() && getSoupAmount() > 1.0) {
			if (setSoupAmount(getSoupAmount() - 1.0F)) {
				stack.shrink(1);
				ItemStack soupStack = getSoupData().get().getStack();
				if (soupStack.is(Items.SUSPICIOUS_STEW)) {
					for (Map.Entry<MobEffect, MobEffectInstance> entry : effects.entrySet()) {
						SuspiciousStewItem.saveMobEffect(soupStack, entry.getKey(), entry.getValue().getDuration());
					}
				}

				player.addItem(soupStack);
				level.playSound(null, blockPosition(), CartRegistry.FILL_BOWL.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
			}
		}

		return InteractionResult.PASS;
	}

	public void maybePlaySound(Player player) {
		player.displayClientMessage(Component.literal("Mm soup"), true);
		level.playSound(null, blockPosition(), CartRegistry.MM_SOUP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
	}

	private Map<MobEffect, MobEffectInstance> getStewEffects(ItemStack stack) {
		Map<MobEffect, MobEffectInstance> mobEffects = new HashMap<>();
		if (stack.getItem() instanceof SuspiciousStewItem) {
			CompoundTag compoundtag = stack.getTag();
			if (compoundtag != null && compoundtag.contains("Effects", 9)) {
				ListTag listtag = compoundtag.getList("Effects", 10);

				for (int i = 0; i < listtag.size(); ++i) {
					int j = 160;
					CompoundTag listtagCompound = listtag.getCompound(i);
					if (listtagCompound.contains("EffectDuration", 3)) {
						j = listtagCompound.getInt("EffectDuration");
					}

					MobEffect mobeffect = MobEffect.byId(listtagCompound.getInt("EffectId"));
					mobeffect = net.minecraftforge.common.ForgeHooks.loadMobEffect(listtagCompound, "forge:effect_id", mobeffect);
					if (mobeffect != null) {
						mobEffects.put(mobeffect, new MobEffectInstance(mobeffect, j));
					}
				}
			}
		}
		return mobEffects;
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

		if (tag.contains("ActiveEffects", 9)) {
			ListTag listtag = tag.getList("ActiveEffects", 10);

			for (int i = 0; i < listtag.size(); ++i) {
				CompoundTag compoundtag = listtag.getCompound(i);
				MobEffectInstance mobeffectinstance = MobEffectInstance.load(compoundtag);
				if (mobeffectinstance != null) {
					this.addEffect(mobeffectinstance);
				}
			}
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.getSoupData().isPresent()) {
			tag.put("soupData", SoupData.writeSoupData(this.getSoupData().get()));
		}
		tag.putFloat("SoupAmount", this.getSoupAmount());
		if (!this.effects.isEmpty()) {
			ListTag listtag = new ListTag();

			for (MobEffectInstance mobeffectinstance : this.effects.values()) {
				listtag.add(mobeffectinstance.save(new CompoundTag()));
			}

			tag.put("ActiveEffects", listtag);
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
		if (level.getGameTime() % 20 == 0) {
			Entity entity = this.getFirstPassenger();
			if (entity instanceof Player player && hasPassenger(player) && random.nextBoolean() && getSoupData().isPresent()) {
				SoupData soupData = getSoupData().get();
				if (!effects.isEmpty() && getSoupAmount() > 0.5F) {
					for (Map.Entry<MobEffect, MobEffectInstance> entry : effects.entrySet()) {
						player.addEffect(entry.getValue());
					}
					this.setSoupAmount(getSoupAmount() - 0.5F);
					this.playSound(SoundEvents.GENERIC_DRINK, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
				} else if (player.getFoodData().needsFood() && getSoupAmount() > 0.25F) {
					player.getFoodData().eat(Math.min(1, (int) (soupData.getNutrition() / 2.0F)), soupData.getSaturationModifier() / 2.0F);
					this.setSoupAmount(getSoupAmount() - 0.25F);
					this.playSound(SoundEvents.GENERIC_DRINK, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
				}
			}
		}
	}

	public boolean addEffect(MobEffectInstance effect) {
		MobEffectInstance mobeffectinstance = this.effects.get(effect.getEffect());
		if (mobeffectinstance == null) {
			this.effects.put(effect.getEffect(), effect);
			return true;
		} else if (mobeffectinstance.update(effect)) {
			return true;
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

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
