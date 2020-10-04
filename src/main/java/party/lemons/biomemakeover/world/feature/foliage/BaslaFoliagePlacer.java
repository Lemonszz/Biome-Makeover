package party.lemons.biomemakeover.world.feature.foliage;

import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.PineFoliagePlacer;

import java.util.Random;

public class BaslaFoliagePlacer extends PineFoliagePlacer
{
	public BaslaFoliagePlacer(UniformIntDistribution radius, UniformIntDistribution offset, UniformIntDistribution height)
	{
		super(radius, offset, height);
	}

	public int getRandomRadius(Random random, int baseHeight) {
		return super.getRandomRadius(random, baseHeight) + random.nextInt(8);
	}

	public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
		return 1 + random.nextInt(2);
	}
}
