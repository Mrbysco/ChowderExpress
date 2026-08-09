package com.mrbysco.chowderexpress.client.pipeline;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mrbysco.chowderexpress.Constants;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;

public class CartRenderPipelines {
	public static final RenderPipeline.Snippet SOUP_SNIPPET = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_LIGHT_DIR_SNIPPET)
			.withVertexShader("core/entity")
			.withFragmentShader("core/entity")
			.withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER2)
			.withVertexBinding(0, DefaultVertexFormat.ENTITY)
			.withPrimitiveTopology(PrimitiveTopology.QUADS)
			.withDepthStencilState(DepthStencilState.DEFAULT)
			.buildSnippet();

	public static final RenderPipeline SOUP = RenderPipeline.builder(SOUP_SNIPPET)
			.withLocation(Constants.modLoc("pipeline/soup"))
			.withShaderDefine("ALPHA_CUTOUT", 0.1F)
			.withBindGroupLayout(BindGroupLayouts.SAMPLER1)
			.withShaderDefine("PER_FACE_LIGHTING")
			.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
			.withCull(false)
			.build();
}
