package com.mrbysco.chowderexpress.platform.services;

import com.mrbysco.chowderexpress.entity.SoupData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

import java.util.Optional;

public interface IPlatformHelper {

	/**
	 * Get the rail direction of a rail block.
	 *
	 * @param state The block state
	 * @param level The level
	 * @param pos   The block position
	 * @return The rail shape
	 */
	RailShape getRailDirection(BlockState state, Level level, BlockPos pos);

	/**
	 * Get the soup data serializer.
	 *
	 * @return The soup data serializer
	 */
	EntityDataSerializer<Optional<SoupData>> getSoupSerializer();

	/**
	 * Get the food properties of a soup item stack for a player.
	 *
	 * @param stack  The item stack
	 * @param player The player consuming the soup
	 * @return The food properties
	 */
	FoodProperties getFoodProperties(ItemStack stack, Player player);
}
