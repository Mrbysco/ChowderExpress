package com.mrbysco.chowderexpress.data.server;

import com.mrbysco.chowderexpress.ChowderExpress;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CartItemTagProvider extends ItemTagsProvider {

	public CartItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> providerCompletableFuture,
	                           CompletableFuture<TagsProvider.TagLookup<Block>> completableFuture, @Nullable ExistingFileHelper fileHelper) {
		super(packOutput, providerCompletableFuture, completableFuture, ChowderExpress.MOD_ID, fileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider lookupProvider) {
		this.tag(ChowderExpress.SOUPS).addTag(Tags.Items.FOODS_SOUP);

		//Farmers Delight
		this.tag(ChowderExpress.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "beef_stew"));
		this.tag(ChowderExpress.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "chicken_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "vegetable_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "pumpkin_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "noodle_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "fish_stew"));
		this.tag(ChowderExpress.SOUPS).addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "baked_cod_stew"));

	}
}