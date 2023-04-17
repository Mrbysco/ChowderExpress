package com.mrbysco.chowderexpress.data.server;

import com.mrbysco.chowderexpress.ChowderExpress;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CartItemTagProvider extends ItemTagsProvider {

	public CartItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
							   TagsProvider<Block> blockTagProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTagProvider, ChowderExpress.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider lookupProvider) {
		this.tag(ChowderExpress.SOUPS).add(Items.BEETROOT_SOUP, Items.MUSHROOM_STEW, Items.RABBIT_STEW, Items.SUSPICIOUS_STEW);

		//Enhanced Farming
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("enhancedfarming", "carrot_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("enhancedfarming", "chicken_noodle_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("enhancedfarming", "corn_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("enhancedfarming", "cucumber_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("enhancedfarming", "onion_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("enhancedfarming", "potato_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("enhancedfarming", "tomato_soup"));

		//Farmers Delight
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("farmersdelight", "beef_stew"));
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("farmersdelight", "chicken_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("farmersdelight", "vegetable_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("farmersdelight", "pumpkin_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("farmersdelight", "noodle_soup"));
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("farmersdelight", "fish_stew"));
		this.tag(ChowderExpress.SOUPS).addOptional(new ResourceLocation("farmersdelight", "baked_cod_stew"));

	}
}