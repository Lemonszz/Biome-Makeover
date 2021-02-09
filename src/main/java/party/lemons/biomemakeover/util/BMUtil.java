package party.lemons.biomemakeover.util;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.util.access.AxeItemAccess;

import java.util.Collections;
import java.util.List;

public class BMUtil
{
	public static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	private static final List<Direction> randomHorizontals = Lists.newArrayList(HORIZONTALS);

	public static Direction randomHorizontal()
	{
		return HORIZONTALS[RandomUtil.RANDOM.nextInt(HORIZONTALS.length)];
	}

	public static List<Direction> randomOrderedHorizontals()
	{
		Collections.shuffle(randomHorizontals);
		return randomHorizontals;
	}

	public static void addStrippedLog(Block log, Block stripped)
	{
		((AxeItemAccess) Items.WOODEN_AXE).addStrippable(log, stripped);
	}

	public static boolean isAdjacentDirection(Direction current, Direction check)
	{
		return check != current.getOpposite();
	}
}
