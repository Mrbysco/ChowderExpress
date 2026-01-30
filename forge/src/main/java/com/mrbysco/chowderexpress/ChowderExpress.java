package com.mrbysco.chowderexpress;

import com.mrbysco.chowderexpress.client.ClientHandler;
import com.mrbysco.chowderexpress.registration.CartRegistry;
import com.mrbysco.chowderexpress.registry.CartDataSerializers;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(Constants.MOD_ID)
public class ChowderExpress {

	public ChowderExpress(IEventBus eventBus, Dist dist) {
		CommonClass.init();

		CartDataSerializers.ENTITY_DATA_SERIALIZER.register(eventBus);

		eventBus.addListener(this::buildCreativeContents);

		if (dist.isClient()) {
			eventBus.addListener(ClientHandler::registerPipelines);
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
		}
	}

	private void buildCreativeContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.accept(CartRegistry.SOUP_CART_ITEM.get());
		}
	}
}