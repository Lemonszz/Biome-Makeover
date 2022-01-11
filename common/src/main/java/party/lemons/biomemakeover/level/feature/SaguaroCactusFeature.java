package party.lemons.biomemakeover.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import party.lemons.biomemakeover.block.SaguaroCactusBlock;
import party.lemons.biomemakeover.init.BMBlocks;

public class SaguaroCactusFeature extends Feature<NoneFeatureConfiguration> {
    public SaguaroCactusFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    private final SaguaroCactusBlock CACTUS = (SaguaroCactusBlock) BMBlocks.SAGUARO_CACTUS;

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        return SaguaroCactusBlock.generateCactus(CACTUS, ctx.level(), ctx.random().nextBoolean(), ctx.origin(), ctx.random(), false);
    }
}
