package party.lemons.biomemakeover;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.BiomeKeys;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMWorldGen;

public class BiomeMakeover implements ModInitializer
{
	public static final String MODID = "biomemakeover";

	@Override
	public void onInitialize()
	{
		BMEntities.init();
		BMWorldGen.init();
	}

	public static Identifier ID(String path)
	{
		return new Identifier(MODID, path);
	}
}
