package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DragonlingEntity;
import party.lemons.biomemakeover.entity.GhoulFishEntity;

public class BMEntities
{
	public static final EntityType<GhoulFishEntity> GHOUL_FISH = FabricEntityTypeBuilder.<GhoulFishEntity>create(SpawnGroup.CREATURE, (t, w)->new GhoulFishEntity(w)).dimensions(EntityDimensions.fixed(1, 0.5F)).build();
	public static final EntityType<DragonlingEntity> DRAGONLING = FabricEntityTypeBuilder.<DragonlingEntity>create(SpawnGroup.MONSTER, (t, w)->new DragonlingEntity(w)).dimensions(EntityDimensions.fixed(0.75F, 1F)).build();

	public static void init()
	{
		Registry.register(Registry.ENTITY_TYPE, new Identifier(BiomeMakeover.MODID, "ghoul_fish"), GHOUL_FISH);
		Registry.register(Registry.ENTITY_TYPE, new Identifier(BiomeMakeover.MODID, "dragonling"), DRAGONLING);
	}
}
