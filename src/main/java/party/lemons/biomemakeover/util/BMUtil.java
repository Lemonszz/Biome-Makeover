package party.lemons.biomemakeover.util;

import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.util.access.AxeItemAccess;

public class BMUtil
{
	public static final Direction[] HORIZONTALS = new Direction[]{
			Direction.NORTH,
			Direction.EAST,
			Direction.SOUTH,
			Direction.WEST
	};

	public static void addStrippedLog(Block log, Block stripped)
	{
		((AxeItemAccess)Items.WOODEN_AXE).addStrippable(log, stripped);
	}

	public static boolean isAdjacentDirection(Direction current, Direction check)
	{
		return check != current.getOpposite();
	}
}
