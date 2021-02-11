package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import party.lemons.biomemakeover.block.IvyBlock;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class ItchingIvyFeature extends Feature<DefaultFeatureConfig>
{
	public ItchingIvyFeature(Codec<DefaultFeatureConfig> configCodec)
	{
		super(configCodec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config)
	{
		BlockPos genPos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos);
		if(!world.getBlockState(genPos.down()).isIn(BlockTags.LEAVES))
			return false;

		world.setBlockState(genPos, BMBlocks.MOTH_BLOSSOM.getPlaceState(world, genPos), 3);
		world.getBlockTickScheduler().schedule(genPos, BMBlocks.MOTH_BLOSSOM, 0);

		for(int i = 0; i < 10; i++)
		{
			int ivyX = RandomUtil.randomRange(-5, 5);
			int ivyZ = RandomUtil.randomRange(-5, 5);
			BlockPos ivyPos = genPos.add(ivyX, 0, ivyZ);
			ivyPos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, ivyPos);
			if(world.getBlockState(ivyPos.down()).isIn(BlockTags.LEAVES))
			{
				world.setBlockState(ivyPos, BMBlocks.ITCHING_IVY.getDefaultState().with(IvyBlock.getPropertyForDirection(Direction.DOWN), true), 3);
				world.getBlockTickScheduler().schedule(ivyPos, BMBlocks.ITCHING_IVY, 0);
			}

		}

		return true;
	}
}
