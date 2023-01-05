package com.mrbysco.chowderexpress.data.client;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

public class CartLanguageProvider extends LanguageProvider {
	public CartLanguageProvider(DataGenerator gen) {
		super(gen, ChowderExpress.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addEntityType(CartRegistry.SOUP_CART, "Soup Cart");
		addItem(CartRegistry.SOUP_CART_ITEM, "Soup Cart");

		addSubtitle(CartRegistry.MM_SOUP, "Mm soup");
		addSubtitle(CartRegistry.EMPTY_BOWL, "Emptying bowl");
		addSubtitle(CartRegistry.FILL_BOWL, "Filling bowl");
	}

	public void addSubtitle(RegistryObject<SoundEvent> sound, String name) {
		this.addSubtitle(sound.get(), name);
	}

	public void addSubtitle(SoundEvent sound, String name) {
		String path = ChowderExpress.MOD_ID + ".subtitle." + sound.getLocation().getPath();
		this.add(path, name);
	}
}
