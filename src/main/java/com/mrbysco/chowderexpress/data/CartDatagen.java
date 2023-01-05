package com.mrbysco.chowderexpress.data;

import com.mrbysco.chowderexpress.data.client.CartItemModelProvider;
import com.mrbysco.chowderexpress.data.client.CartLanguageProvider;
import com.mrbysco.chowderexpress.data.client.CartSoundProvider;
import com.mrbysco.chowderexpress.data.server.CartItemTagProvider;
import com.mrbysco.chowderexpress.data.server.CartRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CartDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeClient(), new CartRecipes(generator));
			generator.addProvider(event.includeServer(), new CartItemTagProvider(generator, helper));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new CartLanguageProvider(generator));
			generator.addProvider(event.includeClient(), new CartItemModelProvider(generator, helper));
			generator.addProvider(event.includeClient(), new CartSoundProvider(generator, helper));
		}
	}
}
