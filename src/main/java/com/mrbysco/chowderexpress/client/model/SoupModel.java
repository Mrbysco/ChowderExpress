package com.mrbysco.chowderexpress.client.model;

import com.mrbysco.chowderexpress.client.state.SoupCartRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SoupModel extends EntityModel<SoupCartRenderState> {

	public SoupModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("soup", CubeListBuilder.create()
						.texOffs(4, 12).addBox(-10.0F, -8.0F, -20.0F, 20.0F, 16.0F, 0.0F),
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, ((float) Math.PI / 2F), 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(SoupCartRenderState renderState) {
		super.setupAnim(renderState);
		root.y = -20.05F;
		float soupAmount = renderState.soupAmount;
		if (soupAmount > 0) {
			root.y -= soupAmount;
		}
	}
}