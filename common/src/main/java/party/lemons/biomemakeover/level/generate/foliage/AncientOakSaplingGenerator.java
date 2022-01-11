package party.lemons.biomemakeover.level.generate.foliage;

import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.Random;

public class AncientOakSaplingGenerator extends AbstractMegaTreeGrower {
    @Nullable
    @Override
    protected ConfiguredFeature<?, ?> getConfiguredFeature(Random random, boolean bl) {
        return BMWorldGen.DarkForest.ANCIENT_OAK_SMALL;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<?, ?> getConfiguredMegaFeature(Random random) {
        return BMWorldGen.DarkForest.ANCIENT_OAK;
    }
}
