package com.mrbysco.chowderexpress.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrbysco.chowderexpress.client.pipeline.CartRenderPipelines;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.BiFunction;

public abstract class SoupRenderTypes extends RenderType {

	public SoupRenderTypes(String name, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
		super(name, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
	}

	public static final BiFunction<ResourceLocation, Boolean, RenderType> SOUP = Util.memoize(
			(location, outline) -> {
				RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
						.setTextureState(new SoupTextureStateShard(location, false, false))
						.setLightmapState(LIGHTMAP)
						.setOverlayState(OVERLAY)
						.createCompositeState(true);
				return create("chowderexpress:soup", 1536, true, true,
						CartRenderPipelines.SOUP, rendertype$compositestate);
			}
	);

	public static RenderType getSoup(ResourceLocation texture) {
		return SOUP.apply(texture, false);
	}

	public static class SoupTextureStateShard extends RenderStateShard.EmptyTextureStateShard {
		private final Optional<ResourceLocation> texture;
		protected boolean blur;
		protected boolean mipmap;

		public SoupTextureStateShard(ResourceLocation resourceLocation, boolean blur, boolean mipmap) {
			super(() -> {
				TextureHelper textureHelper = new TextureHelper(Minecraft.getInstance().getTextureManager());
				AbstractTexture texture = textureHelper.getTexture(resourceLocation);
				texture.setFilter(blur, mipmap);
				RenderSystem.setShaderTexture(0, texture.getTexture());
			}, () -> {
			});
			this.texture = Optional.of(resourceLocation);
			this.blur = blur;
			this.mipmap = mipmap;
		}

		public String toString() {
			return this.name + "[" + this.texture + "(blur=" + this.blur + ", mipmap=" + this.mipmap + ")]";
		}

		protected Optional<ResourceLocation> cutoutTexture() {
			return this.texture;
		}
	}
}
