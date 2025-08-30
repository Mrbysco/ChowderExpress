package com.mrbysco.chowderexpress.client.pipeline;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mrbysco.chowderexpress.ChowderExpress;
import net.minecraft.client.renderer.RenderPipelines;

public class CartRenderPipelines {
	public static final RenderPipeline.Snippet SOUP_SNIPPET = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_LIGHT_DIR_SNIPPET)
			.withVertexShader("core/entity")
			.withFragmentShader("core/entity")
			.withSampler("Sampler0")
			.withSampler("Sampler2")
			.withVertexFormat(DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS)
			.buildSnippet();

	public static final RenderPipeline SOUP = RenderPipeline.builder(SOUP_SNIPPET)
			.withLocation(ChowderExpress.modLoc("pipeline/soup"))
			.withShaderDefine("ALPHA_CUTOUT", 0.1F)
			.withSampler("Sampler1")
			.withBlend(BlendFunction.TRANSLUCENT)
			.withCull(false)
			.build();
}
