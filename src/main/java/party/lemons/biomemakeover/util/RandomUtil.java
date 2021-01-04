package party.lemons.biomemakeover.util;

import java.util.Random;

public final class RandomUtil
{
	public static final Random RANDOM = new Random();

	public static int randomRange(int min, int max)
	{
		return RANDOM.nextInt(max - min) + min;
	}

	public static float randomDirection(float value)
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

	public static int randomBias(int min, int max)
	{
		int num = randomRange(min, max);
		int mid = (max / 2) - (min / 2);
		int halfMid = mid / 2;
		if(num > mid)
			num -= RANDOM.nextInt((halfMid + 1));
		else if(num < mid)
			num += RANDOM.nextInt((halfMid + 1));

		return num;
	}

	private RandomUtil(){}
}
