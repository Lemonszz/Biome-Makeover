package party.lemons.biomemakeover.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Random;

public class OrangeGlowshroomFeature extends Feature<ProbabilityFeatureConfiguration>
{
    public OrangeGlowshroomFeature(Codec<ProbabilityFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> featurePlaceContext) {
        boolean success = false;
        RandomSource random = featurePlaceContext.random();
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        int xOffset = random.nextInt(8) - random.nextInt(8);
        int zOffset = random.nextInt(8) - random.nextInt(8);
        int yLevel = level.getHeight(Heightmap.Types.OCEAN_FLOOR, pos.getX() + xOffset, pos.getZ() + zOffset);

        BlockPos placePos = new BlockPos(pos.getX() + xOffset, yLevel, pos.getZ() + zOffset);
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
