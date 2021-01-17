package party.lemons.biomemakeover.util;

import net.minecraft.block.Block;
import net.minecraft.item.Items;
import party.lemons.biomemakeover.util.access.AxeItemAccess;

public class BMUtil
{
	public static void addStrippedLog(Block log, Block stripped)
	{
		((AxeItemAccess)Items.WOODEN_AXE).addStrippable(log, stripped);
	}
}
