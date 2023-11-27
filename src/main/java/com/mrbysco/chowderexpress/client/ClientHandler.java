package com.mrbysco.chowderexpress.client;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.client.model.SoupModel;
import com.mrbysco.chowderexpress.client.renderer.SoupCartRenderer;
import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ClientHandler {
	public static final ModelLayerLocation SOUP = new ModelLayerLocation(new ResourceLocation(ChowderExpress.MOD_ID, "soup"), "main");

	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CartRegistry.SOUP_CART.get(), SoupCartRenderer::new);
	}

	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(SOUP, () -> SoupModel.createBodyLayer());
	}
}
