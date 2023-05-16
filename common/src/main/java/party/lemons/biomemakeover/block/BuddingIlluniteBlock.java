package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import party.lemons.biomemakeover.init.BMBlocks;

public class BuddingIlluniteBlock extends IlluniteBlock
{
	public BuddingIlluniteBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random)
	{
		if (random.nextInt(5) == 0) {
			Direction direction = Direction.values()[random.nextInt(Direction.values().length)];
			BlockPos offsetPos = blockPos.relative(direction);
			BlockState currentState = serverLevel.getBlockState(offsetPos);
			Block block = null;

			if (BuddingAmethystBlock.canClusterGrowAtState(currentState))
			{
				block = BMBlocks.SMALL_ILLUNITE_BUD.get();
			}
			else if (currentState.is(BMBlocks.SMALL_ILLUNITE_BUD.get()) && currentState.getValue(AmethystClusterBlock.FACING) == direction) {
				block = BMBlocks.MEDIUM_ILLUNITE_BUD.get();
			}
			else if (currentState.is(BMBlocks.MEDIUM_ILLUNITE_BUD.get()) && currentState.getValue(AmethystClusterBlock.FACING) == direction) {
				block = BMBlocks.LARGE_ILLUNITE_BUD.get();
			}
			else if (currentState.is(BMBlocks.LARGE_ILLUNITE_BUD.get()) && currentState.getValue(AmethystClusterBlock.FACING) == direction) {
				block = BMBlocks.ILLUNITE_CLUSTER.get();
			}

			if (block != null) {
				BlockState finalState = block.defaultBlockState()
						.setValue(AmethystClusterBlock.FACING, direction)
						.setValue(AmethystClusterBlock.WATERLOGGED, currentState.getFluidState().getType() == Fluids.WATER);
				serverLevel.setBlockAndUpdate(offsetPos, finalState);
			}
		}
	}
}
