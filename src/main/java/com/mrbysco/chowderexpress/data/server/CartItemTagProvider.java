package com.mrbysco.chowderexpress.data.server;

import com.mrbysco.chowderexpress.ChowderExpress;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class CartItemTagProvider extends ItemTagsProvider {

	public CartItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider, ChowderExpress.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(ChowderExpress.SOUPS).addTag(Tags.Items.FOODS_SOUP);

		//Farmers Delight
		TagBuilder builder = TagBuilder.create();
		builder.addOptionalElement(ResourceLocation.fromNamespaceAndPath("farmersdelight", "beef_stew"));
		builder.addOptionalElement(ResourceLocation.fromNamespaceAndPath("farmersdelight", "chicken_soup"));
		builder.addOptionalElement(ResourceLocation.fromNamespaceAndPath("farmersdelight", "vegetable_soup"));
		builder.addOptionalElement(ResourceLocation.fromNamespaceAndPath("farmersdelight", "pumpkin_soup"));
		builder.addOptionalElement(ResourceLocation.fromNamespaceAndPath("farmersdelight", "noodle_soup"));
		builder.addOptionalElement(ResourceLocation.fromNamespaceAndPath("farmersdelight", "fish_stew"));
		builder.addOptionalElement(ResourceLocation.fromNamespaceAndPath("farmersdelight", "baked_cod_stew"));
		builder.build().forEach(this.tag(ChowderExpress.SOUPS)::add);
	}
}