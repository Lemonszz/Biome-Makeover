package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.BMUtil;

import java.util.Random;

public class MothBlossomBlock extends BMBlock
{
	public MothBlossomBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		entity.slowMovement(state, new Vec3d(0.85F, 0.85F, 0.85D));
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(!attemptSpread(world, pos, random))
			if(!attemptSpread(world, pos.up(), random))
				attemptSpread(world, pos.down(), random);
	}

	private boolean attemptSpread(World world, BlockPos pos, Random random)
	{
		for(Direction direction : BMUtil.randomOrderedHorizontals())
		{
			BlockPos offsetPos = pos.offset(direction);
			BlockState placePosState = world.getBlockState(offsetPos);
			if((placePosState.isAir() || (placePosState.getMaterial().isReplaceable() && !placePosState.isOf(BMBlocks.ITCHING_IVY))) && IvyBlock.isValidPlaceFace(world, Direction.DOWN, pos.offset(direction), world.getBlockState(pos.offset(direction).down())))
			{
				world.setBlockState(offsetPos, BMBlocks.ITCHING_IVY.getDefaultState().with(IvyBlock.getPropertyForDirection(Direction.DOWN), true));
				world.getBlockTickScheduler().schedule(offsetPos, BMBlocks.ITCHING_IVY, 4);
				if(random.nextBoolean())
					return true;
			}
		}
		return false;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		scheduledTick(state, world, pos, random);
	}
}
