package com.mrbysco.chowderexpress.registry;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.entity.SoupData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

public class CartDataSerializers {
	public static final DeferredRegister<DataSerializerEntry> ENTITY_DATA_SERIALIZER = DeferredRegister.create(
			ForgeRegistries.Keys.DATA_SERIALIZERS, ChowderExpress.MOD_ID);

	public static final RegistryObject<DataSerializerEntry> SOUP_DATA = ENTITY_DATA_SERIALIZER.register("soup_data", () ->
			new DataSerializerEntry(
					new EntityDataSerializer<Optional<SoupData>>() {
						@Override
						public void write(FriendlyByteBuf friendlyByteBuf, Optional<SoupData> optionalSoupData) {
							friendlyByteBuf.writeBoolean(optionalSoupData.isPresent());
							if (optionalSoupData.isPresent()) {
								SoupData soupData = optionalSoupData.get();
								friendlyByteBuf.writeItem(soupData.getStack());
								friendlyByteBuf.writeInt(soupData.getNutrition());
								friendlyByteBuf.writeFloat(soupData.getSaturationModifier());
							}
						}

						@Override
						public Optional<SoupData> read(FriendlyByteBuf friendlyByteBuf) {
							boolean isPresent = friendlyByteBuf.readBoolean();
							if (isPresent) {
								ItemStack stack = friendlyByteBuf.readItem();
								int nutrition = friendlyByteBuf.readInt();
								float saturationModifier = friendlyByteBuf.readFloat();
								return Optional.of(new SoupData(stack, nutrition, saturationModifier));
							}
							return Optional.empty();
						}

						@Override
						public Optional<SoupData> copy(Optional<SoupData> optionalSoupData) {
							return optionalSoupData;
						}
					}
			)
	);
}
