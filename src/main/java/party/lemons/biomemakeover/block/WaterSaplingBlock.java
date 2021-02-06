package party.lemons.biomemakeover.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

import static net.minecraft.state.property.Properties.WATERLOGGED;

public class WaterSaplingBlock extends BMSaplingBlock implements Waterloggable
{
	private final int maxDepth;

	public WaterSaplingBlock(SaplingGenerator generator, int maxDepth, Settings settings)
	{
		super(generator, settings);
		this.maxDepth = maxDepth;
		setDefaultState(this.getStateManager().getDefaultState().with(WATERLOGGED, false));
	}

	public void generate(ServerWorld serverWorld, BlockPos blockPos, BlockState blockState, Random random)
	{
		if(blockState.get(WATERLOGGED))
		{
			if(serverWorld.getFluidState(blockPos.up(maxDepth)).getFluid() == Fluids.WATER) return;
		}

		super.generate(serverWorld, blockPos, blockState, random);
	}


	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());

		return getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}

	public FluidState getFluidState(BlockState state)
	{
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(WATERLOGGED);
	}
}
