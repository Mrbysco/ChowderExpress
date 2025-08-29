package com.mrbysco.chowderexpress.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mrbysco.chowderexpress.client.ClientHandler;
import com.mrbysco.chowderexpress.client.SoupRenderTypes;
import com.mrbysco.chowderexpress.client.model.SoupModel;
import com.mrbysco.chowderexpress.client.state.SoupCartRenderState;
import com.mrbysco.chowderexpress.entity.SoupCart;
import com.mrbysco.chowderexpress.entity.SoupData;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SoupCartRenderer extends EntityRenderer<SoupCart, SoupCartRenderState> {
	private static final ResourceLocation MINECART_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/minecart.png");
	protected final MinecartModel model;
	protected final SoupModel soupModel;
	private final BlockRenderDispatcher blockRenderer;

	public SoupCartRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.7F;
		this.model = new MinecartModel(context.bakeLayer(ModelLayers.MINECART));
		this.soupModel = new SoupModel(context.bakeLayer(ClientHandler.SOUP));
		this.blockRenderer = context.getBlockRenderDispatcher();
	}

	@Override
	public SoupCartRenderState createRenderState() {
		return new SoupCartRenderState();
	}

	@Override
	public void extractRenderState(SoupCart cart, SoupCartRenderState renderState, float partialTick) {
		super.extractRenderState(cart, renderState, partialTick);
		renderState.soupAmount = cart.getSoupAmount();
		renderState.soupData = cart.getSoupData();
	}

	@Override
	public void render(SoupCartRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.render(renderState, poseStack, bufferSource, packedLight);
		poseStack.pushPose();
		long i = renderState.offsetSeed;
		float f = (((float)(i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		float f1 = (((float)(i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		float f2 = (((float)(i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		poseStack.translate(f, f1, f2);
		if (renderState.isNewRender) {
			newRender(renderState, poseStack);
		} else {
			oldRender(renderState, poseStack);
		}

		float f3 = renderState.hurtTime;
		if (f3 > 0.0F) {
			poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f3) * f3 * renderState.damageTime / 10.0F * (float)renderState.hurtDir));
		}

		BlockState blockstate = renderState.displayBlockState;
		if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
			poseStack.pushPose();
			float f4 = 0.75F;
			poseStack.scale(0.75F, 0.75F, 0.75F);
			poseStack.translate(-0.5F, (float)(renderState.displayOffset - 8) / 16.0F, 0.5F);
			poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
			this.renderMinecartContents(renderState, blockstate, poseStack, bufferSource, packedLight);
			poseStack.popPose();
		}

		poseStack.scale(-1.0F, -1.0F, 1.0F);
		this.model.setupAnim(renderState);
		VertexConsumer vertexconsumer = bufferSource.getBuffer(this.model.renderType(MINECART_LOCATION));
		this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

		this.renderSoup(renderState, poseStack, bufferSource, packedLight);

		poseStack.popPose();
	}

	private static <S extends MinecartRenderState> void newRender(S renderState, PoseStack poseStack) {
		poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot));
		poseStack.mulPose(Axis.ZP.rotationDegrees(-renderState.xRot));
		poseStack.translate(0.0F, 0.375F, 0.0F);
	}

	private static <S extends MinecartRenderState> void oldRender(S renderState, PoseStack poseStack) {
		double d0 = renderState.x;
		double d1 = renderState.y;
		double d2 = renderState.z;
		float f = renderState.xRot;
		float f1 = renderState.yRot;
		if (renderState.posOnRail != null && renderState.frontPos != null && renderState.backPos != null) {
			Vec3 vec3 = renderState.frontPos;
			Vec3 vec31 = renderState.backPos;
			poseStack.translate(renderState.posOnRail.x - d0, (vec3.y + vec31.y) / 2.0 - d1, renderState.posOnRail.z - d2);
			Vec3 vec32 = vec31.add(-vec3.x, -vec3.y, -vec3.z);
			if (vec32.length() != 0.0) {
				vec32 = vec32.normalize();
				f1 = (float)(Math.atan2(vec32.z, vec32.x) * 180.0 / Math.PI);
				f = (float)(Math.atan(vec32.y) * 73.0);
			}
		}

		poseStack.translate(0.0F, 0.375F, 0.0F);
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - f1));
		poseStack.mulPose(Axis.ZP.rotationDegrees(-f));
	}

	private void renderSoup(SoupCartRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		float soupAmount = renderState.soupAmount;
		if (soupAmount > 0 && renderState.soupData.isPresent()) {
			final SoupData data = renderState.soupData.get();
			ResourceLocation soupKind = data.location();
			if (soupKind != null) {
				ResourceLocation soupLocation = ResourceLocation.fromNamespaceAndPath(soupKind.getNamespace(), "textures/soup/" + soupKind.getPath() + ".png");
				this.soupModel.setupAnim(renderState);

				VertexConsumer vertexconsumer = bufferSource.getBuffer(SoupRenderTypes.getSoup(soupLocation));
				this.soupModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
			}
		}
	}

	protected void renderMinecartContents(SoupCartRenderState renderState, BlockState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		this.blockRenderer.renderSingleBlock(state, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
	}
}