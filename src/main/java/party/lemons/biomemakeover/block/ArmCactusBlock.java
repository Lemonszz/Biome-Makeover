package party.lemons.biomemakeover.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmCactusBlock extends BMBlock
{
	public static BooleanProperty HORIZONTAL = BooleanProperty.of("horizontal");
	public static BooleanProperty NORTH = BooleanProperty.of("north");
	public static BooleanProperty SOUTH = BooleanProperty.of("south");
	public static BooleanProperty EAST = BooleanProperty.of("east");
	public static BooleanProperty WEST = BooleanProperty.of("west");

	public ArmCactusBlock(Settings settings)
	{
		super(settings);

		this.setDefaultState(getStateManager().getDefaultState().with(HORIZONTAL, false).with(NORTH, false).with(SOUTH, false).with(EAST, false).with(WEST, false));
	}


	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		BlockState st = getDefaultState();
		if(ctx.getSide().getHorizontal() >= 0)
			st = st.with(HORIZONTAL, true).with(FACING_PROPERTIES.get(ctx.getSide().getOpposite()), true);

		return st;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom)
	{
		if(direction.getAxis() == Direction.Axis.Y)
			return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);

		if(newState.getBlock() == this)
		{
			if(newState.get(HORIZONTAL) && newState.get(FACING_PROPERTIES.get(direction.getOpposite())))
				return state.with(FACING_PROPERTIES.get(direction), true);
		}
		else if(newState.isAir())
		{

			if(state.get(FACING_PROPERTIES.get(direction)))
			{
				if(state.get(HORIZONTAL))
				{
					//break??
				}
				else
				{
					return state.with(FACING_PROPERTIES.get(direction), false);
				}
			}
		}

		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
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

		if(state.get(HORIZONTAL))
			base = Block.createCuboidShape(size, size, size, 16 - size, 16, 16 - size);
		else
			base = Block.createCuboidShape(size, 0, size, 16 - size, 16, 16 - size);

		List<VoxelShape> connections = Lists.newArrayList();
		for(Direction dir : Direction.values())
		{
			if(dir.getHorizontal() < 0)
				continue;

			if(state.get(FACING_PROPERTIES.get(dir)))
			{
				double x= dir == Direction.WEST ? 0 : dir == Direction.EAST ? 16D : size;
				double z= dir == Direction.NORTH ? 0 : dir == Direction.SOUTH ? 16D : size;

				VoxelShape sh = Block.createCuboidShape(x, 8, z, 16 - size, 16, 16 - size);
				connections.add(sh);
			}
		}

		return VoxelShapes.union(base, connections.toArray(new VoxelShape[]{}));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(EAST, WEST, NORTH, SOUTH, HORIZONTAL);
	}

	public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = Util.make(Maps.newEnumMap(Direction.class), (enumMap) -> {
		enumMap.put(Direction.NORTH, NORTH);
		enumMap.put(Direction.EAST, EAST);
		enumMap.put(Direction.SOUTH, SOUTH);
		enumMap.put(Direction.WEST, WEST);
	});
}
