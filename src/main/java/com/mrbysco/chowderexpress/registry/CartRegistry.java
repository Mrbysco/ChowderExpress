package com.mrbysco.chowderexpress.registry;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.entity.SoupCart;
import com.mrbysco.chowderexpress.item.SoupCartItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CartRegistry {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ChowderExpress.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, ChowderExpress.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, ChowderExpress.MOD_ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> MM_SOUP = SOUND_EVENTS.register("mm_soup", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(ChowderExpress.MOD_ID, "mm_soup")));
	public static final DeferredHolder<SoundEvent, SoundEvent> FILL_BOWL = SOUND_EVENTS.register("fill_bowl", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(ChowderExpress.MOD_ID, "fill_bowl")));
	public static final DeferredHolder<SoundEvent, SoundEvent> EMPTY_BOWL = SOUND_EVENTS.register("empty_bowl", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(ChowderExpress.MOD_ID, "empty_bowl")));

	public static final DeferredItem<Item> SOUP_CART_ITEM = ITEMS.register("soup_cart", () -> new SoupCartItem(new Item.Properties()));

	public static final Supplier<EntityType<SoupCart>> SOUP_CART = ENTITY_TYPES.register("soup_cart", () ->
			EntityType.Builder.<SoupCart>of(SoupCart::new, MobCategory.MISC)
					.sized(0.98F, 0.7F).clientTrackingRange(8).build("soup_cart"));
}
