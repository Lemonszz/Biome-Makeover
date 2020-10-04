package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.entity.BlightbatEntity;
import party.lemons.biomemakeover.entity.GlowfishEntity;
import party.lemons.biomemakeover.entity.MushroomVillagerEntity;
import party.lemons.biomemakeover.util.RegistryHelper;

public class BMEntities
{
	public static final EntityType<MushroomVillagerEntity> MUSHROOM_TRADER = FabricEntityTypeBuilder.<MushroomVillagerEntity>create(SpawnGroup.AMBIENT, (t, w)->new MushroomVillagerEntity(w)).dimensions(EntityDimensions.fixed(0.6F, 1.95F)).trackRangeBlocks(12).build();
	public static final EntityType<BlightbatEntity> BLIGHTBAT = FabricEntityTypeBuilder.<BlightbatEntity>create(SpawnGroup.AMBIENT, (t, w)->new BlightbatEntity(w)).dimensions(EntityDimensions.fixed(0.56F, 0.9F)).trackRangeBlocks(5).build();
	public static final EntityType<GlowfishEntity> GLOWFISH = FabricEntityTypeBuilder.<GlowfishEntity>create(SpawnGroup.WATER_AMBIENT, (t, w)->new GlowfishEntity(w)).dimensions(EntityDimensions.fixed(0.7F, 0.4F)).trackRangeBlocks(4).build();

	public static void init()
	{
		RegistryHelper.register(Registry.ENTITY_TYPE, EntityType.class, BMEntities.class);
	}
}
