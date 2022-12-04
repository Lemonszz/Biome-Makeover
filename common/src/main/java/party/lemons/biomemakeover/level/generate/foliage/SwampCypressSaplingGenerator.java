package party.lemons.biomemakeover.level.generate.foliage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;

public class SwampCypressSaplingGenerator extends AbstractTreeGrower {

    public static final ResourceKey<ConfiguredFeature<?,?>> SMALL = ResourceKey.create(Registries.CONFIGURED_FEATURE, BiomeMakeover.ID("swamp/swamp_cypress"));

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean bl)
    {
        return SMALL;
    }

    public boolean growTree(ServerLevel serverLevel, ChunkGenerator chunkGenerator, BlockPos blockPos, BlockState blockState, RandomSource random) {
        ResourceKey<ConfiguredFeature<?,?>> key = this.getConfiguredFeature(random, this.hasFlowers(serverLevel, blockPos));
        if (key == null) {
            return false;
        }

        Holder<ConfiguredFeature<?,?>> holder = serverLevel.registryAccess()
                .registryOrThrow(Registries.CONFIGURED_FEATURE)
                .getHolder(key)
                .orElse(null);

        if(holder != null) {

            serverLevel.setBlock(blockPos, Blocks.WATER.defaultBlockState(), 4);
            if (holder.value().place(serverLevel, chunkGenerator, random, blockPos)) {
                return true;
            }
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