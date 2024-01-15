package party.lemons.biomemakeover.util;

import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.Set;

public final class RandomUtil
{
    public static final RandomSource RANDOM = RandomSource.createThreadSafe();

    public static int randomRange(int min, int max)
    {
        return RANDOM.nextInt(max - min) + min;
    }

    public static double randomRange(double min, double max)
    {
        return min + (max - min) * RANDOM.nextDouble();
    }

    public static float randomDirection(float value)
    {
        return RANDOM.nextBoolean() ? value : -value;
    }

    public static double randomDirection(double value)
    {
        return RANDOM.nextBoolean() ? value : -value;
    }

    public static int randomDirection(int value)
    {
        return RANDOM.nextBoolean() ? value : -value;
    }

    public static <T> T choose(T... values)
    {
        return values[RANDOM.nextInt(values.length)];
    }

    public static <T> T choose(List<T> values)
    {
        return values.get(RANDOM.nextInt(values.size()));
    }

    public static <T> T choose(Set<T> values)
    {
        return values.stream().skip(RANDOM.nextInt(values.size())).findFirst().orElse(null);
    }

    public static int randomBias(int min, int max)
    {
        int num = randomRange(min, max);
        int mid = (max / 2) - (min / 2);
        int halfMid = mid / 2;
        if(num > mid) num -= RANDOM.nextInt((halfMid + 1));
        else if(num < mid) num += RANDOM.nextInt((halfMid + 1));

        return num;
    }

    private RandomUtil()
    {
    }
}
