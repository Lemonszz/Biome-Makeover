package party.lemons.biomemakeover.level.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record GrassPatchFeatureConfig(
        BlockStateProvider grass,
        BlockStateProvider dirt,
        int size) implements FeatureConfiguration
{
    public static final Codec<GrassPatchFeatureConfig> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    BlockStateProvider.CODEC.fieldOf("grass_provider").forGetter((c) -> c.grass),
                    BlockStateProvider.CODEC.fieldOf("dirt_provider").forGetter((c) -> c.dirt),
                    Codec.INT.fieldOf("size").orElse(7).forGetter((x) -> x.size)
            ).apply(instance, GrassPatchFeatureConfig::new));
}