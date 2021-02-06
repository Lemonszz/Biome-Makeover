package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Iterator;
import java.util.Random;

public class PeatFeature extends Feature<DefaultFeatureConfig>
{
	public PeatFeature(Codec<DefaultFeatureConfig> codec)
	{
		super(codec);
	}

	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig cfg)
	{

		boolean success = false;
		int radius = RandomUtil.randomRange(4, 8);

		BlockPos centerPos = new BlockPos(blockPos.getX(), 61, blockPos.getZ());
		if(!isWaterNearby(world, centerPos)) return false;

		int placeCount = 0;
		for(int xx = centerPos.getX() - radius; xx <= centerPos.getX() + radius; xx++)
		{
			for(int zz = centerPos.getZ() - radius; zz <= centerPos.getZ() + radius; zz++)
			{
				int offsetX = xx - centerPos.getX();
				int offsetZ = zz - centerPos.getZ();
				if(offsetX * offsetX + offsetZ * offsetZ <= radius * radius)
				{
					BlockPos placePos = new BlockPos(xx, centerPos.getY(), zz);
					BlockState upState = world.getBlockState(placePos.up());
					if(upState.isOf(Blocks.GRASS_BLOCK))
					{
						placePeat(placePos, world, random, 5);
						if(random.nextInt(20) == 0)
						{
							world.setBlockState(placePos.up(), BMBlocks.MOSSY_PEAT.getDefaultState(), 2);
						}
						placeCount++;
						success = true;
					}
				}
			}
		}

		if(placeCount > 5)
		{
			for(int xx = centerPos.getX() - radius; xx <= centerPos.getX() + radius; xx++)
			{
				for(int zz = centerPos.getZ() - radius; zz <= centerPos.getZ() + radius; zz++)
				{
					int offsetX = xx - centerPos.getX();
					int offsetZ = zz - centerPos.getZ();
					if(offsetX * offsetX + offsetZ * offsetZ <= radius * radius)
					{
						BlockPos placePos = new BlockPos(xx, centerPos.getY(), zz);
						BlockState upState = world.getBlockState(placePos.up());
						if(random.nextInt(5) == 0 && world.getBlockState(placePos).isOpaque() && upState.isOf(Blocks.WATER) && world.getBlockState(placePos.up(2)).isAir())
						{
							world.setBlockState(placePos, BMBlocks.MOSSY_PEAT.getDefaultState(), 2);
						}
					}
				}
			}
		}

		return success;
	}

	private void placePeat(BlockPos pos, StructureWorldAccess world, Random random, int chance)
	{
		world.setBlockState(pos, BMBlocks.PEAT.getDefaultState(), 2);
		if(random.nextInt(chance) == 0)
		{
			placePeat(pos.down(), world, random, chance + 2);
		}
	}

	private static boolean isWaterNearby(WorldView world, BlockPos pos)
	{
		Iterator<BlockPos> iterators = BlockPos.iterate(pos.add(-3, 0, -3), pos.add(3, 1, 3)).iterator();

		BlockPos blockPos;
		do
		{
			if(!iterators.hasNext())
			{
				return false;
			}

			blockPos = iterators.next();
		}while(!world.getFluidState(blockPos).isIn(FluidTags.WATER));

		return true;
	}
}
