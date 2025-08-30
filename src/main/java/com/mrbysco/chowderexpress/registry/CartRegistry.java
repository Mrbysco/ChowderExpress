package com.mrbysco.chowderexpress.registry;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.entity.SoupCart;
import com.mrbysco.chowderexpress.item.SoupCartItem;
import net.minecraft.core.registries.Registries;
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
	public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(ChowderExpress.MOD_ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> MM_SOUP = SOUND_EVENTS.register("mm_soup", () ->
			SoundEvent.createVariableRangeEvent(ChowderExpress.modLoc("mm_soup")));
	public static final DeferredHolder<SoundEvent, SoundEvent> FILL_BOWL = SOUND_EVENTS.register("fill_bowl", () ->
			SoundEvent.createVariableRangeEvent(ChowderExpress.modLoc("fill_bowl")));
	public static final DeferredHolder<SoundEvent, SoundEvent> EMPTY_BOWL = SOUND_EVENTS.register("empty_bowl", () ->
			SoundEvent.createVariableRangeEvent(ChowderExpress.modLoc("empty_bowl")));

	public static final DeferredItem<Item> SOUP_CART_ITEM = ITEMS.registerItem("soup_cart", SoupCartItem::new);

	public static final Supplier<EntityType<SoupCart>> SOUP_CART = ENTITY_TYPES.registerEntityType("soup_cart",
			SoupCart::new,
			MobCategory.MISC,
			builder -> builder
					.sized(0.98F, 0.7F)
					.passengerAttachments(0.1875F)
					.clientTrackingRange(8)
	);
}
