package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class WaterTallFlowerBlock extends BMTallFlowerBlock implements FluidFillable
{
	public WaterTallFlowerBlock(Settings settings)
	{
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(HALF, DoubleBlockHalf.LOWER));
	}

	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		if(state.get(HALF) == DoubleBlockHalf.LOWER)
			return super.canPlaceAt(state, world, pos) && world.getFluidState(pos).getFluid() == Fluids.WATER;

		return super.canPlaceAt(state, world, pos);
	}

	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		BlockPos pos = ctx.getBlockPos();
		return pos.getY() < 255 && (ctx.getWorld().getBlockState(pos.up()).canReplace(ctx) && ctx.getWorld().getFluidState(pos.up()).isEmpty()) ? super.getPlacementState(ctx) : null;
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom)
	{
		BlockState blockState = super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
		if(!blockState.isAir())
		{
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return blockState;
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos)
	{
		return super.canPlantOnTop(floor, world, pos) || floor.isIn(BlockTags.SAND) || floor.isOf(Blocks.CLAY);
	}

	public void placeAt(WorldAccess world, BlockPos pos, int flags)
	{
		if(getDefaultState().canPlaceAt(world, pos) && world.isAir(pos.up()))
		{
			world.setBlockState(pos, this.getDefaultState().with(HALF, DoubleBlockHalf.LOWER), flags);
			world.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER), flags);
		}
	}

	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid)
	{
		return false;
	}

	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState)
	{
		return false;
	}

	@Override
	public FluidState getFluidState(BlockState state)
	{
		if(state.get(HALF) == DoubleBlockHalf.LOWER) return Fluids.WATER.getStill(false);

		return super.getFluidState(state);
	}
}
