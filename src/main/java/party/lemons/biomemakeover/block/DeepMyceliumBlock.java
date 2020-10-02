package party.lemons.biomemakeover.block;

import net.minecraft.block.Block;
import net.minecraft.block.MyceliumBlock;
import party.lemons.biomemakeover.util.BlockWithItem;

public class DeepMyceliumBlock extends MyceliumBlock implements BlockWithItem
{
	public DeepMyceliumBlock(Block.Settings settings)
	{
		super(settings.postProcess((s,w,p)->true));
	}
}
