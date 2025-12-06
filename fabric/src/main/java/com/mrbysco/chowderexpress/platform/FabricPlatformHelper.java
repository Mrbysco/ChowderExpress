package com.mrbysco.chowderexpress.platform;

import com.mrbysco.chowderexpress.ChowderExpress;
import com.mrbysco.chowderexpress.entity.SoupData;
import com.mrbysco.chowderexpress.platform.services.IPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

import java.util.Optional;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public RailShape getRailDirection(BlockState state, Level level, BlockPos pos) {
		return state.getValue(((BaseRailBlock) state.getBlock()).getShapeProperty());
	}

	@Override
	public EntityDataSerializer<Optional<SoupData>> getSoupSerializer() {
		return ChowderExpress.SOUP_DATA;
	}

	@Override
	public FoodProperties getFoodProperties(ItemStack stack, Player player) {
		return stack.get(DataComponents.FOOD);
	}
}
