package com.mrbysco.chowderexpress;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mrbysco.chowderexpress.client.SoupModellayer;
import com.mrbysco.chowderexpress.client.model.SoupModel;
import com.mrbysco.chowderexpress.client.pipeline.CartRenderPipelines;
import com.mrbysco.chowderexpress.client.renderer.SoupCartRenderer;
import com.mrbysco.chowderexpress.registration.CartRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class ChowderExpressClient implements ClientModInitializer {
	public static final RenderPipeline SOUP_PIPELINE = RenderPipelines.register(CartRenderPipelines.SOUP);

	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(SoupModellayer.SOUP, SoupModel::createBodyLayer);
		EntityRenderers.register(CartRegistry.SOUP_CART.get(), SoupCartRenderer::new);
	}
}
