package party.lemons.biomemakeover.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Random;

public class BarrelCactusBlock extends BMBlock
{
	protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 7.0D, 12.0D);
	private final boolean flowered;

	public BarrelCactusBlock(boolean flowered, FabricBlockSettings settings)
	{
		super(settings);
		this.flowered = flowered;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		BlockState checkState = world.getBlockState(pos.down());
		return checkState.isOf(Blocks.SAND) || checkState.isOf(Blocks.RED_SAND) || checkState.isOf(Blocks.CACTUS) || checkState.isOf(BMBlocks.SAGUARO_CACTUS);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(!flowered && world.getBaseLightLevel(pos, 0) >= 9 && random.nextInt(7) == 0)
			world.setBlockState(pos, BMBlocks.BARREL_CACTUS_FLOWERED.getDefaultState());
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom)
	{
		if(!state.canPlaceAt(world, pos))
		{
			world.getBlockTickScheduler().schedule(pos, this, 1);
		}
		return state;
	}

	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(!state.canPlaceAt(world, pos))
		{
			world.breakBlock(pos, true);
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return SHAPE;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
	{
		if(!entity.isSneaking())
		{
			if(entity instanceof ItemEntity && entity.age < 30) return;

			entity.damage(DamageSource.CACTUS, 1.0F);
		}

		super.onEntityCollision(state, world, pos, entity);
	}

	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type)
	{
		return false;
	}
}
