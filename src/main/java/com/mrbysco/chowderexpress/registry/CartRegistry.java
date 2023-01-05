package com.mrbysco.chowderexpress.registry;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.entity.SoupCart;
import com.mrbysco.chowderexpress.item.SoupCartItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CartRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ChowderExpress.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ChowderExpress.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, ChowderExpress.MOD_ID);

	public static final RegistryObject<SoundEvent> MM_SOUP = SOUND_EVENTS.register("mm_soup", () ->
			new SoundEvent(new ResourceLocation(ChowderExpress.MOD_ID, "mm_soup")));
	public static final RegistryObject<SoundEvent> FILL_BOWL = SOUND_EVENTS.register("fill_bowl", () ->
			new SoundEvent(new ResourceLocation(ChowderExpress.MOD_ID, "fill_bowl")));
	public static final RegistryObject<SoundEvent> EMPTY_BOWL = SOUND_EVENTS.register("empty_bowl", () ->
			new SoundEvent(new ResourceLocation(ChowderExpress.MOD_ID, "empty_bowl")));

	public static final RegistryObject<Item> SOUP_CART_ITEM = ITEMS.register("soup_cart", () -> new SoupCartItem(
			new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION)));

	public static final RegistryObject<EntityType<SoupCart>> SOUP_CART = ENTITY_TYPES.register("soup_cart", () ->
			EntityType.Builder.<SoupCart>of(SoupCart::new, MobCategory.MISC)
					.sized(0.98F, 0.7F).clientTrackingRange(8)
					.setCustomClientFactory(SoupCart::new).build("soup_cart"));
}
