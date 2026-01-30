package com.mrbysco.chowderexpress.datagen.assets;

import com.mrbysco.chowderexpress.registration.CartRegistry;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class CartModelProvider extends FabricModelProvider {

	public CartModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockModels) {

	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModels) {
		itemModels.generateFlatItem(CartRegistry.SOUP_CART_ITEM.get(), ModelTemplates.FLAT_ITEM);
	}
}
