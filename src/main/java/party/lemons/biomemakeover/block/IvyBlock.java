package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Random;

public class IvyBlock extends IvyShapedBlock
{
	public IvyBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		Pair<Integer, Integer> nearbyCount = getNearbyIvyCount(world, pos);
		if(nearbyCount.getLeft() >= 6 && nearbyCount.getRight() >= 1) return;

		Direction checkDirection = getRandomStateSide(state, random);
		if(checkDirection == null) return;

		Direction direction = Direction.random(random);
		while(direction == checkDirection.getOpposite()) direction = Direction.random(random);

		BlockPos offsetPos = pos.offset(direction);
		BlockState offsetState = world.getBlockState(offsetPos);
		BooleanProperty dirProperty = getPropertyForDirection(direction);

		if(!state.get(dirProperty) && isValidPlaceFace(world, direction, offsetPos, offsetState))
		{
			if(hasAdjacentSide(direction, state))
				world.setBlockState(pos, state.with(dirProperty, true));
		}else if(hasAdjacentSide(direction, state) && canReplace(offsetState))
		{
			BlockPos hitPos = offsetPos.offset(checkDirection);
			BlockState hitState = world.getBlockState(hitPos);
			if(isValidPlaceFace(world, direction, hitPos, hitState))
			{
				placeOrAddTo(world, offsetPos, checkDirection);
				return;
			}

			BlockPos creepPos = pos.offset(direction).offset(checkDirection);
			BlockState creepState = world.getBlockState(creepPos);
			if(canReplace(creepState))
			{
				hitPos = pos.offset(checkDirection);
				hitState = world.getBlockState(hitPos);
				if(isValidPlaceFace(world, direction, hitPos, hitState))
				{
					placeOrAddTo(world, creepPos, direction.getOpposite());
				}
			}
		}
	}

	private Pair<Integer, Integer> getNearbyIvyCount(World world, BlockPos pos)
	{
		int distance = 3;
		int count = -1; //-1 because it counts itself and this was easier lol
		for(int x = -distance; x < distance; x++)
		{
			for(int z = -distance; z < distance; z++)
			{
				for(int y = -distance; y < distance; y++)
				{
					if(world.getBlockState(pos.add(x, y, z)).isOf(this)) count++;
				}
			}
		}
		int adjacent = 0;
		for(Direction dir : Direction.values())
			if(world.getBlockState(pos.offset(dir)).isOf(this)) adjacent++;


		return new Pair<>(count, adjacent);
	}

	private boolean canReplace(BlockState state)
	{
		return !state.isIn(BMBlocks.IVY_TAG) && (state.isAir() || state.isOf(this) || (state.getMaterial().isReplaceable() && !state.getMaterial().isLiquid()));
	}


	private void placeOrAddTo(World world, BlockPos pos, Direction direction)
	{
		BlockState state = world.getBlockState(pos);

		if(state.isOf(this))
		{
			world.setBlockState(pos, state.with(getPropertyForDirection(direction), true));
		}
		else if(canReplace(state))
		{
			world.setBlockState(pos, getDefaultState().with(getPropertyForDirection(direction), true));
			if(doesScheduleAfterSet())
				world.getBlockTickScheduler().schedule(pos, this, getScheduleDelay());
		}
	}

	protected boolean doesScheduleAfterSet()
	{
		return false;
	}

	protected int getScheduleDelay()
	{
		return 4;
	}

	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext context)
	{
		return true;
	}

}
