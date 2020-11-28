package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import party.lemons.biomemakeover.entity.*;
import party.lemons.biomemakeover.util.RegistryHelper;

import java.util.Map;

public class BMEntities
{
	public static final EntityType<MushroomVillagerEntity> MUSHROOM_TRADER = FabricEntityTypeBuilder.<MushroomVillagerEntity>create(SpawnGroup.AMBIENT, (t, w)->new MushroomVillagerEntity(w)).dimensions(EntityDimensions.fixed(0.6F, 1.95F)).trackRangeBlocks(12).build();
	public static final EntityType<BlightbatEntity> BLIGHTBAT = FabricEntityTypeBuilder.<BlightbatEntity>create(SpawnGroup.AMBIENT, (t, w)->new BlightbatEntity(w)).dimensions(EntityDimensions.fixed(0.56F, 0.9F)).trackRangeBlocks(5).build();
	public static final EntityType<GlowfishEntity> GLOWFISH = FabricEntityTypeBuilder.<GlowfishEntity>create(SpawnGroup.WATER_AMBIENT, (t, w)->new GlowfishEntity(w)).dimensions(EntityDimensions.fixed(0.7F, 0.4F)).trackRangeBlocks(4).build();
	public static final EntityType<TumbleweedEntity> TUMBLEWEED = FabricEntityTypeBuilder.<TumbleweedEntity>create(SpawnGroup.MISC, (t, w)->new TumbleweedEntity(w)).dimensions(EntityDimensions.fixed(0.7F, 0.7F)).trackRangeBlocks(12).build();
	public static final EntityType<CowboyEntity> COWBOY = FabricEntityTypeBuilder.<CowboyEntity>create(SpawnGroup.MONSTER, (t, w)->new CowboyEntity(w)).dimensions(EntityDimensions.fixed(0.6F, 1.95F)).spawnableFarFromPlayer().trackRangeBlocks(12).build();
	public static final EntityType<GhostEntity> GHOST = FabricEntityTypeBuilder.<GhostEntity>create(SpawnGroup.MONSTER, (t, w)->new GhostEntity(w)).dimensions(EntityDimensions.fixed(0.6F, 1.95F)).spawnableFarFromPlayer().trackRangeBlocks(12).build();
	public static final EntityType<ScuttlerEntity> SCUTTLER = FabricEntityTypeBuilder.<ScuttlerEntity>create(SpawnGroup.CREATURE, (t, w)->new ScuttlerEntity(w)).dimensions(EntityDimensions.fixed(0.8F, 0.6F)).trackRangeBlocks(12).build();
	public static final EntityType<ToadEntity> TOAD = FabricEntityTypeBuilder.<ToadEntity>create(SpawnGroup.CREATURE, (t, w)->new ToadEntity(w)).dimensions(EntityDimensions.fixed(0.8F, 0.6F)).trackRangeBlocks(12).build();
	public static final EntityType<DragonflyEntity> DRAGONFLY = FabricEntityTypeBuilder.<DragonflyEntity>create(SpawnGroup.AMBIENT, (t, w)->new DragonflyEntity(w)).dimensions(EntityDimensions.fixed(0.8F, 0.6F)).trackRangeBlocks(12).build();
	public static final EntityType<BMBoatEntity> BM_BOAT = FabricEntityTypeBuilder.create(SpawnGroup.MISC, (EntityType.EntityFactory<BMBoatEntity>)BMBoatEntity::new).trackable(128, 3).dimensions(EntityDimensions.fixed(1.375F, 0.5625F)).build();

	public static void init()
	{
		RegistryHelper.register(Registry.ENTITY_TYPE, EntityType.class, BMEntities.class);
	}

	public static void registerSpawnRestrictions(Map<EntityType<?>, SpawnRestriction.Entry> restrictions)
	{
		restrictions.put(BMEntities.GLOWFISH, new SpawnRestriction.Entry(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnRestriction.Location.IN_WATER, (e, world, spawnReason, pos, b)->world.getBlockState(pos).isOf(Blocks.WATER) && world.getBlockState(pos.up()).isOf(Blocks.WATER)));
		restrictions.put(BMEntities.SCUTTLER, new SpawnRestriction.Entry(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnRestriction.Location.ON_GROUND, (e, world, spawnReason, pos, b)->world.getBlockState(pos.down()).isOpaque()));
	}
}
