package party.lemons.biomemakeover.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import party.lemons.biomemakeover.block.IvyBlock;
import party.lemons.biomemakeover.block.MothBlossomBlock;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.SpreadUtil;

public class ItchingIvyFeature extends Feature<NoneFeatureConfiguration> {
    private static final int TRIES = 10;
    private static final float SPREAD_RADIUS = 5.64f;

    public ItchingIvyFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx)
    {
        WorldGenLevel level = ctx.level();
        RandomSource random = ctx.random();
        BlockPos genPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, ctx.origin());
        if(!level.getBlockState(genPos.below()).is(BlockTags.LEAVES))
            return false;

        BlockState placeState = ((MothBlossomBlock)BMBlocks.MOTH_BLOSSOM.get()).getGrowState(level, genPos);
        if(placeState != Blocks.AIR.defaultBlockState()) {
            level.setBlock(genPos, placeState, 3);
            level.scheduleTick(genPos, BMBlocks.MOTH_BLOSSOM.get(), 0);

            for (int i = 0; i < TRIES; i++) {
                BlockPos ivyPos = SpreadUtil.sampleCircularOutwardlyFadingSpread(random, SPREAD_RADIUS,
                        (dx, dz) -> genPos.offset(Math.round(dx), 0, Math.round(dz))
                );
                ivyPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, ivyPos);
                if (level.getBlockState(ivyPos.below()).is(BlockTags.LEAVES)) {
                    level.setBlock(ivyPos, BMBlocks.ITCHING_IVY.get().defaultBlockState().setValue(IvyBlock.getPropertyForDirection(Direction.DOWN), true), 3);
                    level.scheduleTick(ivyPos, BMBlocks.ITCHING_IVY.get(), 0);
                }

            }

            return true;
        }

        return false;
    }
}
