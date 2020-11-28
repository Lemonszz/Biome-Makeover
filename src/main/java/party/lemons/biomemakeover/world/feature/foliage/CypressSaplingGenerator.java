package party.lemons.biomemakeover.world.feature.foliage;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.Random;

public class CypressSaplingGenerator extends SaplingGenerator
{
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl)
	{
		return (ConfiguredFeature<TreeFeatureConfig, ?>) BMWorldGen.CYPRESS_TREE;
	}
}
