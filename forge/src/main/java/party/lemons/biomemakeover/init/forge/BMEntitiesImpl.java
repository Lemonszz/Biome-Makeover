package party.lemons.biomemakeover.init.forge;

import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeBiomeTagsProvider;
import party.lemons.biomemakeover.forge.BMForge;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMWorldGen;

public class BMEntitiesImpl
{
	public static void registerSpawn(TagKey<Biome>[] tags, EntityType<?> type, MobCategory category, int weight, int min, int max)
	{
		BiomeModifications.addProperties(p->true, (biomeContext, mutable) -> {
			if(type == BMEntities.HELMIT_CRAB.get())
				if(!BMForge.BEACH_BIOMES.contains(biomeContext.getKey().get()))
					return;
			else if(type == BMEntities.ROOTLING.get() || type == EntityType.FOX || type == EntityType.RABBIT || type ==  BMEntities.MOTH.get() || type ==  BMEntities.OWL.get())
				if(!BMForge.DARK_FOREST_BIOMES.contains(biomeContext.getKey().get()))
					return;
			else if(type == BMEntities.SCUTTLER.get())
				if(!BMForge.MESA_BIOMES.contains(biomeContext.getKey().get()))
					return;
			else if(type == BMEntities.LIGHTNING_BUG.get() || type == BMEntities.DRAGONFLY.get() || type == BMEntities.DECAYED.get())
					if(!BMForge.SWAMP_BIOMES.contains(biomeContext.getKey().get()))
						return;
			else if(type == BMEntities.MUSHROOM_TRADER.get() || type == BMEntities.GLOWFISH.get() || type == BMEntities.BLIGHTBAT.get())
					if(!BMForge.MUSHROOM_BIOMES.contains(biomeContext.getKey().get()))
						return;
			mutable.getSpawnProperties().addSpawn(category, new MobSpawnSettings.SpawnerData(type, weight, min, max));
		});

		/*
		BiomeModifications.addSpawn((c)->{
			for(TagKey<Biome> tag : tags)
			{
				if(c.hasTag(tag))
					return true;
			}
			return false;
		}, category, type, weight, min, max);*/
	}

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
