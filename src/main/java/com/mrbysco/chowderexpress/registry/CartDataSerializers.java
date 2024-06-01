package com.mrbysco.chowderexpress.registry;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.entity.SoupData;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;
import java.util.function.Supplier;

public class CartDataSerializers {
	public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZER = DeferredRegister.create(
			NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, ChowderExpress.MOD_ID);

	public static final Supplier<EntityDataSerializer<Optional<SoupData>>> SOUP_DATA = ENTITY_DATA_SERIALIZER.register("soup_data", () ->
			EntityDataSerializer.forValueType(
					SoupData.STREAM_CODEC.apply(ByteBufCodecs::optional)
			)
	);
}
