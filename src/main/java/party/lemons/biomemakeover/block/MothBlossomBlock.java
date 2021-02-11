package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.BMUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MothBlossomBlock extends IvyShapedBlock
{
	public MothBlossomBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom)
	{
		boolean isValidFace = isValidPlaceFace(world, direction, posFrom, newState);
		if(!isValidFace)
		{
			if(IvyShapedBlock.hasDirection(state, direction))
			{
				if(direction == Direction.DOWN)
					world.breakBlock(pos, false);
				return getStateWithoutDirection(state, getPropertyForDirection(direction));
			}
		}
		else
		{
			return state.with(getPropertyForDirection(direction), true);
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		for(int i = 0; i < 3; i++)
		{
			if(attemptSpread(world, pos.add(0, yOffset[i], 0), random))
				return;
		}
	}
	private final int[] yOffset = {0, 1, -1};

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

	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		return getPlaceState(ctx.getWorld(), ctx.getBlockPos());
	}

	public BlockState getPlaceState(WorldAccess world, BlockPos pos)
	{
		BlockState placeState = getDefaultState();
		for(Direction dir : Direction.values())
		{
			BlockPos offsetPos = pos.offset(dir);
			BlockState offsetState = world.getBlockState(offsetPos);
			boolean validFace = isValidPlaceFace(world, dir, offsetPos, offsetState);
			placeState = placeState.with(getPropertyForDirection(dir), validFace);

			if(dir == Direction.DOWN && !validFace)
				return null;
		}
		return placeState;
	}
}
