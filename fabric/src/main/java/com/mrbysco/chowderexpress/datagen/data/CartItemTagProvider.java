package com.mrbysco.chowderexpress.datagen.data;

import com.mrbysco.chowderexpress.Constants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class CartItemTagProvider extends FabricTagProvider.ItemTagProvider {

	public CartItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(Constants.SOUPS).addOptionalTag(ConventionalItemTags.SOUP_FOODS.location());

		//Farmers Delight
		this.tag(Constants.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "beef_stew"));
		this.tag(Constants.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "chicken_soup"));
		this.tag(Constants.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "vegetable_soup"));
		this.tag(Constants.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "pumpkin_soup"));
		this.tag(Constants.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "noodle_soup"));
		this.tag(Constants.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "fish_stew"));
		this.tag(Constants.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "baked_cod_stew"));

	}
}