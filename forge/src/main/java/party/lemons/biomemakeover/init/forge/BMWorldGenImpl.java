package party.lemons.biomemakeover.init.forge;

import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import party.lemons.biomemakeover.init.BMWorldGen;

public class BMWorldGenImpl
{
	public static TagKey<Biome>[] getMushroomTags()
	{
		return new TagKey[]{
				Tags.Biomes.IS_MUSHROOM
		};
	}

	public static TagKey<Biome>[] getBadlandsTags()
	{
		return new TagKey[]{
				BiomeTags.IS_BADLANDS,
		};
	}

	public static TagKey<Biome>[] getSwampTags()
	{
		return new TagKey[]{
				Tags.Biomes.IS_SWAMP
		};
	}

	public static TagKey<Biome>[] getDarkForestTags()
	{
		return new TagKey[]{
				BMWorldGen.DARK_FOREST_BIOMES
		};
	}

	public static TagKey<Biome>[] getBeachTags()
	{
		return new TagKey[]{
				Tags.Biomes.IS_BEACH
		};
	}
}
