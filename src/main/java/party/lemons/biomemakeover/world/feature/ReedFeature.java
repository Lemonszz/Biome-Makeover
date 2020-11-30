package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;

import java.util.Random;

public class ReedFeature extends Feature<RandomPatchFeatureConfig>
{
	public ReedFeature(Codec<RandomPatchFeatureConfig> codec) {
		super(codec);
	}

	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, RandomPatchFeatureConfig config) {
		BlockPos centerPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, blockPos).down();

		int successes = 0;
		BlockPos.Mutable placePos = new BlockPos.Mutable();

		for(int j = 0; j < config.tries; ++j)
		{
			BlockState blockState = config.stateProvider.getBlockState(random, blockPos);

			placePos.set(centerPos, random.nextInt(config.spreadX + 1) - random.nextInt(config.spreadX + 1), random.nextInt(config.spreadY + 1) - random.nextInt(config.spreadY + 1), random.nextInt(config.spreadZ + 1) - random.nextInt(config.spreadZ + 1));
			BlockPos genPos = placePos.down();
			BlockPos floorPos = genPos.down();
			if(world.getFluidState(genPos).getFluid() == Fluids.WATER && world.getFluidState(placePos).isEmpty() && world.getFluidState(floorPos).isEmpty())
			{
				config.blockPlacer.generate(world, genPos, blockState, random);
				++successes;
			}

		}

		return successes > 0;
	}
}
