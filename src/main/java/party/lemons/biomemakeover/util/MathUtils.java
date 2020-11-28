package party.lemons.biomemakeover.util;

public class MathUtils
{
	public static float approachValue(float current, float target, float step)
	{
		if(current < target)
		{
			return Math.min(current + step, target);
		}
		return Math.max(current - step, target);
	}

	public static double approachValue(double current, double target, double step)
	{
		if(current < target)
		{
			return Math.min(current + step, target);
		}
		return Math.max(current - step, target);
	}
}
