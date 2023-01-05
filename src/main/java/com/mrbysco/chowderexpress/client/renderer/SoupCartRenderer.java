package com.mrbysco.chowderexpress.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.mrbysco.chowderexpress.client.ClientHandler;
import com.mrbysco.chowderexpress.client.SoupRenderTypes;
import com.mrbysco.chowderexpress.client.model.SoupModel;
import com.mrbysco.chowderexpress.entity.SoupCart;
import com.mrbysco.chowderexpress.entity.SoupData;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SoupCartRenderer<T extends SoupCart> extends EntityRenderer<T> {
	private static final ResourceLocation MINECART_LOCATION = new ResourceLocation("textures/entity/minecart.png");
	protected final MinecartModel<T> model;
	protected final SoupModel<T> soupModel;
	private final BlockRenderDispatcher blockRenderer;

	public SoupCartRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.7F;
		this.model = new MinecartModel<>(context.bakeLayer(ModelLayers.MINECART));
		this.soupModel = new SoupModel<>(context.bakeLayer(ClientHandler.SOUP));
		this.blockRenderer = context.getBlockRenderDispatcher();
	}

	public void render(T cart, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
		super.render(cart, entityYaw, partialTicks, poseStack, bufferSource, packedLightIn);
		poseStack.pushPose();
		long i = (long) cart.getId() * 493286711L;
		i = i * i * 4392167121L + i * 98761L;
		float f = (((float) (i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		float f1 = (((float) (i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		float f2 = (((float) (i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		poseStack.translate((double) f, (double) f1, (double) f2);
		double d0 = Mth.lerp((double) partialTicks, cart.xOld, cart.getX());
		double d1 = Mth.lerp((double) partialTicks, cart.yOld, cart.getY());
		double d2 = Mth.lerp((double) partialTicks, cart.zOld, cart.getZ());
		double d3 = (double) 0.3F;
		Vec3 vec3 = cart.getPos(d0, d1, d2);
		float f3 = Mth.lerp(partialTicks, cart.xRotO, cart.getXRot());
		if (vec3 != null) {
			Vec3 vec31 = cart.getPosOffs(d0, d1, d2, (double) d3);
			Vec3 vec32 = cart.getPosOffs(d0, d1, d2, (double) -d3);
			if (vec31 == null) {
				vec31 = vec3;
			}

			if (vec32 == null) {
				vec32 = vec3;
			}

			poseStack.translate(vec3.x - d0, (vec31.y + vec32.y) / 2.0D - d1, vec3.z - d2);
			Vec3 vec33 = vec32.add(-vec31.x, -vec31.y, -vec31.z);
			if (vec33.length() != 0.0D) {
				vec33 = vec33.normalize();
				entityYaw = (float) (Math.atan2(vec33.z, vec33.x) * 180.0D / Math.PI);
				f3 = (float) (Math.atan(vec33.y) * 73.0D);
			}
		}

		poseStack.translate(0.0D, 0.375D, 0.0D);
		poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(-f3));
		float f5 = (float) cart.getHurtTime() - partialTicks;
		float f6 = cart.getDamage() - partialTicks;
		if (f6 < 0.0F) {
			f6 = 0.0F;
		}

		if (f5 > 0.0F) {
			poseStack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(f5) * f5 * f6 / 10.0F * (float) cart.getHurtDir()));
		}

		int j = cart.getDisplayOffset();
		BlockState blockstate = cart.getDisplayBlockState();
		if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
			poseStack.pushPose();
			float f4 = 0.75F;
			poseStack.scale(f4, f4, f4);
			poseStack.translate(-0.5D, (double) ((float) (j - 8) / 16.0F), 0.5D);
			poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
			this.renderMinecartContents(cart, partialTicks, blockstate, poseStack, bufferSource, packedLightIn);
			poseStack.popPose();
		}

		poseStack.scale(-1.0F, -1.0F, 1.0F);
		this.model.setupAnim(cart, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		VertexConsumer vertexconsumer = bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(cart)));
		this.model.renderToBuffer(poseStack, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		this.renderSoup(cart, entityYaw, partialTicks, poseStack, bufferSource, packedLightIn);

		poseStack.popPose();
	}

	private void renderSoup(T cart, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
		float soupAmount = cart.getSoupAmount();
		if (soupAmount > 0 && cart.getSoupData().isPresent()) {
			final SoupData data = cart.getSoupData().get();
			ResourceLocation soupKind = data.getLocation();
			if (soupKind != null) {
				ResourceLocation soupLocation = new ResourceLocation(soupKind.getNamespace(), "textures/soup/" + soupKind.getPath() + ".png");
				this.model.copyPropertiesTo(this.soupModel);
				this.soupModel.setupAnim(cart, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

				VertexConsumer vertexconsumer = bufferSource.getBuffer(SoupRenderTypes.getSoup(soupLocation));
				this.soupModel.renderToBuffer(poseStack, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	public ResourceLocation getTextureLocation(T cart) {
		return MINECART_LOCATION;
	}

	protected void renderMinecartContents(T cart, float partialTicks, BlockState stateIn, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
		blockRenderer.renderSingleBlock(stateIn, poseStack, bufferSource, packedLightIn, OverlayTexture.NO_OVERLAY);
	}
}