package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.world.feature.config.FallenLogFeatureConfig;

import java.util.Random;

public class FallenLogFeature extends Feature<FallenLogFeatureConfig>
{
	public FallenLogFeature(Codec<FallenLogFeatureConfig> configCodec)
	{
		super(configCodec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, FallenLogFeatureConfig config)
	{
		int length = config.length.getValue(random);
		Direction.Axis axis = RandomUtil.choose(Direction.Axis.X, Direction.Axis.Z);
		Direction direction = axis == Direction.Axis.X ? RandomUtil.choose(Direction.EAST, Direction.WEST) : RandomUtil.choose(Direction.NORTH, Direction.SOUTH);
		int mushroomChance = config.mushroomChance;

		int yPos = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
		BlockPos start = new BlockPos(pos.getX(), yPos, pos.getZ());

		for(int i = 0; i < length; i++)
		{
			BlockPos genPos = start.offset(direction, i);
			BlockState downState = world.getBlockState(genPos.down());
			FluidState downFluid = world.getFluidState(genPos.down());
			if(!downFluid.isEmpty())
				break;

			if(canReplace(downState))
			{
				genPos = genPos.down();
				start = start.down();
			}

			if(canReplace(world.getBlockState(genPos)) && world.setBlockState(genPos, config.log.getBlockState(random, pos).with(PillarBlock.AXIS, axis), 2))
			{
				BlockPos upPos = genPos.up();

				if(world.isAir(upPos))
				{
					if(random.nextInt(100) <= mushroomChance)
					{
						BlockState st = config.mushroom.getBlockState(random, upPos);
						world.setBlockState(upPos, st, 1);
					}
				}

			}
			else
			{
				break;
			}
		}
		return true;
	}

	public boolean canReplace(BlockState state)
	{
		return state.isAir() || state.getBlock() == Blocks.GRASS;
	}
}
