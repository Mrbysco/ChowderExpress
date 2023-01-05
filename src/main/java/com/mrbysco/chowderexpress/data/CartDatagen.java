package com.mrbysco.chowderexpress.data;

import com.mrbysco.chowderexpress.data.client.CartItemModelProvider;
import com.mrbysco.chowderexpress.data.client.CartLanguageProvider;
import com.mrbysco.chowderexpress.data.client.CartSoundProvider;
import com.mrbysco.chowderexpress.data.server.CartItemTagProvider;
import com.mrbysco.chowderexpress.data.server.CartRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CartDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(new CartRecipes(generator));
			generator.addProvider(new CartItemTagProvider(generator, helper));
		}
		if (event.includeClient()) {
			generator.addProvider(new CartLanguageProvider(generator));
			generator.addProvider(new CartItemModelProvider(generator, helper));
			generator.addProvider(new CartSoundProvider(generator, helper));
		}
	}
}
