package party.lemons.biomemakeover.level.generate.foliage;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;

public class AncientOakSaplingGenerator extends AbstractMegaTreeGrower {
    public static final ResourceKey<ConfiguredFeature<?,?>> MEGA = ResourceKey.create(Registries.CONFIGURED_FEATURE, BiomeMakeover.ID("dark_forest/ancient_oak"));
    public static final ResourceKey<ConfiguredFeature<?,?>> SMALL = ResourceKey.create(Registries.CONFIGURED_FEATURE, BiomeMakeover.ID("dark_forest/ancient_oak_small"));

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource randomSource)
    {
        return MEGA;
    }

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean bl)
    {
        return SMALL;
    }

  /*  @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bl) {
        return BMWorldGen.DarkForest.ANCIENT_OAK_SMALL;
    }

    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
        return BMWorldGen.DarkForest.ANCIENT_OAK;
    }*/
}
