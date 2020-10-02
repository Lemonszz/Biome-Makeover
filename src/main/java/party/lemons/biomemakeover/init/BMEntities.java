package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.BuiltinBiomes;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DragonlingEntity;
import party.lemons.biomemakeover.entity.GhoulFishEntity;
import party.lemons.biomemakeover.entity.MushroomVillagerEntity;
import party.lemons.biomemakeover.util.RegistryHelper;

public class BMEntities
{
	public static final EntityType<GhoulFishEntity> GHOUL_FISH = FabricEntityTypeBuilder.<GhoulFishEntity>create(SpawnGroup.CREATURE, (t, w)->new GhoulFishEntity(w)).dimensions(EntityDimensions.fixed(1, 0.5F)).build();
	public static final EntityType<DragonlingEntity> DRAGONLING = FabricEntityTypeBuilder.<DragonlingEntity>create(SpawnGroup.MONSTER, (t, w)->new DragonlingEntity(w)).dimensions(EntityDimensions.fixed(0.75F, 1F)).build();
	public static final EntityType<MushroomVillagerEntity> MUSHROOM_TRADER = FabricEntityTypeBuilder.<MushroomVillagerEntity>create(SpawnGroup.MISC, (t, w)->new MushroomVillagerEntity(w)).dimensions(EntityDimensions.fixed(0.6F, 1.95F)).trackRangeBlocks(12).build();

	public static void init()
	{
		RegistryHelper.register(Registry.ENTITY_TYPE, EntityType.class, BMEntities.class);
		initSpawns();
	}

	private static void initSpawns()
	{
	}
}
