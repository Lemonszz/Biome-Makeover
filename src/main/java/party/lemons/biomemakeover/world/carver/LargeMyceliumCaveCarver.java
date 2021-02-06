package party.lemons.biomemakeover.world.carver;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.CaveCarver;
import org.apache.commons.lang3.mutable.MutableBoolean;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

public class LargeMyceliumCaveCarver extends CaveCarver
{
	private final BlockState MYCELIUM = Blocks.MYCELIUM.getDefaultState();
	private final BlockState MYCELIUM_SPROUTS = BMBlocks.MYCELIUM_SPROUTS.getDefaultState();
	private final BlockState MYCELIUM_ROOTS = BMBlocks.MYCELIUM_ROOTS.getDefaultState();
	private final BlockState DIRT = Blocks.DIRT.getDefaultState();

	public LargeMyceliumCaveCarver(Codec<ProbabilityConfig> codec, int i)
	{
		super(codec, i);
	}

	protected boolean carveAtPoint(Chunk chunk, Function<BlockPos, Biome> posToBiome, BitSet carvingMask, Random random, BlockPos.Mutable mutable, BlockPos.Mutable mutable2, BlockPos.Mutable mutable3, int seaLevel, int mainChunkX, int mainChunkZ, int x, int z, int relativeX, int y, int relativeZ, MutableBoolean mutableBoolean)
	{
		int i = relativeX | relativeZ << 4 | y << 8;
		if(carvingMask.get(i))
		{
			return false;
		}else
		{
			carvingMask.set(i);
			mutable.set(x, y, z);
			BlockState blockState = chunk.getBlockState(mutable);
			BlockState blockState2 = chunk.getBlockState(mutable2.set(mutable, Direction.UP));
			if(blockState.isOf(Blocks.GRASS_BLOCK) || blockState.isOf(Blocks.MYCELIUM))
			{
				mutableBoolean.setTrue();
			}

			if(!this.canCarveBlock(blockState, blockState2))
			{
				return false;
			}else
			{
				if(y == 10)
				{
					chunk.setBlockState(mutable, MYCELIUM, false);
					if(random.nextInt(5) == 0)
						chunk.setBlockState(mutable.up(), random.nextBoolean() ? MYCELIUM_SPROUTS : MYCELIUM_ROOTS, false);
				}else if(y < 10)
				{
					chunk.setBlockState(mutable, DIRT, false);
				}else
				{
					chunk.setBlockState(mutable, CAVE_AIR, false);
					if(mutableBoolean.isTrue())
					{
						mutable3.set(mutable, Direction.DOWN);
						if(chunk.getBlockState(mutable3).isOf(Blocks.DIRT))
						{
							chunk.setBlockState(mutable3, posToBiome.apply(mutable).getGenerationSettings().getSurfaceConfig().getTopMaterial(), false);
						}
					}
				}

				return true;
			}
		}
	}


	protected int getMaxCaveCount()
	{
		return 30;
	}

	protected float getTunnelSystemWidth(Random random)
	{
		float f = random.nextFloat() * 5.0F + random.nextFloat();
		if(random.nextInt(5) == 0)
		{
			f *= random.nextFloat() * random.nextFloat() * 3.0F + 1.0F;
		}

		return f;
	}

	protected double getTunnelSystemHeightWidthRatio()
	{
		return 1.3D;
	}

}
