package com.mrbysco.chowderexpress.data.server;

import com.mrbysco.chowderexpress.ChowderExpress;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CartItemTagProvider extends ItemTagsProvider {
	public CartItemTagProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {
		super(dataGenerator, new BlockTagsProvider(dataGenerator), ChowderExpress.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
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