package party.lemons.biomemakeover.block;

import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.BMUtil;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MothBlossomBlock extends IvyShapedBlock
{
	public static final DirectionProperty BLOSSOM_DIRECTION = DirectionProperty.of("blossom", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);

	public MothBlossomBlock(Settings settings)
	{
		super(settings);
		setDefaultState(getDefaultState().with(BLOSSOM_DIRECTION, Direction.DOWN));
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom)
	{
		if(!isValidPlaceFace(world, direction.getOpposite(), posFrom, newState))
		{
			if(direction == state.get(BLOSSOM_DIRECTION))
			{
				world.breakBlock(pos, false);
				return state;
			}

			if(hasDirection(state, direction))
			{
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
	public boolean hasDirection(BlockState blockState, Direction direction)
	{
		if(blockState.get(BLOSSOM_DIRECTION) == direction)
			return true;

		return super.hasDirection(blockState, direction);
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

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
	{
		if(random.nextInt(3) == 0)
		{
			double xx = 0, yy = 0, zz = 0, vx = 0, vy = 0, vz = 0;

			//TODO: could probably use a cleanup but honestly who cares.
			switch(state.get(BLOSSOM_DIRECTION))
			{
				case DOWN:
					xx = (pos.getX() + 0.5F) + RandomUtil.randomDirection(random.nextFloat() / 4F);
					zz = pos.getZ() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
					yy = pos.getY() + 0.1F;

					vx = RandomUtil.randomDirection(random.nextFloat() / 20F);
					vz = RandomUtil.randomDirection(random.nextFloat() / 20F);
					vy = random.nextFloat() / 10F;
					break;
				case UP:
					xx = (pos.getX() + 0.5F) + RandomUtil.randomDirection(random.nextFloat() / 4F);
					zz = pos.getZ() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
					yy = pos.getY() + 0.9F;

					vx = RandomUtil.randomDirection(random.nextFloat() / 20F);
					vz = RandomUtil.randomDirection(random.nextFloat() / 20F);
					vy = (random.nextFloat() / 10F) * -1;
					break;
				case NORTH:
					xx = pos.getX() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
					zz = pos.getZ() + 0.1F;
					yy = pos.getY() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);

					vx = RandomUtil.randomDirection(random.nextFloat() / 20F);
					vz = random.nextFloat() / 10F;
					vy = random.nextFloat() / 20F;
					break;
				case SOUTH:
					xx = pos.getX() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
					zz = pos.getZ() + 0.9F;
					yy = pos.getY() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);

					vx = RandomUtil.randomDirection(random.nextFloat() / 20F);
					vz = (random.nextFloat() / 10F) * -1F;
					vy = random.nextFloat() / 20F;
					break;
				case WEST:
					xx = (pos.getX() + 0.1);
					zz = pos.getZ() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
					yy = pos.getY() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);

					vx = random.nextFloat() / 10F;
					vz = RandomUtil.randomDirection(random.nextFloat() / 20F);
					vy = random.nextFloat() / 20F;
					break;
				case EAST:
					xx = (pos.getX() + 0.9);
					zz = pos.getZ() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
					yy = pos.getY() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);

					vx = (random.nextFloat() / 10F) * -1;
					vz = RandomUtil.randomDirection(random.nextFloat() / 20F);
					vy = random.nextFloat() / 20F;
					break;
			}

			world.addParticle(BMEffects.BLOSSOM, xx, yy, zz, vx, vy, vz);
		}
	}

	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		BlockPos placeOffset = ctx.getBlockPos().offset(ctx.getSide().getOpposite());
		BlockState offsetState = ctx.getWorld().getBlockState(placeOffset);
		if(isValidPlaceFace(ctx.getWorld(), ctx.getSide().getOpposite(), placeOffset, offsetState))
		{
			BlockState state = getDefaultState().with(BLOSSOM_DIRECTION, ctx.getSide().getOpposite());
			for(Direction dir : Direction.values())
			{
				if(dir == ctx.getSide().getOpposite())
					continue;

				placeOffset = ctx.getBlockPos().offset(dir);
				offsetState = ctx.getWorld().getBlockState(placeOffset);
				boolean validFace = isValidPlaceFace(ctx.getWorld(), dir, placeOffset, offsetState);
				state = state.with(getPropertyForDirection(dir), validFace);
			}
			return state;
		}
		return null;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		Direction dir = state.get(BLOSSOM_DIRECTION);
		BlockPos offsetPos = pos.offset(dir);
		BlockState offsetState = world.getBlockState(offsetPos);
		return isValidPlaceFace(world, dir.getOpposite(), offsetPos, offsetState) || super.canPlaceAt(state, world, pos);
	}

	public BlockState getGrowState(WorldAccess world, BlockPos pos)
	{
		BlockState placeState = getDefaultState();
		List<Direction> validDirections = Lists.newArrayList();

		for(Direction dir : Direction.values())
		{
			BlockPos offsetPos = pos.offset(dir);
			BlockState offsetState = world.getBlockState(offsetPos);
			boolean validFace = isValidPlaceFace(world, dir, offsetPos, offsetState);
			placeState = placeState.with(getPropertyForDirection(dir), validFace);

			if(validFace)
				validDirections.add(dir);
		}

		if(validDirections.isEmpty())
			return Blocks.AIR.getDefaultState();

		Direction blossomDir = RandomUtil.choose(validDirections);
		placeState = placeState.with(BLOSSOM_DIRECTION, blossomDir);

		return placeState;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		BlockState st = state.with(getPropertyForDirection(state.get(BLOSSOM_DIRECTION)), true);
		return this.shapes.get(st);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(BLOSSOM_DIRECTION);
	}
}
