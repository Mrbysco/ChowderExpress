package com.mrbysco.chowderexpress;

import com.mrbysco.chowderexpress.entity.SoupData;
import com.mrbysco.chowderexpress.registration.CartRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.CreativeModeTabs;

import java.util.Optional;

public class ChowderExpress implements ModInitializer {

	public final static EntityDataSerializer<Optional<SoupData>> SOUP_DATA = EntityDataSerializer.forValueType(
			SoupData.STREAM_CODEC.apply(ByteBufCodecs::optional)
	);

	@Override
	public void onInitialize() {
		CommonClass.init();

		EntityDataSerializers.registerSerializer(SOUP_DATA);

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
			entries.accept(CartRegistry.SOUP_CART_ITEM.get());
		});
	}
}
