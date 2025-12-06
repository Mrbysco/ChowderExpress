package com.mrbysco.chowderexpress.datagen;

import com.mrbysco.chowderexpress.datagen.assets.CartLanguageProvider;
import com.mrbysco.chowderexpress.datagen.assets.CartModelProvider;
import com.mrbysco.chowderexpress.datagen.assets.CartSoundProvider;
import com.mrbysco.chowderexpress.datagen.data.CartItemTagProvider;
import com.mrbysco.chowderexpress.datagen.data.CartRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class CartDatagen implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(CartRecipeProvider::new);
		pack.addProvider(CartItemTagProvider::new);

		pack.addProvider(CartLanguageProvider::new);
		pack.addProvider(CartModelProvider::new);
		pack.addProvider(CartSoundProvider::new);

	}
}