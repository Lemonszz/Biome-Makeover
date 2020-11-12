package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Iterator;
import java.util.Random;

public class PayDirtFeature extends Feature<DefaultFeatureConfig>
{
	private final BlockState PAY_DIRT = BMBlocks.PAYDIRT.getDefaultState();

	public PayDirtFeature(Codec<DefaultFeatureConfig> configCodec)
	{
		super(configCodec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig featureConfig)
	{
		for(int i = 0; i < 3; ++i)
		{
			int xSize = random.nextInt(4);
			int ySize = random.nextInt(4);
			int zSize = random.nextInt(4);
			float distance = (float)(xSize + ySize + zSize) * 0.333F + 0.5F;

			for(BlockPos generatePos : BlockPos.iterate(blockPos.add(-xSize, -ySize, -zSize), blockPos.add(xSize, ySize, zSize)))
			{
				BlockState currentState = world.getBlockState(generatePos);
				if(currentState.getBlock() == Blocks.WATER || currentState.isAir()) continue;

				if(generatePos.getSquaredDistance(blockPos) <= (double) (distance * distance))
				{
					boolean found = false;
					for(Direction dir : Direction.values())
					{
						Block st = world.getBlockState(generatePos.offset(dir)).getBlock();
						if(st == Blocks.WATER || (st == PAY_DIRT.getBlock() && random.nextBoolean()))
						{
							found = true;
							break;
						}
					}

					if(!found) continue;

					world.setBlockState(generatePos, PAY_DIRT, 4);
				}
			}

			blockPos = blockPos.add(-1 + random.nextInt(2), -random.nextInt(2), -1 + random.nextInt(2));
		}
		return true;
	}
}
