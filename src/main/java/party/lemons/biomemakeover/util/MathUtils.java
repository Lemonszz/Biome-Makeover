package party.lemons.biomemakeover.util;

import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.world.biome.Biome;
import party.lemons.biomemakeover.util.access.ChunkRenderRegionAccess;

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

	public static int colourBoost(int color, int rShift, int gShift, int bShift)
	{
		int r = color >> 16 & 0xFF;
		int g = color >> 8 & 0xFF;
		int b = color & 0xFF;

		color = (Math.max(0, Math.min(0xFF, r + rShift)) << 16) + Math.max(0, Math.min(0xFF, g + gShift) << 8) + Math.max(0, Math.min(0xFF, b + bShift));

		return color;
	}
}
