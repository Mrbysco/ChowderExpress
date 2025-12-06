package com.mrbysco.chowderexpress;

import com.mrbysco.chowderexpress.client.SoupModellayer;
import com.mrbysco.chowderexpress.client.model.SoupModel;
import com.mrbysco.chowderexpress.client.renderer.SoupCartRenderer;
import com.mrbysco.chowderexpress.registration.CartRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ChowderExpressClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(SoupModellayer.SOUP, SoupModel::createBodyLayer);
		EntityRendererRegistry.register(CartRegistry.SOUP_CART.get(), SoupCartRenderer::new);
	}
}
