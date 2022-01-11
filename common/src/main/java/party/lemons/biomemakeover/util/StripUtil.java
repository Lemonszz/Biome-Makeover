package party.lemons.biomemakeover.util;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import party.lemons.biomemakeover.util.access.AxeItemAccess;

public class StripUtil
{
    public static void addStrippedLog(Block log, Block stripped)
    {
        ((AxeItemAccess)Items.WOODEN_AXE).bm_addStrippableLog(log, stripped);
    }
}
