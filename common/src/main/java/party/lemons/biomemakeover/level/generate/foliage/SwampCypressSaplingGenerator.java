package party.lemons.biomemakeover.level.generate.foliage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.Random;

public class SwampCypressSaplingGenerator extends AbstractTreeGrower {
    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean bl) {
        return BMWorldGen.Swamp.SWAMP_CYPRESS;
    }

    public boolean growTree(ServerLevel serverLevel, ChunkGenerator chunkGenerator, BlockPos blockPos, BlockState blockState, Random random) {
        Holder<? extends ConfiguredFeature<?, ?>> configuredFeature = this.getConfiguredFeature(random, this.hasFlowers(serverLevel, blockPos));
        if (configuredFeature == null) {
            return false;
        }
        serverLevel.setBlock(blockPos, Blocks.WATER.defaultBlockState(), 4);
        if (configuredFeature.value().place(serverLevel, chunkGenerator, random, blockPos)) {
            return true;
        }
        serverLevel.setBlock(blockPos, blockState, 4);
        return false;
    }

    private boolean hasFlowers(LevelAccessor levelAccessor, BlockPos blockPos) {
        for (BlockPos blockPos2 : BlockPos.MutableBlockPos.betweenClosed(blockPos.below().north(2).west(2), blockPos.above().south(2).east(2))) {
            if (!levelAccessor.getBlockState(blockPos2).is(BlockTags.FLOWERS)) continue;
            return true;
        }
        return false;
    }
}