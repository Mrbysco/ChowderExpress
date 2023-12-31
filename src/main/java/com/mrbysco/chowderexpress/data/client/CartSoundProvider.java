package com.mrbysco.chowderexpress.data.client;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class CartSoundProvider extends SoundDefinitionsProvider {

	public CartSoundProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
		super(packOutput, ChowderExpress.MOD_ID, existingFileHelper);
	}

	@Override
	public void registerSounds() {
		this.add(CartRegistry.MM_SOUP, definition()
				.subtitle(modSubtitle(CartRegistry.MM_SOUP.getId()))
				.with(sound(modLoc("mm_soup"))));

		this.add(CartRegistry.EMPTY_BOWL, definition()
				.subtitle(modSubtitle(CartRegistry.EMPTY_BOWL.getId()))
				.with(
						sound(new ResourceLocation("item/bucket/empty1")),
						sound(new ResourceLocation("item/bucket/empty1")).pitch(0.9),
						sound(new ResourceLocation("item/bucket/empty2")),
						sound(new ResourceLocation("item/bucket/empty3"))
				));
		this.add(CartRegistry.FILL_BOWL, definition()
				.subtitle(modSubtitle(CartRegistry.FILL_BOWL.getId()))
				.with(
						sound(new ResourceLocation("item/bucket/fill1")),
						sound(new ResourceLocation("item/bucket/fill2")),
						sound(new ResourceLocation("item/bucket/fill3"))
				));
	}

	public String modSubtitle(ResourceLocation id) {
		return ChowderExpress.MOD_ID + ".subtitle." + id.getPath();
	}

	public ResourceLocation modLoc(String name) {
		return new ResourceLocation(ChowderExpress.MOD_ID, name);
	}
}
