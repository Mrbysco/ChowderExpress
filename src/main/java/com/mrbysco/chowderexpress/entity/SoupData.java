package com.mrbysco.chowderexpress.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class SoupData {
	private final ItemStack stack;
	private final ResourceLocation location;
	private final int nutrition;
	private final float saturationModifier;

	public SoupData(ItemStack stack, int nutrition, float saturationModifier) {
		this.stack = stack;
		this.location = BuiltInRegistries.ITEM.getKey(stack.getItem());
		this.nutrition = nutrition;
		this.saturationModifier = saturationModifier;
	}

	public SoupData(ItemStack stack, @Nullable FoodProperties foodProperties) {
		this.stack = stack;
		this.location = BuiltInRegistries.ITEM.getKey(stack.getItem());
		if (foodProperties != null) {
			this.nutrition = foodProperties.getNutrition();
			this.saturationModifier = foodProperties.getSaturationModifier();
		} else {
			this.nutrition = 0;
			this.saturationModifier = 0;
		}
	}

	public ItemStack getStack() {
		return stack;
	}

	public ResourceLocation getLocation() {
		return location;
	}

	public int getNutrition() {
		return nutrition;
	}

	public float getSaturationModifier() {
		return saturationModifier;
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

	public static SoupData readSoupData(CompoundTag tag) {
		ItemStack soupStack = ItemStack.of(tag.getCompound("soupStack"));
		int nutrition = tag.getInt("nutrition");
		float saturationModifier = tag.getFloat("saturationModifier");
		return new SoupData(soupStack, nutrition, saturationModifier);
	}

	public static CompoundTag writeSoupData(SoupData soupData) {
		CompoundTag tag = new CompoundTag();
		tag.put("soupStack", soupData.getStack().save(new CompoundTag()));
		tag.putInt("nutrition", soupData.getNutrition());
		tag.putFloat("saturationModifier", soupData.getSaturationModifier());
		return tag;
	}
}
