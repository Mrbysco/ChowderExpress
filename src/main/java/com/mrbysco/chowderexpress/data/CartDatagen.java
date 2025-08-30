package com.mrbysco.chowderexpress.data;

import com.mrbysco.chowderexpress.data.client.CartLanguageProvider;
import com.mrbysco.chowderexpress.data.client.CartModelProvider;
import com.mrbysco.chowderexpress.data.client.CartSoundProvider;
import com.mrbysco.chowderexpress.data.server.CartItemTagProvider;
import com.mrbysco.chowderexpress.data.server.CartRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class CartDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

			generator.addProvider(true, new CartRecipeProvider.Runner(packOutput, lookupProvider));
			generator.addProvider(true, new CartItemTagProvider(packOutput, lookupProvider));

			generator.addProvider(true, new CartLanguageProvider(packOutput));
			generator.addProvider(true, new CartModelProvider(packOutput));
			generator.addProvider(true, new CartSoundProvider(packOutput));

	}
}
