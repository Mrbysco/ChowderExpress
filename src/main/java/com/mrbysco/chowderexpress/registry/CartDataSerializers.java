package com.mrbysco.chowderexpress.registry;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.entity.SoupData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

public class CartDataSerializers {
	public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZER = DeferredRegister.create(
			ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, ChowderExpress.MOD_ID);

	public static final RegistryObject<EntityDataSerializer<Optional<SoupData>>> SOUP_DATA = ENTITY_DATA_SERIALIZER.register("soup_data", () ->
			EntityDataSerializer.optional(CartDataSerializers::writeSoup, CartDataSerializers::readSoup)
	);

	private static SoupData readSoup(FriendlyByteBuf friendlyByteBuf) {
		ItemStack stack = friendlyByteBuf.readItem();
		int nutrition = friendlyByteBuf.readInt();
		float saturationModifier = friendlyByteBuf.readFloat();
		return new SoupData(stack, nutrition, saturationModifier);
	}

	private static void writeSoup(FriendlyByteBuf friendlyByteBuf, SoupData soupData) {
		friendlyByteBuf.writeItem(soupData.getStack());
		friendlyByteBuf.writeInt(soupData.getNutrition());
		friendlyByteBuf.writeFloat(soupData.getSaturationModifier());
	}
}
