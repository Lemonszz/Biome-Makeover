package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.function.Supplier;

public class OffsetMushroomPlantBlock extends BMMushroomPlantBlock
{
    public OffsetMushroomPlantBlock(Supplier<ConfiguredFeature<?, ?>> giantShroomFeature, Properties properties) {
        super(giantShroomFeature, properties);
    }

    public BlockBehaviour.OffsetType getOffsetType() {
        return BlockBehaviour.OffsetType.XZ;
    }
}
