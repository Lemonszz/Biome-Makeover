package party.lemons.biomemakeover.world.feature.foliage;

import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.Random;

public class AncientOakSaplingGenerator extends LargeTreeSaplingGenerator
{
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl)
	{
		return BMWorldGen.ANCIENT_OAK_SMALL;
	}

	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createLargeTreeFeature(Random random)
	{
		return BMWorldGen.ANCIENT_OAK;
	}
}
