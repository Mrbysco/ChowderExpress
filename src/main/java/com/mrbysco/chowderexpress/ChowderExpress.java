package com.mrbysco.chowderexpress;

import com.mojang.logging.LogUtils;
import com.mrbysco.chowderexpress.client.ClientHandler;
import com.mrbysco.chowderexpress.registry.CartDataSerializers;
import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(ChowderExpress.MOD_ID)
public class ChowderExpress {
	public static final String MOD_ID = "chowderexpress";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final TagKey<Item> SOUPS = TagKey.create(Registries.ITEM, modLoc("soups"));

	public ChowderExpress(IEventBus eventBus, Dist dist) {
		CartRegistry.ITEMS.register(eventBus);
		CartRegistry.SOUND_EVENTS.register(eventBus);
		CartRegistry.ENTITY_TYPES.register(eventBus);
		CartDataSerializers.ENTITY_DATA_SERIALIZER.register(eventBus);

		eventBus.addListener(this::buildCreativeContents);

		if (dist.isClient()) {
			eventBus.addListener(ClientHandler::registerPipelines);
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
		}
	}

	public static ResourceLocation modLoc(String path) {
		return ResourceLocation.fromNamespaceAndPath(ChowderExpress.MOD_ID, path);
	}

	private void buildCreativeContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.accept(new ItemStack(CartRegistry.SOUP_CART_ITEM.get()));
		}
	}
}
