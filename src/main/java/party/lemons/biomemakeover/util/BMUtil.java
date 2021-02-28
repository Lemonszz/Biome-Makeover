package party.lemons.biomemakeover.util;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.Tag;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.util.access.AxeItemAccess;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class BMUtil
{
	public static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	private static final List<Direction> randomHorizontals = Lists.newArrayList(HORIZONTALS);

	public static <T> boolean isInAny(T tagged, Tag<T>... tags)
	{
		for(Tag<T> t : tags)
		{
			if(t.contains(tagged))
				return true;
		}
		return false;
	}

	public static Direction randomHorizontal()
	{
		return HORIZONTALS[RandomUtil.RANDOM.nextInt(HORIZONTALS.length)];
	}

	public static Hand getHandPossiblyHolding(LivingEntity entity, Predicate<ItemStack> predicate)
	{
		return predicate.test(entity.getMainHandStack()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
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
