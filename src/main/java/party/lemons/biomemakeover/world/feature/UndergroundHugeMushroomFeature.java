package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;

import java.util.Random;

public abstract class UndergroundHugeMushroomFeature extends Feature<HugeMushroomFeatureConfig>
{
	public UndergroundHugeMushroomFeature(Codec<HugeMushroomFeatureConfig> codec)
	{
		super(codec);
	}

	protected void generateStem(WorldAccess world, Random random, BlockPos pos, HugeMushroomFeatureConfig config, int height, BlockPos.Mutable mutable)
	{
		for(int i = 0; i < height; ++i)
		{
			mutable.set(pos).move(Direction.UP, i);
			if(!world.getBlockState(mutable).isOpaqueFullCube(world, mutable))
			{
				this.setBlockState(world, mutable, config.stemProvider.getBlockState(random, pos));
			}
		}

	}

	protected int getHeight(Random random)
	{
		int i = random.nextInt(3) + 4;
		if(random.nextInt(12) == 0)
		{
			i *= 2;
		}

		return i;
	}

	protected boolean canGenerate(WorldAccess world, BlockPos pos, int height, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config)
	{
		int i = pos.getY();
		if(i >= 1 && i + height + 1 < 256)
		{
			Block block = world.getBlockState(pos.down()).getBlock();
			if(!isSoil(block) && !isStone(block) && !block.isIn(BlockTags.MUSHROOM_GROW_BLOCK))
			{
				return false;
			}else
			{
				for(int j = 0; j <= height; ++j)
				{
					BlockState st = world.getBlockState(mutable.set(pos, 0, j, 0));
					if(!st.isAir() && !st.isIn(BlockTags.LEAVES))
					{
						return false;
					}
				}

				return true;
			}
		}else
		{
			return false;
		}
	}

	public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, HugeMushroomFeatureConfig hugeMushroomFeatureConfig)
	{
		int i = this.getHeight(random);
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		if(!this.canGenerate(structureWorldAccess, blockPos, i, mutable, hugeMushroomFeatureConfig))
		{
			return false;
		}else
		{
			this.generateCap(structureWorldAccess, random, blockPos, i, mutable, hugeMushroomFeatureConfig);
			this.generateStem(structureWorldAccess, random, blockPos, hugeMushroomFeatureConfig, i, mutable);
			return true;
		}
	}

	protected abstract int getCapSize(int i, int j, int capSize, int y);

	protected abstract void generateCap(WorldAccess world, Random random, BlockPos start, int y, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config);
}
