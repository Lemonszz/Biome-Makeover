package party.lemons.biomemakeover.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.SpreadUtil;

public class OrangeGlowshroomFeature extends Feature<ProbabilityFeatureConfiguration>
{
    private static final float SPREAD_RADIUS = 9.0f;

    public OrangeGlowshroomFeature(Codec<ProbabilityFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> featurePlaceContext) {
        boolean success = false;
        RandomSource random = featurePlaceContext.random();
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();

        BlockPos placePos = SpreadUtil.sampleCircularOutwardlyFadingSpread(random, SPREAD_RADIUS, (dx, dz) -> {
            int xOffset = Math.round(dx);
            int zOffset = Math.round(dz);
            int yLevel = level.getHeight(Heightmap.Types.OCEAN_FLOOR, pos.getX() + xOffset, pos.getZ() + zOffset);
            return new BlockPos(pos.getX() + xOffset, yLevel, pos.getZ() + zOffset);
        });

        if (level.getBlockState(placePos).is(Blocks.WATER))
        {
            BlockState state = BMBlocks.ORANGE_GLOWSHROOM.get().defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true);
            if (state.canSurvive(level, placePos)) {
                level.setBlock(placePos, state, 2);
                success = true;
            }
        }
        return success;
    }
}
