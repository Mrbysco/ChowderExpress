package com.mrbysco.chowderexpress;

import com.mojang.logging.LogUtils;
import com.mrbysco.chowderexpress.client.ClientHandler;
import com.mrbysco.chowderexpress.registry.CartDataSerializers;
import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ChowderExpress.MOD_ID)
public class ChowderExpress {
	public static final String MOD_ID = "chowderexpress";
	private static final Logger LOGGER = LogUtils.getLogger();
	public static final TagKey<Item> SOUPS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(ChowderExpress.MOD_ID, "soups"));

	public ChowderExpress() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		CartRegistry.ITEMS.register(eventBus);
		CartRegistry.SOUND_EVENTS.register(eventBus);
		CartRegistry.ENTITY_TYPES.register(eventBus);
		CartDataSerializers.ENTITY_DATA_SERIALIZER.register(eventBus);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
		});
	}
}
