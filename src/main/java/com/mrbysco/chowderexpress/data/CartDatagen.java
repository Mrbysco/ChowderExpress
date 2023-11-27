package com.mrbysco.chowderexpress.data;

import com.mrbysco.chowderexpress.data.client.CartItemModelProvider;
import com.mrbysco.chowderexpress.data.client.CartLanguageProvider;
import com.mrbysco.chowderexpress.data.client.CartSoundProvider;
import com.mrbysco.chowderexpress.data.server.CartBlockTagProvider;
import com.mrbysco.chowderexpress.data.server.CartItemTagProvider;
import com.mrbysco.chowderexpress.data.server.CartRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CartDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeClient(), new CartRecipes(packOutput, lookupProvider));
			BlockTagsProvider blockTagsProvider;
			generator.addProvider(event.includeServer(), blockTagsProvider = new CartBlockTagProvider(packOutput, lookupProvider, helper));
			generator.addProvider(event.includeServer(), new CartItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), helper));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new CartLanguageProvider(packOutput));
			generator.addProvider(event.includeClient(), new CartItemModelProvider(packOutput, helper));
			generator.addProvider(event.includeClient(), new CartSoundProvider(packOutput, helper));
		}
	}
}
