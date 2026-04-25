package com.mrbysco.chowderexpress;

import com.mrbysco.chowderexpress.entity.SoupData;
import com.mrbysco.chowderexpress.registration.CartRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.item.CreativeModeTabs;

import java.util.Optional;

public class ChowderExpress implements ModInitializer {

	public final static EntityDataSerializer<Optional<SoupData>> SOUP_DATA = EntityDataSerializer.forValueType(
			SoupData.STREAM_CODEC.apply(ByteBufCodecs::optional)
	);

	@Override
	public void onInitialize() {
		CommonClass.init();

		FabricEntityDataRegistry.register(Constants.modLoc("soup_data"), SOUP_DATA);

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
			entries.accept(CartRegistry.SOUP_CART_ITEM.get());
		});
	}
}
