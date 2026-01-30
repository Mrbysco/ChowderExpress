package com.mrbysco.chowderexpress.datagen.assets;

import com.mrbysco.chowderexpress.Constants;
import com.mrbysco.chowderexpress.registration.CartRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.sounds.SoundEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CartLanguageProvider extends FabricLanguageProvider {
	public CartLanguageProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(output, registryLookup);
	}

	@Override
	public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder builder) {
		builder.add(CartRegistry.SOUP_CART.get(), "Soup Cart");
		builder.add(CartRegistry.SOUP_CART_ITEM.get(), "Soup Cart");

		addSubtitle(builder, CartRegistry.MM_SOUP, "Mm soup");
		addSubtitle(builder, CartRegistry.EMPTY_BOWL, "Emptying bowl");
		addSubtitle(builder, CartRegistry.FILL_BOWL, "Filling bowl");
	}

	public void addSubtitle(TranslationBuilder builder, Supplier<SoundEvent> sound, String name) {
		this.addSubtitle(builder, sound.get(), name);
	}

	public void addSubtitle(TranslationBuilder builder, SoundEvent sound, String name) {
		String path = Constants.MOD_ID + ".subtitle." + sound.location().getPath();
		builder.add(path, name);
	}
}
