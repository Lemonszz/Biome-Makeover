package party.lemons.biomemakeover.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SaguaroCactusBlock extends BMBlock implements Fertilizable
{
	public static final BooleanProperty HORIZONTAL = BooleanProperty.of("horizontal");
	public static final DirectionProperty HORIZONTAL_DIRECTION = HorizontalFacingBlock.FACING;
	public static final BooleanProperty NORTH = BooleanProperty.of("north");
	public static final BooleanProperty SOUTH = BooleanProperty.of("south");
	public static final BooleanProperty EAST = BooleanProperty.of("east");
	public static final BooleanProperty WEST = BooleanProperty.of("west");

	public SaguaroCactusBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(getStateManager().getDefaultState().with(HORIZONTAL, false).with(HORIZONTAL_DIRECTION, Direction.NORTH).with(NORTH, false).with(SOUTH, false).with(EAST, false).with(WEST, false));
	}


	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		BlockState st = getDefaultState();
		if(ctx.getSide().getHorizontal() >= 0)
			st = st.with(HORIZONTAL, true).with(HORIZONTAL_DIRECTION, ctx.getSide().getOpposite()).with(FACING_PROPERTIES.get(ctx.getSide().getOpposite()), true);

		return st;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom)
	{
		if(!canPlaceAt(state, world, pos))
		{
			world.getBlockTickScheduler().schedule(pos, this, 1);
			return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
		}

		if(direction.getAxis().isVertical())
			return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);

		if(newState.getBlock() == this)
		{
			if(newState.get(HORIZONTAL) && newState.get(FACING_PROPERTIES.get(direction.getOpposite())))
				return state.with(FACING_PROPERTIES.get(direction), true);
		}else
		{
			return state.with(FACING_PROPERTIES.get(direction), false);
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(!state.canPlaceAt(world, pos))
		{
			world.breakBlock(pos, true);
		}
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		if(state.get(HORIZONTAL))
		{
			Direction offset = state.get(HORIZONTAL_DIRECTION);
			if(!world.getBlockState(pos.offset(offset)).isOf(this)) return false;
		}else
		{
			BlockState checkState = world.getBlockState(pos.down());
			return (checkState.isOf(this) || checkState.isOf(Blocks.SAND) || checkState.isOf(Blocks.RED_SAND)) && !world.getBlockState(pos.up()).getMaterial().isLiquid();
		}

		return super.canPlaceAt(state, world, pos);
	}

	public boolean isGrowBlock(BlockState state)
	{
		return state.isOf(Blocks.SAND) || state.isOf(Blocks.RED_SAND);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return getShape(state);
	}

	public VoxelShape getShape(BlockState state)
	{
		return shapes.get(state);
	}

	private final HashMap<BlockState, VoxelShape> shapes = Util.make(Maps.newHashMap(), m->getStateManager().getStates().forEach(st->m.put(st, getShapeForState(st))));

	private VoxelShape getShapeForState(BlockState state)
	{
		double size = 4;
		VoxelShape base;

		if(state.get(HORIZONTAL)) base = Block.createCuboidShape(size, size * 2, size, 16 - size, 15.98, 16 - size);
		else base = Block.createCuboidShape(size, 0, size, 16 - size, 15.98, 16 - size);

		List<VoxelShape> connections = Lists.newArrayList();
		for(Direction dir : Direction.values())
		{
			if(dir.getHorizontal() < 0) continue;

			if(state.get(FACING_PROPERTIES.get(dir)))
			{
				double x = dir == Direction.WEST ? 0 : dir == Direction.EAST ? 16D : size;
				double z = dir == Direction.NORTH ? 0 : dir == Direction.SOUTH ? 16D : size;

				VoxelShape sh = Block.createCuboidShape(x, 8, z, 16 - size, 16, 16 - size);
				connections.add(sh);
			}
		}

		return VoxelShapes.union(base, connections.toArray(new VoxelShape[]{}));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(EAST, WEST, NORTH, SOUTH, HORIZONTAL, HORIZONTAL_DIRECTION);
	}

	public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = Util.make(Maps.newEnumMap(Direction.class), (enumMap)->
	{
		enumMap.put(Direction.NORTH, NORTH);
		enumMap.put(Direction.EAST, EAST);
		enumMap.put(Direction.SOUTH, SOUTH);
		enumMap.put(Direction.WEST, WEST);
	});

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient)
	{
		return state.equals(getDefaultState()) && isGrowBlock(world.getBlockState(pos.down())) && world.getBlockState(pos.up()).isAir();
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state)
	{
		return (double) world.random.nextFloat() < 0.45D;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state)
	{
		BMWorldGen.SAGUARO_CACTUS_FEATURE.generateCactus(world, random.nextBoolean(), pos, random, false);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(isFertilizable(world, pos, state, world.isClient) && random.nextInt(10) == 0)
			grow(world, random, pos, state);
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
	{
		entity.damage(DamageSource.CACTUS, 1.0F);
	}

	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type)
	{
		return false;
	}

}
