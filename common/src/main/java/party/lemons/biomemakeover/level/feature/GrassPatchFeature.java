package party.lemons.biomemakeover.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.VegetationPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
import net.minecraft.world.level.lighting.LightEngine;

import java.util.function.Predicate;

public class GrassPatchFeature extends VegetationPatchFeature
{

    public GrassPatchFeature(Codec<VegetationPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeGround(WorldGenLevel worldGenLevel, VegetationPatchConfiguration vegetationPatchConfiguration, Predicate<BlockState> predicate, RandomSource random, BlockPos.MutableBlockPos mutableBlockPos, int i) {
        for (int j = 0; j < i; ++j) {
            BlockState currentPos;
            BlockState grassState = vegetationPatchConfiguration.groundState.getState(random, mutableBlockPos);
            if (grassState.is((currentPos = worldGenLevel.getBlockState(mutableBlockPos)).getBlock())) continue;
            if (!predicate.test(currentPos)) {
                return j != 0;
            }
            if(!canBeGrass(grassState, worldGenLevel, mutableBlockPos))
                grassState = Blocks.DIRT.defaultBlockState();

            worldGenLevel.setBlock(mutableBlockPos, grassState, 2);

            BlockPos belowPos = mutableBlockPos.below();
            BlockState belowState = worldGenLevel.getBlockState(belowPos);
            if(belowState.is(grassState.getBlock()))
                if(!canBeGrass(belowState, worldGenLevel, belowPos))
                    worldGenLevel.setBlock(belowPos,  Blocks.DIRT.defaultBlockState(), 2);

            mutableBlockPos.move(vegetationPatchConfiguration.surface.getDirection());
        }
        return true;
    }

    private static boolean canBeGrass(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockPos blockPos2 = blockPos.above();
        BlockState blockState2 = levelReader.getBlockState(blockPos2);
        if (blockState2.is(Blocks.SNOW) && blockState2.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        }
        if (blockState2.getFluidState().getAmount() == 8) {
            return false;
        }
        int i = LightEngine.getLightBlockInto(levelReader, blockState, blockPos, blockState2, blockPos2, Direction.UP, blockState2.getLightBlock(levelReader, blockPos2));
        return i < levelReader.getMaxLightLevel();
    }

}