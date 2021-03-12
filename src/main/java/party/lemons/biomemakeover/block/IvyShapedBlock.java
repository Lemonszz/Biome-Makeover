package party.lemons.biomemakeover.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IvyShapedBlock extends BMBlock
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
	protected final ImmutableMap<BlockState, VoxelShape> shapes;

	public IvyShapedBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(createDefaultState(this.stateManager));
		this.shapes = collectVoxelShapes(this.stateManager);
	}

	public static BooleanProperty getPropertyForDirection(Direction direction)
	{
		return FACING_PROPERTIES.get(direction);
	}

	protected static BlockState createDefaultState(StateManager<Block, BlockState> stateManager)
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

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom)
	{
		BlockState returnState;
		if(hasDirection(state, direction) && !isValidPlaceFace(world, direction, posFrom, newState))
		{
			returnState = getStateWithoutDirection(state, getPropertyForDirection(direction));
			if(!world.isClient())
			{
				world.syncWorldEvent(2001, pos, Block.getRawIdFromState(getDefaultState().with(getPropertyForDirection(direction), true)));
			}
		}
		else
		{
			returnState = state;
		}

		if(!hasAnySide(returnState))
		{
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
			return state;
		}

		return returnState;
	}

	private boolean hasAnySide(BlockState state)
	{
		for(Direction d : Direction.values())
		{
			if(hasDirection(state, d))
				return true;
		}
		return false;
	}

	public boolean hasAdjacentSide(Direction direction, BlockState state)
	{
		for(Direction d : Direction.values())
		{
			if(BMUtil.isAdjacentDirection(d, direction) && hasDirection(state, d)) return true;
		}

		return false;
	}

	public BlockState getStateWithoutDirection(BlockState state, BooleanProperty directionProperty)
	{
		return state.with(directionProperty, false);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return this.shapes.get(state);
	}


	public static VoxelShape getDownShape()
	{
		return DOWN_SHAPE;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		boolean canPlace = false;

		for(Direction direction : Direction.values())
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

	protected static ImmutableMap<BlockState, VoxelShape> collectVoxelShapes(StateManager<Block, BlockState> stateManager)
	{
		Map<BlockState, VoxelShape> map = stateManager.getStates().stream().collect(Collectors.toMap(Function.identity(), IvyShapedBlock::mergeVoxelShapes));
		return ImmutableMap.copyOf(map);
	}

	protected Direction getRandomStateSide(BlockState state, Random random)
	{
		List<Direction> dirs = Arrays.stream(Direction.values()).filter(d->state.get(getPropertyForDirection(d))).collect(Collectors.toList());
		if(dirs.isEmpty()) return null;

		return dirs.get(random.nextInt(dirs.size()));
	}

	protected static VoxelShape mergeVoxelShapes(BlockState blockState)
	{
		VoxelShape shape = VoxelShapes.empty();
		for(Direction direction : Direction.values())
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
		for(Direction direction : Direction.values())
		{
			builder.add(getPropertyForDirection(direction));
		}
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
		for(Direction direction : Direction.values())
		{
			state = state.with(getPropertyForDirection(function.apply(direction)), blockState.get(getPropertyForDirection(direction)));
		}
		return state;
	}

	public static boolean hasDirection(BlockState blockState, Direction direction)
	{
		BooleanProperty booleanProperty = getPropertyForDirection(direction);
		return blockState.get(booleanProperty);
	}

	public static boolean isValidPlaceFace(BlockView blockView, Direction direction, BlockPos blockPos, BlockState blockState)
	{
		return Block.isFaceFullSquare(blockState.getCollisionShape(blockView, blockPos), direction.getOpposite());
	}

	private static boolean isValidDirectionForState(BlockState blockState, Direction direction)
	{
		BooleanProperty booleanProperty = getPropertyForDirection(direction);
		return blockState.contains(booleanProperty) && blockState.get(booleanProperty);
	}

	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		World world = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		BlockState blockState = world.getBlockState(blockPos);
		return Arrays.stream(ctx.getPlacementDirections())
				.map((direction)->this.getPlacementState(blockState, world, blockPos, direction))
				.filter(Objects::nonNull)
				.findFirst()
				.orElse(null);
	}
}
