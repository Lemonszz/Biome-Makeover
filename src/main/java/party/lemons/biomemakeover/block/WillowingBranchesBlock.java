package party.lemons.biomemakeover.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class WillowingBranchesBlock extends BMBlock implements Waterloggable
{
	public static final IntProperty STAGE = IntProperty.of("stage", 0, 2);
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	public WillowingBranchesBlock(Settings settings)
	{
		super(settings);

		this.setDefaultState(getStateManager().getDefaultState().with(STAGE, 0).with(WATERLOGGED, false));
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom)
	{
		if(direction == Direction.UP) if(newState.isOf(this) && newState.get(STAGE) != state.get(STAGE) - 1)
		{
			int newStage = newState.get(STAGE);
			if(newStage != 2) return state.with(STAGE, newStage + 1);
		}

		if(!canPlaceAt(state, world, pos))
		{
			world.getBlockTickScheduler().schedule(pos, this, 1);
			return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
		}

		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(random.nextInt(7) == 0)
		{
			if(state.get(STAGE) < 2)
			{
				BlockState placeState = getDefaultState().with(STAGE, state.get(STAGE) + 1);
				if(world.getBlockState(pos.down()).isAir() && canPlaceAt(placeState, world, pos.down()))
				{
					FluidState st = world.getFluidState(pos.down());
					world.setBlockState(pos.down(), placeState.with(WATERLOGGED, st.getFluid() == Fluids.WATER));
				}
			}
		}

		super.randomTick(state, world, pos, random);
	}

	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(!state.canPlaceAt(world, pos))
		{
			world.breakBlock(pos, true);
		}
	}

	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		BlockPos blockPos = pos.up();
		BlockState upState = world.getBlockState(blockPos);
		return (upState.isOf(this) && upState.get(STAGE) < 2) || upState.isIn(BlockTags.LEAVES);
	}

	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos)
	{
		return state.getFluidState().isEmpty();
	}

	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type)
	{
		return type == NavigationType.AIR && !this.collidable || super.canPathfindThrough(state, world, pos, type);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		BlockPos pos = ctx.getBlockPos();
		BlockState upState = ctx.getWorld().getBlockState(pos.up());
		FluidState fluidState = ctx.getWorld().getFluidState(pos);
		boolean waterlog = fluidState.getFluid() == Fluids.WATER;

		if(upState.isOf(this))
		{
			if(upState.get(STAGE) < 2)
			{
				return this.getDefaultState().with(STAGE, upState.get(STAGE) + 1).with(WATERLOGGED, waterlog);
			}
		}
		return super.getPlacementState(ctx).with(WATERLOGGED, waterlog);
	}

	public FluidState getFluidState(BlockState state)
	{
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(STAGE, WATERLOGGED);
		super.appendProperties(builder);
	}
}
