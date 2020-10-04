package party.lemons.biomemakeover.block;

import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import party.lemons.biomemakeover.util.BlockWithItem;

public class BMSaplingBlock extends SaplingBlock implements BlockWithItem
{
	public BMSaplingBlock(SaplingGenerator generator, Settings settings)
	{
		super(generator, settings);
	}
}
