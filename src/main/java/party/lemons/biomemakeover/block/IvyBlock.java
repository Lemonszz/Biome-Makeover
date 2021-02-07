package party.lemons.biomemakeover.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.util.BMUtil;
import party.lemons.biomemakeover.util.HorizontalDirection;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IvyBlock extends BMBlock
{
	private static final VoxelShape UP_SHAPE = Block.createCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
	private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
	private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
	private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
	private static final Map<Direction, BooleanProperty> FACING_PROPERTIES = ConnectingBlock.FACING_PROPERTIES;
	private static final Map<Direction, VoxelShape> DIRECTION_TO_SHAPE = Util.make(Maps.newEnumMap(Direction.class), (enumMap)->
	{
		enumMap.put(Direction.NORTH, SOUTH_SHAPE);
		enumMap.put(Direction.EAST, WEST_SHAPE);
		enumMap.put(Direction.SOUTH, NORTH_SHAPE);
		enumMap.put(Direction.WEST, EAST_SHAPE);
		enumMap.put(Direction.UP, UP_SHAPE);
		enumMap.put(Direction.DOWN, DOWN_SHAPE);
	});
	private static final Direction[] DIRECTIONS = Direction.values();
	private final ImmutableMap<BlockState, VoxelShape> shapes;

	public IvyBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(createDefaultState(this.stateManager));
		this.shapes = collectVoxelShapes(this.stateManager);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(getNearbyIvyCount(world, pos) >= 6) return;

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

	private int getNearbyIvyCount(World world, BlockPos pos)
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
		return count;
	}

	private boolean canReplace(BlockState state)
	{
		return state.isAir() || state.isOf(this) || (state.getMaterial().isReplaceable() && !state.getMaterial().isLiquid());
	}

	private Direction getRandomStateSide(BlockState state, Random random)
	{
		List<Direction> dirs = Arrays.stream(Direction.values()).filter(d->state.get(getPropertyForDirection(d))).collect(Collectors.toList());
		if(dirs.isEmpty()) return null;

		return dirs.get(random.nextInt(dirs.size()));
	}

	private void placeOrAddTo(World world, BlockPos pos, Direction direction)
	{
		BlockState state = world.getBlockState(pos);

		if(state.isOf(this))
		{
			world.setBlockState(pos, state.with(getPropertyForDirection(direction), true));
		}else if(canReplace(state))
		{
			world.setBlockState(pos, getDefaultState().with(getPropertyForDirection(direction), true));
		}
	}

	private Direction getRandomHorizontal(BlockState state, Random random)
	{
		List<Direction> validMovements = Lists.newArrayList();
		for(Direction direction : BMUtil.HORIZONTALS)
		{
			if(state.get(getPropertyForDirection(direction))) validMovements.add(direction);
		}

		if(validMovements.isEmpty()) return null;

		return validMovements.get(random.nextInt(validMovements.size()));
	}

	private static ImmutableMap<BlockState, VoxelShape> collectVoxelShapes(StateManager<Block, BlockState> stateManager)
	{
		Map<BlockState, VoxelShape> map = stateManager.getStates().stream().collect(Collectors.toMap(Function.identity(), IvyBlock::mergeVoxelShapes));
		return ImmutableMap.copyOf(map);
	}

	private static VoxelShape mergeVoxelShapes(BlockState blockState)
	{
		VoxelShape shape = VoxelShapes.empty();
		for(Direction direction : DIRECTIONS)
		{
			if(isValidDirectionForState(blockState, direction))
			{
				shape = VoxelShapes.union(shape, DIRECTION_TO_SHAPE.get(direction));
			}
		}
		return shape;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		for(Direction direction : DIRECTIONS)
		{
			builder.add(getPropertyForDirection(direction));
		}
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom)
	{
		return hasDirection(state, direction) && !isValidPlaceFace(world, direction, posFrom, newState) ? getStateWithoutDirection(state, getPropertyForDirection(direction)) : state;
	}

	public boolean hasAdjacentSide(Direction direction, BlockState state)
	{
		for(Direction d : DIRECTIONS)
		{
			if(BMUtil.isAdjacentDirection(d, direction) && hasDirection(state, d)) return true;
		}

		return false;
	}

	private BlockState getStateWithoutDirection(BlockState state, BooleanProperty directionProperty)
	{
		return state.with(directionProperty, false);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return this.shapes.get(state);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		boolean canPlace = false;

		for(Direction direction : DIRECTIONS)
		{
			if(hasDirection(state, direction))
			{
				BlockPos blockPos = pos.offset(direction);
				if(!isValidPlaceFace(world, direction, blockPos, world.getBlockState(blockPos)))
				{
					return true;
				}
				canPlace = true;
			}
		}
		return canPlace;
	}

	public static VoxelShape getDownShape()
	{
		return DOWN_SHAPE;
	}

	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext context)
	{
		return true;
	}

	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		World world = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		BlockState blockState = world.getBlockState(blockPos);
		return Arrays.stream(ctx.getPlacementDirections()).map((direction)->
		{
			return this.getPlacementState(blockState, world, blockPos, direction);
		}).filter(Objects::nonNull).findFirst().orElse(null);
	}

	@Nullable
	public BlockState getPlacementState(BlockState blockState, WorldAccess worldAccess, BlockPos blockPos, Direction direction)
	{
		BlockState placeState;
		if(blockState.isOf(this))
		{
			if(hasDirection(blockState, direction))
			{
				return null;
			}
			placeState = blockState;
		}else
		{
			placeState = this.getDefaultState();
		}
		BlockPos placeSide = blockPos.offset(direction);
		return isValidPlaceFace(worldAccess, direction, placeSide, worldAccess.getBlockState(placeSide)) ? placeState.with(getPropertyForDirection(direction), true) : null;
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation)
	{
		return this.getStateWithDirections(state, rotation::rotate);
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror)
	{
		return this.getStateWithDirections(state, mirror::apply);
	}

	private BlockState getStateWithDirections(BlockState blockState, Function<Direction, Direction> function)
	{
		BlockState state = blockState;
		for(Direction direction : DIRECTIONS)
		{
			state = state.with(getPropertyForDirection(function.apply(direction)), blockState.get(getPropertyForDirection(direction)));
		}
		return state;
	}

	private static boolean hasDirection(BlockState blockState, Direction direction)
	{
		BooleanProperty booleanProperty = getPropertyForDirection(direction);
		return blockState.get(booleanProperty);
	}

	private static boolean isValidPlaceFace(BlockView blockView, Direction direction, BlockPos blockPos, BlockState blockState)
	{
		return Block.isFaceFullSquare(blockState.getCollisionShape(blockView, blockPos), direction.getOpposite());
	}

	private static boolean isValidDirectionForState(BlockState blockState, Direction direction)
	{
		BooleanProperty booleanProperty = getPropertyForDirection(direction);
		return blockState.contains(booleanProperty) && blockState.get(booleanProperty);
	}

	public static BooleanProperty getPropertyForDirection(Direction direction)
	{
		return FACING_PROPERTIES.get(direction);
	}

	private static BlockState createDefaultState(StateManager<Block, BlockState> stateManager)
	{
		BlockState blockState = stateManager.getDefaultState();

		for(BooleanProperty property : FACING_PROPERTIES.values())
		{
			if(blockState.contains(property))
			{
				blockState = blockState.with(property, false);
			}
		}
		return blockState;
	}
}
