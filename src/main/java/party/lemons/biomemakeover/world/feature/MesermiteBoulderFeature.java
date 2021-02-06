package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import party.lemons.biomemakeover.block.IlluniteClusterBlock;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Iterator;
import java.util.Random;

public class MesermiteBoulderFeature extends Feature<SingleStateFeatureConfig>
{
	public MesermiteBoulderFeature(Codec<SingleStateFeatureConfig> codec)
	{
		super(codec);
	}

	public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos blockPos, SingleStateFeatureConfig cfg)
	{
		for(; blockPos.getY() > 3; blockPos = blockPos.down())
		{
			if(!world.isAir(blockPos.down()))
			{
				Block block = world.getBlockState(blockPos.down()).getBlock();
				if(isSoil(block) || isStone(block))
				{
					break;
				}
			}
		}

		if(blockPos.getY() <= 3)
		{
			return false;
		}else
		{
			for(int i = 0; i < 10; ++i)
			{
				int xSize = random.nextInt(3);
				int ySize = random.nextInt(4);
				int zSize = random.nextInt(3);
				float distance = (float) (xSize + ySize + zSize) * 0.333F + 0.5F;
				Iterator it = BlockPos.iterate(blockPos.add(-xSize, -ySize, -zSize), blockPos.add(xSize, ySize, zSize)).iterator();

				while(it.hasNext())
				{
					BlockPos placePos = (BlockPos) it.next();
					if(placePos.getSquaredDistance(blockPos) <= (double) (distance * distance))
					{
						world.setBlockState(placePos, cfg.state, 4);

						if(random.nextInt(10) == 0)
						{
							for(Direction direction : Direction.values())
							{
								BlockPos offsetPos = placePos.offset(direction);
								if(world.isAir(offsetPos) && random.nextBoolean())
								{
									world.setBlockState(offsetPos, BMBlocks.ILLUNITE_CLUSTER.getDefaultState().with(IlluniteClusterBlock.FACING, direction), 16);
								}
							}
						}
					}
				}

				blockPos = blockPos.add(-1 + random.nextInt(2), -random.nextInt(2), -1 + random.nextInt(2));
			}

			return true;
		}
	}
}