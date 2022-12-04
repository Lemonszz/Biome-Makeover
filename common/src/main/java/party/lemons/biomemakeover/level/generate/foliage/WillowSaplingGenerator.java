package party.lemons.biomemakeover.level.generate.foliage;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;

public class WillowSaplingGenerator extends AbstractTreeGrower {
    public static final ResourceKey<ConfiguredFeature<?,?>> SMALL = ResourceKey.create(Registries.CONFIGURED_FEATURE, BiomeMakeover.ID("swamp/willow"));

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean bl)
    {
        return SMALL;
    }
}