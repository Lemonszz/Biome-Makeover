package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Random;

public class ItchingIvyBlock extends IvyBlock implements Fertilizable
{
	public ItchingIvyBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	protected boolean doesScheduleAfterSet()
	{
		return true;
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient)
	{
		return state.get(IvyShapedBlock.getPropertyForDirection(Direction.DOWN));
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state)
	{
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state)
	{
		world.setBlockState(pos, BMBlocks.MOTH_BLOSSOM.getGrowState(world, pos));
	}
}
