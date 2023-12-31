package com.mrbysco.chowderexpress.item;

import com.mrbysco.chowderexpress.entity.SoupCart;
import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class SoupCartItem extends Item {

	private static final DispenseItemBehavior MINECART_DISPENSER_BEHAVIOR = new DefaultDispenseItemBehavior() {
		private final DefaultDispenseItemBehavior behaviourDefaultDispenseItem = new DefaultDispenseItemBehavior();

		/**
		 * Dispense the specified stack, play the dispense sound and spawn particles.
		 */
		public ItemStack execute(BlockSource source, ItemStack stack) {
			Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
			Level level = source.getLevel();
			double d0 = source.x() + (double) direction.getStepX() * 1.125D;
			double d1 = Math.floor(source.y()) + (double) direction.getStepY();
			double d2 = source.z() + (double) direction.getStepZ() * 1.125D;
			BlockPos blockpos = source.getPos().relative(direction);
			BlockState blockstate = level.getBlockState(blockpos);
			RailShape railshape = blockstate.getBlock() instanceof BaseRailBlock ? ((BaseRailBlock) blockstate.getBlock()).getRailDirection(blockstate, level, blockpos, null) : RailShape.NORTH_SOUTH;
			double d3;
			if (blockstate.is(BlockTags.RAILS)) {
				if (railshape.isAscending()) {
					d3 = 0.6D;
				} else {
					d3 = 0.1D;
				}
			} else {
				if (!blockstate.isAir() || !level.getBlockState(blockpos.below()).is(BlockTags.RAILS)) {
					return this.behaviourDefaultDispenseItem.dispense(source, stack);
				}

				BlockState blockstate1 = level.getBlockState(blockpos.below());
				RailShape railshape1 = blockstate1.getBlock() instanceof BaseRailBlock ? ((BaseRailBlock) blockstate1.getBlock()).getRailDirection(blockstate1, level, blockpos.below(), null) : RailShape.NORTH_SOUTH;
				if (direction != Direction.DOWN && railshape1.isAscending()) {
					d3 = -0.4D;
				} else {
					d3 = -0.9D;
				}
			}

			SoupCart soupCart = new SoupCart(CartRegistry.SOUP_CART.get(), level, d0, d1 + d3, d2);
			if (stack.hasCustomHoverName()) {
				soupCart.setCustomName(stack.getHoverName());
			}
			level.addFreshEntity(soupCart);
			stack.shrink(1);

			return stack;
		}

		/**
		 * Play the dispense sound from the specified block.
		 */
		protected void playSound(BlockSource source) {
			source.getLevel().levelEvent(1000, source.getPos(), 0);
		}
	};

	public SoupCartItem(Properties properties) {
		super(properties.stacksTo(1));
		DispenserBlock.registerBehavior(this, MINECART_DISPENSER_BEHAVIOR);
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		if (!state.is(BlockTags.RAILS)) {
			return InteractionResult.FAIL;
		} else {
			ItemStack itemstack = context.getItemInHand();
			if (!level.isClientSide) {
				RailShape shape = state.getBlock() instanceof BaseRailBlock ? ((BaseRailBlock) state.getBlock()).getRailDirection(state, level, pos, null) : RailShape.NORTH_SOUTH;
				double d0 = 0.0D;
				if (shape.isAscending()) {
					d0 = 0.5D;
				}

				SoupCart soupCart = new SoupCart(CartRegistry.SOUP_CART.get(), level,
						(double) pos.getX() + 0.5D, (double) pos.getY() + 0.0625D + d0, (double) pos.getZ() + 0.5D);
				if (itemstack.hasCustomHoverName()) {
					soupCart.setCustomName(itemstack.getHoverName());
				}

				level.addFreshEntity(soupCart);
			}

			itemstack.shrink(1);
			return InteractionResult.SUCCESS;
		}
	}
}
