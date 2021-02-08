package party.lemons.biomemakeover.world.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import party.lemons.biomemakeover.block.IlluniteClusterBlock;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Iterator;
import java.util.List;
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
		}
		else
		{
			List<BlockPos> rockPositions = Lists.newArrayList();
			for(int i = 0; i < 4; ++i)
			{
				int xSize = random.nextInt(3);
				int ySize = random.nextInt(4);
				int zSize = random.nextInt(3);
				float distance = (float) (xSize + ySize + zSize) * 0.333F + 0.5F;

				for(BlockPos genPos : BlockPos.iterate(blockPos.add(-xSize, -ySize, -zSize), blockPos.add(xSize, ySize, zSize)))
				{
					if(genPos.getSquaredDistance(blockPos) <= (double) (distance * distance))
					{
						BlockPos placePos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, genPos);
						BlockState downState = world.getBlockState(placePos.down());
						while(downState.getMaterial().isReplaceable() || downState.isIn(BlockTags.LOGS) || downState.isIn(BlockTags.LEAVES))
						{
							placePos = placePos.down();
							downState = world.getBlockState(placePos.down());
						}
						if(!(downState.isOf(BMBlocks.MESMERITE) || isSoil(downState.getBlock()) || isStone(downState.getBlock()) || downState.getBlock() == Blocks.GRAVEL))
							continue;

						world.setBlockState(placePos, cfg.state, 4);
						rockPositions.add(placePos);
					}
				}
				blockPos = blockPos.add(-1 + random.nextInt(2), -random.nextInt(2), -1 + random.nextInt(2));
			}
			for(BlockPos placePositions : rockPositions)
			{
				if(random.nextInt(10) == 0)
				{
					for(Direction direction : Direction.values())
					{
						BlockPos offsetPos = placePositions.offset(direction);
						if(world.isAir(offsetPos) && random.nextBoolean())
						{
							world.setBlockState(offsetPos, BMBlocks.ILLUNITE_CLUSTER.getDefaultState().with(IlluniteClusterBlock.FACING, direction), 16);
						}
					}
				}
			}

			return true;
		}
	}
}