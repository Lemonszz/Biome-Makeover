package party.lemons.biomemakeover.util;

import java.util.Random;

public final class RandomUtil
{
	private static Random RANDOM = new Random();

	public static int randomRange(int min, int max)
	{
		return RANDOM.nextInt(max - min) + min;
	}

	private RandomUtil(){}
}
