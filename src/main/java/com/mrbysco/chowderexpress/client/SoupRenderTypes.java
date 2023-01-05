package com.mrbysco.chowderexpress.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mrbysco.chowderexpress.ChowderExpress;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public class SoupRenderTypes extends RenderType {
	public SoupRenderTypes(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
	}

	private static final Function<ResourceLocation, RenderType> SOUP = Util.memoize((resourceLocation) ->
			create("chowderexpress:soup",
					DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true,
					CompositeState.builder()
							.setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
							.setTextureState(new SoupTextureStateShard(resourceLocation, false, false))
							.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
							.setCullState(NO_CULL)
							.setLightmapState(LIGHTMAP)
							.setOverlayState(OVERLAY)
							.createCompositeState(true)));

	public static RenderType getSoup(ResourceLocation texture) {
		return SOUP.apply(texture);
	}

	public static class SoupTextureStateShard extends RenderStateShard.EmptyTextureStateShard {
		private static final ResourceLocation DEFAULT_SOUP = new ResourceLocation(ChowderExpress.MOD_ID, "textures/soup/default_soup.png");
		private final Optional<ResourceLocation> texture;
		protected boolean blur;
		protected boolean mipmap;

		public SoupTextureStateShard(ResourceLocation resourceLocation, boolean blur, boolean mipmap) {
			super(() -> {
				RenderSystem.enableTexture();
				TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
				texturemanager.getTexture(resourceLocation);
				AbstractTexture texture = texturemanager.getTexture(resourceLocation, null);
				if (texture == null) {
					texture = loadTexture(texturemanager.getTexture(resourceLocation));
				}
				if (texture == MissingTextureAtlasSprite.getTexture()) {
					//Default soup texture if no specific texture is found
					AbstractTexture abstracttexture = new SimpleTexture(DEFAULT_SOUP);
					abstracttexture.setFilter(blur, mipmap);
					texturemanager.register(resourceLocation, abstracttexture);
				}
				RenderSystem.setShaderTexture(0, resourceLocation);
			}, () -> {
			});
			this.texture = Optional.of(resourceLocation);
			this.blur = blur;
			this.mipmap = mipmap;
		}

		/**
		 * Custom loadTexture method to stop the missing texture error from logging
		 */
		private static AbstractTexture loadTexture(AbstractTexture texture) {
			try {
				texture.load(Minecraft.getInstance().getResourceManager());
				return texture;
			} catch (IOException ioexception) {
				return MissingTextureAtlasSprite.getTexture();
			}
		}

		public String toString() {
			return this.name + "[" + this.texture + "(blur=" + this.blur + ", mipmap=" + this.mipmap + ")]";
		}

		protected Optional<ResourceLocation> cutoutTexture() {
			return this.texture;
		}
	}
}
