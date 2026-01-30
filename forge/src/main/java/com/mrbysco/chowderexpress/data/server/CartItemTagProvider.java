package com.mrbysco.chowderexpress.data.server;

import com.mrbysco.chowderexpress.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class CartItemTagProvider extends ItemTagsProvider {

	public CartItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider, Constants.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider lookupProvider) {
		this.tag(Constants.SOUPS).addTag(Tags.Items.FOODS_SOUP);

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