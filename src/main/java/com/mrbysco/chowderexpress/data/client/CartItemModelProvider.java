package com.mrbysco.chowderexpress.data.client;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CartItemModelProvider extends ItemModelProvider {
	public CartItemModelProvider(DataGenerator gen, ExistingFileHelper helper) {
		super(gen, ChowderExpress.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		generatedItem(CartRegistry.SOUP_CART_ITEM.getId());
	}

	private void generatedItem(ResourceLocation location) {
		singleTexture(location.getPath(), new ResourceLocation("item/generated"),
				"layer0", new ResourceLocation(ChowderExpress.MOD_ID, "item/" + location.getPath()));
	}
}
