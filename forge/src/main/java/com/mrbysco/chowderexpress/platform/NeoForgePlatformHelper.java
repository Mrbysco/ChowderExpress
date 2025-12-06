package com.mrbysco.chowderexpress.platform;

import com.mrbysco.chowderexpress.entity.SoupData;
import com.mrbysco.chowderexpress.platform.services.IPlatformHelper;
import com.mrbysco.chowderexpress.registry.CartDataSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

import java.util.Optional;

public class NeoForgePlatformHelper implements IPlatformHelper {

	@Override
	public RailShape getRailDirection(BlockState state, Level level, BlockPos pos) {
		return ((BaseRailBlock) state.getBlock()).getRailDirection(state, level, pos, null);
	}

	@Override
	public EntityDataSerializer<Optional<SoupData>> getSoupSerializer() {
		return CartDataSerializers.SOUP_DATA.get();
	}

	@Override
	public FoodProperties getFoodProperties(ItemStack stack, Player player) {
		return stack.getFoodProperties(player);
	}
}
