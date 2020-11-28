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

	private RandomUtil(){}
}
