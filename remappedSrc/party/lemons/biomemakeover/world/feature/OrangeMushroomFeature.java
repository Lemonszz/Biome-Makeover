package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallSeagrassBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import party.lemons.biomemakeover.init.BMBlocks;

public class OrangeMushroomFeature extends Feature<ProbabilityConfig>
{
	public OrangeMushroomFeature(Codec<ProbabilityConfig> codec) {
		super(codec);
	}

	public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, ProbabilityConfig probabilityConfig) {
		int xOffset = random.nextInt(8) - random.nextInt(8);
		int zOffset = random.nextInt(8) - random.nextInt(8);
		int yGenerate = structureWorldAccess.getTopY(Heightmap.Type.OCEAN_FLOOR, blockPos.getX() + xOffset, blockPos.getZ() + zOffset);
		BlockPos generatePos = new BlockPos(blockPos.getX() + xOffset, yGenerate, blockPos.getZ() + zOffset);
		if (structureWorldAccess.getBlockState(generatePos).isOf(Blocks.WATER)) {
			BlockState state = BMBlocks.ORANGE_GLOWSHROOM.getDefaultState().with(Properties.WATERLOGGED, true);
			if (state.canPlaceAt(structureWorldAccess, generatePos)) {
				structureWorldAccess.setBlockState(generatePos, state, 2);
			}
		}

		return true;
	}
}
