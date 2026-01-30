package com.mrbysco.chowderexpress.registration;

import com.mrbysco.chowderexpress.Constants;
import com.mrbysco.chowderexpress.entity.SoupCart;
import com.mrbysco.chowderexpress.item.SoupCartItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;

public class CartRegistry {
	public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(BuiltInRegistries.ITEM, Constants.MOD_ID);
	public static final RegistrationProvider<SoundEvent> SOUND_EVENTS = RegistrationProvider.get(BuiltInRegistries.SOUND_EVENT, Constants.MOD_ID);
	public static final RegistrationProvider<EntityType<?>> ENTITY_TYPES = RegistrationProvider.get(BuiltInRegistries.ENTITY_TYPE, Constants.MOD_ID);

	public static final RegistryObject<SoundEvent> MM_SOUP = SOUND_EVENTS.register("mm_soup", () ->
			SoundEvent.createVariableRangeEvent(Constants.modLoc("mm_soup")));
	public static final RegistryObject<SoundEvent> FILL_BOWL = SOUND_EVENTS.register("fill_bowl", () ->
			SoundEvent.createVariableRangeEvent(Constants.modLoc("fill_bowl")));
	public static final RegistryObject<SoundEvent> EMPTY_BOWL = SOUND_EVENTS.register("empty_bowl", () ->
			SoundEvent.createVariableRangeEvent(Constants.modLoc("empty_bowl")));

	public static final RegistryObject<SoupCartItem> SOUP_CART_ITEM = ITEMS.register("soup_cart", () -> new SoupCartItem(
			new Item.Properties().setId(getKey("soup_cart"))
	));

	private static ResourceKey<Item> getKey(String name) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
	}

	public static final RegistryObject<EntityType<SoupCart>> SOUP_CART = ENTITY_TYPES.register("soup_cart", () ->
			EntityType.Builder.<SoupCart>of(SoupCart::new, MobCategory.MISC)
					.sized(0.98F, 0.7F).clientTrackingRange(8).build(getEntityKey("soup_cart")));

	public static ResourceKey<EntityType<?>> getEntityKey(String name) {
		return ResourceKey.create(BuiltInRegistries.ENTITY_TYPE.key(), Constants.modLoc(name));
	}

	// Called in the mod initializer / constructor in order to make sure that items are registered
	public static void loadClass() {
	}
}
