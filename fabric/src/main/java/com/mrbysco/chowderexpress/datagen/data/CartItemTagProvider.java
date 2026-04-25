package com.mrbysco.chowderexpress.datagen.data;

import com.mrbysco.chowderexpress.Constants;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;

public class CartItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

	public CartItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.builder(Constants.SOUPS).addOptionalTag(ConventionalItemTags.SOUP_FOODS);

		//Farmers Delight
		this.getOrCreateRawBuilder(Constants.SOUPS).addOptionalElement(Identifier.fromNamespaceAndPath("farmersdelight", "beef_stew"));
		this.getOrCreateRawBuilder(Constants.SOUPS).addOptionalElement(Identifier.fromNamespaceAndPath("farmersdelight", "chicken_soup"));
		this.getOrCreateRawBuilder(Constants.SOUPS).addOptionalElement(Identifier.fromNamespaceAndPath("farmersdelight", "vegetable_soup"));
		this.getOrCreateRawBuilder(Constants.SOUPS).addOptionalElement(Identifier.fromNamespaceAndPath("farmersdelight", "pumpkin_soup"));
		this.getOrCreateRawBuilder(Constants.SOUPS).addOptionalElement(Identifier.fromNamespaceAndPath("farmersdelight", "noodle_soup"));
		this.getOrCreateRawBuilder(Constants.SOUPS).addOptionalElement(Identifier.fromNamespaceAndPath("farmersdelight", "fish_stew"));
		this.getOrCreateRawBuilder(Constants.SOUPS).addOptionalElement(Identifier.fromNamespaceAndPath("farmersdelight", "baked_cod_stew"));

	}
}