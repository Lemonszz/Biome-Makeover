package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

public class WaterTreeFeature extends TreeFeature
{
	public WaterTreeFeature(Codec<TreeFeatureConfig> codec)
	{
		super(codec);
	}
}
