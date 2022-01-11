package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LayerLightEngine;

import java.util.Random;
import java.util.function.Supplier;

public class BMSpreadableBlock extends BMBlock
{
    private final Supplier<Block> dirtBlock;

    public BMSpreadableBlock(BlockBehaviour.Properties properties, Supplier<Block> dirtBlock)
    {
        super(properties);
        this.dirtBlock = dirtBlock;
    }

    private static boolean canBeGrass(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockPos blockPos2 = blockPos.above();
        BlockState blockState2 = levelReader.getBlockState(blockPos2);

        int i = LayerLightEngine.getLightBlockInto(levelReader, blockState, blockPos, blockState2, blockPos2, Direction.UP, blockState2.getLightBlock(levelReader, blockPos2));
        return i < levelReader.getMaxLightLevel();
    }

    private static boolean canPropagate(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockPos blockPos2 = blockPos.above();
        return canBeGrass(blockState, levelReader, blockPos);
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random)
    {
        if (!canBeGrass(blockState, serverLevel, blockPos))
        {
            serverLevel.setBlockAndUpdate(blockPos, dirtBlock.get().defaultBlockState());
            return;
        }
        if (serverLevel.getMaxLocalRawBrightness(blockPos.above()) >= 9)
        {
            BlockState blockState2 = this.defaultBlockState();
            for (int i = 0; i < 4; ++i) {
                BlockPos blockPos2 = blockPos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                if (!serverLevel.getBlockState(blockPos2).is(dirtBlock.get()) || !canPropagate(blockState2, serverLevel, blockPos2)) continue;
                serverLevel.setBlockAndUpdate(blockPos2, blockState2);
            }
        }
    }
}
