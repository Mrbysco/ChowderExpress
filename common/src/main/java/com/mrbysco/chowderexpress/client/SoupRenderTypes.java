package com.mrbysco.chowderexpress.client;

import com.mrbysco.chowderexpress.client.pipeline.CartRenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.function.BiFunction;

public abstract class SoupRenderTypes {

	private static final BiFunction<Identifier, Boolean, RenderType> SOUP = Util.memoize(
			(location, outline) -> {
				RenderSetup rendersetup = RenderSetup.builder(CartRenderPipelines.SOUP)
						.withTexture("Sampler0", location)
						.useLightmap()
						.useOverlay()
						.createRenderSetup();
				return RenderType.create("chowderexpress:soup", rendersetup);
			}
	);

	public static RenderType getSoup(Identifier texture) {
		return SOUP.apply(texture, false);
	}
}
