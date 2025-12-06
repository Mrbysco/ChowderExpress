package com.mrbysco.chowderexpress;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

public class Constants {
	public static final String MOD_ID = "chowderexpress";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final TagKey<Item> SOUPS = TagKey.create(Registries.ITEM, modLoc("soups"));

	public static ResourceLocation modLoc(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}