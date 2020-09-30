package party.lemons.biomemakeover;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BuiltinBiomes;
import party.lemons.biomemakeover.crafting.itemgroup.BiomeMakeoverItemGroup;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.util.access.BiomeEffectsAccessor;

public class BiomeMakeover implements ModInitializer
{
	public static final String MODID = "biomemakeover";
	public static ItemGroup GROUP;

	@Override
	public void onInitialize()
	{
		GROUP = new BiomeMakeoverItemGroup(new Identifier(MODID, MODID));
		BMWorldGen.init();
		BMEffects.init();

		BMEntities.init();
		BMBlocks.init();
		BMItems.init();

		//TOOD: Move
		BiomeEffectsAccessor.setWaterColor(BuiltinRegistries.BIOME.get(BiomeKeys.MUSHROOM_FIELDS), 0xad3fe4);
		BiomeEffectsAccessor.setWaterColor(BuiltinRegistries.BIOME.get(BiomeKeys.MUSHROOM_FIELD_SHORE), 0x633fe4);
	}

	public static Identifier ID(String path)
	{
		return new Identifier(MODID, path);
	}
}
