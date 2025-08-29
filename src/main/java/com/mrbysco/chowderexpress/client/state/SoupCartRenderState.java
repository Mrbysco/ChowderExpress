package com.mrbysco.chowderexpress.client.state;

import com.mrbysco.chowderexpress.entity.SoupData;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;

import java.util.Optional;

public class SoupCartRenderState extends MinecartRenderState {
	public float soupAmount = 0;
	public Optional<SoupData> soupData = Optional.empty();
}
