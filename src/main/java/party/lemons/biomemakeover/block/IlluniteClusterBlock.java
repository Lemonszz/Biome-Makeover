package party.lemons.biomemakeover.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import party.lemons.biomemakeover.util.BlockWithItem;

import java.util.Locale;
import java.util.Random;

import static net.minecraft.state.property.Properties.WATERLOGGED;

public class IlluniteClusterBlock extends FacingBlock implements BlockWithItem, Waterloggable
{
	protected static final VoxelShape Y_SHAPE = Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
	protected static final VoxelShape Z_SHAPE = Block.createCuboidShape(3.0D, 3.0D, 0.0D, 13.0D, 13.0D, 16.0D);
	protected static final VoxelShape X_SHAPE = Block.createCuboidShape(0.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D);

	public static final EnumProperty<Type> TYPE = EnumProperty.of("type", Type.class);

	public IlluniteClusterBlock(Settings settings)
	{
		super(settings.ticksRandomly().luminance(value->
		{
			switch(value.get(TYPE))
			{
				case DAY:
					return 2;
				case NIGHT:
					return 15;
				case UNKNOWN:
					return 2;
			}
			return 0;
		}));
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.UP).with(TYPE, Type.DAY).with(Properties.WATERLOGGED, false));
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		Type expectedType = getType(world);
		if(state.get(TYPE) != expectedType)
		{
			world.setBlockState(pos, state.with(TYPE, expectedType));
		}
		world.getBlockTickScheduler().schedule(new BlockPos(pos), this, 20 + random.nextInt(150));
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
	{
		scheduleUpdates(world, pos, random);
		super.randomDisplayTick(state, world, pos, random);
	}

	@Override
	public void prepare(BlockState state, WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth)
	{
		super.prepare(state, world, pos, flags, maxUpdateDepth);
		scheduleUpdates(world, pos, world.getRandom());
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		Direction dir = state.get(FACING);
		BlockPos blockPos = pos.offset(dir.getOpposite());

		return Block.sideCoversSmallSquare(world, blockPos, dir);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom)
	{
		if(!canPlaceAt(state, world, pos))
			return Blocks.AIR.getDefaultState();

		scheduleUpdates(world, pos, world.getRandom());
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	public BlockState rotate(BlockState state, BlockRotation rotation)
	{
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	public BlockState mirror(BlockState state, BlockMirror mirror)
	{
		return state.with(FACING, mirror.apply(state.get(FACING)));
	}

	public FluidState getFluidState(BlockState state)
	{
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		Direction direction = ctx.getSide();

		return this.getDefaultState().with(FACING, direction).with(TYPE, getType(ctx.getWorld())).with(Properties.WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}


	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		switch(state.get(FACING).getAxis()) {
			case X:
			default:
				return X_SHAPE;
			case Z:
				return Z_SHAPE;
			case Y:
				return Y_SHAPE;
		}
	}

	private Type getType(World world)
	{
		if(world.getDimension().hasFixedTime())
		{
			return Type.UNKNOWN;
		}else if(world.isNight())
		{
			return Type.NIGHT;
		}else
		{
			return Type.DAY;
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(TYPE);
		builder.add(FACING);
		builder.add(Properties.WATERLOGGED);
	}

	public void scheduleUpdates(WorldAccess world, BlockPos pos, Random random)
	{
		if(!world.getBlockTickScheduler().isScheduled(pos, this))
			world.getBlockTickScheduler().schedule(new BlockPos(pos), this, 20 + random.nextInt(150));
	}

	public enum Type implements StringIdentifiable
	{
		DAY, NIGHT, UNKNOWN;

		@Override
		public String asString()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
}
