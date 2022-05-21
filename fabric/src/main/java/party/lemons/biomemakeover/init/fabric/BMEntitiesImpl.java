package party.lemons.biomemakeover.init.fabric;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import party.lemons.biomemakeover.init.BMWorldGen;

public class BMEntitiesImpl
{
	public static void registerSpawn(TagKey<Biome>[] tags, EntityType<?> type, MobCategory category, int weight, int min, int max)
	{

		BiomeModifications.addSpawn((c)->{
			for(TagKey<Biome> tag : tags)
			{
				if(c.hasTag(tag))
					return true;
			}
			return false;
		}, category, type, weight, min, max);
	}

	public static TagKey<Biome>[] getMushroomTags()
	{
		return new TagKey[]{
				ConventionalBiomeTags.MUSHROOM
		};
	}

	public static TagKey<Biome>[] getBadlandsTags()
	{
		return new TagKey[]{
				BiomeTags.IS_BADLANDS,
				ConventionalBiomeTags.BADLANDS
		};
	}

	public static TagKey<Biome>[] getSwampTags()
	{
		return new TagKey[]{
				ConventionalBiomeTags.SWAMP
		};
	}

	public static TagKey<Biome>[] getDarkForestTags()
	{
		return new TagKey[]{
				BMWorldGen.DARK_FOREST_BIOMES
		};
	}
}
