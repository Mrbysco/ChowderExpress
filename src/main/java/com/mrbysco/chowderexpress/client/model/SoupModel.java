package com.mrbysco.chowderexpress.client.model;

import com.mrbysco.chowderexpress.entity.SoupCart;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SoupModel<T extends SoupCart> extends HierarchicalModel<T> {
	private final ModelPart root;

	public SoupModel(ModelPart root) {
		this.root = root;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("soup", CubeListBuilder.create()
						.texOffs(4, 12).addBox(-10.0F, -8.0F, -20.0F, 20.0F, 16.0F, 0.0F),
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, ((float) Math.PI / 2F), 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	public void setupAnim(T soupCart, float p_103101_, float p_103102_, float p_103103_, float p_103104_, float p_103105_) {
		final float soupAmount = soupCart.getSoupAmount();
		root.y = -20.05F;
		if (soupAmount > 0) {
			root.y -= soupAmount;
		}
	}

	public ModelPart root() {
		return this.root;
	}
}