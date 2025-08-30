package com.mrbysco.chowderexpress.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record SoupData(ItemStack stack, int nutrition, float saturationModifier) {
	public static final Codec<SoupData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					ItemStack.SINGLE_ITEM_CODEC.fieldOf("stack").forGetter(SoupData::stack),
					Codec.INT.fieldOf("nutrition").forGetter(SoupData::nutrition),
					Codec.FLOAT.fieldOf("saturationModifier").forGetter(SoupData::saturationModifier))
			.apply(instance, SoupData::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, SoupData> STREAM_CODEC = StreamCodec.of(
			SoupData.Serializer::toNetwork, SoupData.Serializer::fromNetwork
	);

	public SoupData(ItemStack stack, @Nullable FoodProperties foodProperties) {
		this(stack, foodProperties != null ? foodProperties.nutrition() : 0, foodProperties != null ? foodProperties.saturation() : 0.0F);
	}


	public ResourceLocation location() {
		return BuiltInRegistries.ITEM.getKey(stack.getItem());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (SoupData) obj;
		return Objects.equals(this.stack, that.stack) &&
				Objects.equals(this.nutrition, that.nutrition) &&
				Objects.equals(this.saturationModifier, that.saturationModifier);
	}

	@Override
	public int hashCode() {
		return Objects.hash(stack, nutrition, saturationModifier);
	}

	@Override
	public String toString() {
		return "SoupData[" +
				"stack=" + stack + ", " +
				"nutrition=" + nutrition + ", " +
				"saturationModifier=" + saturationModifier + ']';
	}

	public static class Serializer {
		private static SoupData fromNetwork(RegistryFriendlyByteBuf byteBuf) {
			ItemStack itemstack = ItemStack.STREAM_CODEC.decode(byteBuf);
			int nutrition = byteBuf.readVarInt();
			float saturationModifier = byteBuf.readFloat();
			return new SoupData(itemstack, nutrition, saturationModifier);
		}

		private static void toNetwork(RegistryFriendlyByteBuf byteBuf, SoupData stack) {
			ItemStack.STREAM_CODEC.encode(byteBuf, stack.stack);
			byteBuf.writeVarInt(stack.nutrition());
			byteBuf.writeFloat(stack.saturationModifier());
		}
	}
}
