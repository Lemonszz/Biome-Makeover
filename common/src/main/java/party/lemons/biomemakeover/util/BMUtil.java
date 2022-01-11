package party.lemons.biomemakeover.util;

import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

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

    public static boolean isAdjacentDirection(Direction current, Direction check)
    {
        return check != current.getOpposite();
    }

    public static InteractionHand getHandPossiblyHolding(LivingEntity entity, Predicate<ItemStack> predicate)
    {
        return predicate.test(entity.getMainHandItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }
}
