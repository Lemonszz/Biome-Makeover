package party.lemons.biomemakeover.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.function.Supplier;

public class OffsetMushroomPlantBlock extends BMMushroomPlantBlock
{
	public OffsetMushroomPlantBlock(Supplier<ConfiguredFeature> giantShroomFeature, Settings settings)
	{
		super(giantShroomFeature, settings);
	}

	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.XZ;
	}
}
