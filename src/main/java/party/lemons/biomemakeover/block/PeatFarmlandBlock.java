package party.lemons.biomemakeover.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Iterator;
import java.util.Random;

import static net.minecraft.state.property.Properties.MOISTURE;

public class PeatFarmlandBlock extends BMBlock
{
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public PeatFarmlandBlock(AbstractBlock.Settings settings)
	{
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(FarmlandBlock.MOISTURE, 0));
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom)
	{
		if(direction == Direction.UP && !state.canPlaceAt(world, pos))
		{
			world.getBlockTickScheduler().schedule(pos, this, 1);
		}

		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		BlockState blockState = world.getBlockState(pos.up());
		return !blockState.getMaterial().isSolid() || blockState.getBlock() instanceof FenceGateBlock || blockState.getBlock() instanceof PistonExtensionBlock;
	}

	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		return !this.getDefaultState().canPlaceAt(ctx.getWorld(), ctx.getBlockPos()) ? BMBlocks.PEAT.getDefaultState() : super.getPlacementState(ctx);
	}

	public boolean hasSidedTransparency(BlockState state)
	{
		return true;
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return SHAPE;
	}

	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(!state.canPlaceAt(world, pos))
		{
			setToDirt(state, world, pos);
		}
	}

	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		int i = state.get(MOISTURE);
		if(!isWaterNearby(world, pos) && !world.hasRain(pos.up()))
		{
			if(i > 0)
			{
				world.setBlockState(pos, state.with(MOISTURE, i - 1), 2);
			}else if(!hasCrop(world, pos))
			{
				setToDirt(state, world, pos);
			}
		}else if(i < 7)
		{
			world.setBlockState(pos, state.with(MOISTURE, 7), 2);
		}

		world.getBlockState(pos.up()).randomTick(world, pos.up(), random);
	}

	public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance)
	{
		if(!world.isClient && world.random.nextFloat() < distance - 0.5F && entity instanceof LivingEntity && (entity instanceof PlayerEntity || world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) && entity.getWidth() * entity.getWidth() * entity.getHeight() > 0.512F)
		{
			setToDirt(world.getBlockState(pos), world, pos);
		}

		super.onLandedUpon(world, pos, entity, distance);
	}

	public static void setToDirt(BlockState state, World world, BlockPos pos)
	{
		world.setBlockState(pos, pushEntitiesUpBeforeBlockChange(state, BMBlocks.PEAT.getDefaultState(), world, pos));
	}

	private static boolean hasCrop(BlockView world, BlockPos pos)
	{
		Block block = world.getBlockState(pos.up()).getBlock();
		return block instanceof CropBlock || block instanceof StemBlock || block instanceof AttachedStemBlock;
	}


	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type)
	{
		return false;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(FarmlandBlock.MOISTURE);
	}

	private static boolean isWaterNearby(WorldView world, BlockPos pos)
	{
		Iterator var2 = BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 1, 4)).iterator();

		BlockPos blockPos;
		do
		{
			if(!var2.hasNext())
			{
				return false;
			}

			blockPos = (BlockPos) var2.next();
		}while(!world.getFluidState(blockPos).isIn(FluidTags.WATER));

		return true;
	}

	@Override
	public boolean is(Block block)
	{
		return super.is(block) || block == Blocks.FARMLAND;
	}
}
