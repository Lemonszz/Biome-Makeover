package party.lemons.biomemakeover.level.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class GrassPatchFeatureConfig implements FeatureConfiguration
{
    public static final Codec<GrassPatchFeatureConfig> CODEC = RecordCodecBuilder.create((instance)->
    {
        return instance.group(BlockStateProvider.CODEC.fieldOf("grass_provider").forGetter((c)->
        {
            return c.grass;
        }), BlockStateProvider.CODEC.fieldOf("dirt_provider").forGetter((c)->
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