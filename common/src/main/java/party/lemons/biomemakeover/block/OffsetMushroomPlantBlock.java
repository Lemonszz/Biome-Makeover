package party.lemons.biomemakeover.block;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.function.Supplier;

public class OffsetMushroomPlantBlock extends BMMushroomPlantBlock
{
    public OffsetMushroomPlantBlock(Supplier<Holder<? extends ConfiguredFeature<?, ?>>> giantShroomFeature, Properties properties) {
        super(giantShroomFeature, properties);
    }

    public BlockBehaviour.OffsetType getOffsetType() {
        return BlockBehaviour.OffsetType.XZ;
    }
}
