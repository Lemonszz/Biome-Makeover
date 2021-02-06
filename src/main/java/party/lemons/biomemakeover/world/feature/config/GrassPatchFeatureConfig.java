package party.lemons.biomemakeover.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class GrassPatchFeatureConfig implements FeatureConfig
{
	public static final Codec<GrassPatchFeatureConfig> CODEC = RecordCodecBuilder.create((instance)->
	{
		return instance.group(BlockStateProvider.TYPE_CODEC.fieldOf("grass_provider").forGetter((c)->
		{
			return c.grass;
		}), BlockStateProvider.TYPE_CODEC.fieldOf("dirt_provider").forGetter((c)->
		{
			return c.dirt;
		}), Codec.INT.fieldOf("size").orElse(7).forGetter((x)->
		{
			return x.size;
		})).apply(instance, GrassPatchFeatureConfig::new);
	});
	public final BlockStateProvider grass, dirt;
	public final int size;

	public GrassPatchFeatureConfig(BlockStateProvider grass, BlockStateProvider dirt, int size)
	{
		this.grass = grass;
		this.dirt = dirt;
		this.size = size;
	}
}
