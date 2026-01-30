package com.mrbysco.chowderexpress.data.client;

import com.mrbysco.chowderexpress.Constants;
import com.mrbysco.chowderexpress.registration.CartRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class CartSoundProvider extends SoundDefinitionsProvider {

	public CartSoundProvider(PackOutput packOutput) {
		super(packOutput, Constants.MOD_ID);
	}

	@Override
	public void registerSounds() {
		this.add(CartRegistry.MM_SOUP.get(), definition()
				.subtitle(modSubtitle(CartRegistry.MM_SOUP.getId()))
				.with(sound(Constants.modLoc("mm_soup"))));

		this.add(CartRegistry.EMPTY_BOWL.get(), definition()
				.subtitle(modSubtitle(CartRegistry.EMPTY_BOWL.getId()))
				.with(
						sound(Identifier.withDefaultNamespace("item/bucket/empty1")),
						sound(Identifier.withDefaultNamespace("item/bucket/empty1")).pitch(0.9),
						sound(Identifier.withDefaultNamespace("item/bucket/empty2")),
						sound(Identifier.withDefaultNamespace("item/bucket/empty3"))
				));
		this.add(CartRegistry.FILL_BOWL.get(), definition()
				.subtitle(modSubtitle(CartRegistry.FILL_BOWL.getId()))
				.with(
						sound(Identifier.withDefaultNamespace("item/bucket/fill1")),
						sound(Identifier.withDefaultNamespace("item/bucket/fill2")),
						sound(Identifier.withDefaultNamespace("item/bucket/fill3"))
				));
	}

	public String modSubtitle(Identifier id) {
		return Constants.MOD_ID + ".subtitle." + id.getPath();
	}
}
