package com.mrbysco.chowderexpress.data.client;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CartItemModelProvider extends ItemModelProvider {
	public CartItemModelProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, ChowderExpress.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		generatedItem(CartRegistry.SOUP_CART_ITEM.getId());
	}

	private void generatedItem(ResourceLocation location) {
		singleTexture(location.getPath(), ResourceLocation.withDefaultNamespace("item/generated"),
				"layer0", ChowderExpress.modLoc("item/" + location.getPath()));
	}
}
