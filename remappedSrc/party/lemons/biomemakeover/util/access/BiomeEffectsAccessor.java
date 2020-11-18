package party.lemons.biomemakeover.util.access;

import net.minecraft.world.biome.Biome;

public interface BiomeEffectsAccessor
{
	void setWaterColor(int color);

	static void setWaterColor(Biome biome, int color)
	{
		((BiomeEffectsAccessor) biome.getEffects()).setWaterColor(color);
	}
}
