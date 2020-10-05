package party.lemons.biomemakeover.block;

import net.minecraft.block.PressurePlateBlock;
import party.lemons.biomemakeover.util.BlockWithItem;

public class BMPressurePlateBlock extends PressurePlateBlock implements BlockWithItem
{
	public BMPressurePlateBlock(ActivationRule type, Settings settings)
	{
		super(type, settings);
	}
}
