package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.*;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorMimicEntity;
import party.lemons.biomemakeover.util.RegistryHelper;

import java.util.Map;
import java.util.Random;

public class BMEntities
{
	public static final EntityType<MushroomVillagerEntity> MUSHROOM_TRADER = FabricEntityTypeBuilder.<MushroomVillagerEntity>create(SpawnGroup.AMBIENT, (t, w)->new MushroomVillagerEntity(w)).dimensions(EntityDimensions.fixed(0.6F, 1.95F)).trackRangeBlocks(12).build();
	public static final EntityType<DecayedEntity> DECAYED = FabricEntityTypeBuilder.<DecayedEntity>create(SpawnGroup.MONSTER, (t, w)->new DecayedEntity(w)).dimensions(EntityDimensions.fixed(0.6F, 1.95F)).trackRangeBlocks(8).build();
	public static final EntityType<GiantSlimeEntity> GIANT_SLIME = FabricEntityTypeBuilder.<GiantSlimeEntity>create(SpawnGroup.MONSTER, (t, w)->new GiantSlimeEntity(w)).dimensions(EntityDimensions.changing(2.04F, 2.04F)).trackRangeBlocks(10).build();
	public static final EntityType<BlightbatEntity> BLIGHTBAT = FabricEntityTypeBuilder.<BlightbatEntity>create(SpawnGroup.AMBIENT, (t, w)->new BlightbatEntity(w)).dimensions(EntityDimensions.fixed(0.56F, 0.9F)).trackRangeBlocks(5).build();
	public static final EntityType<GlowfishEntity> GLOWFISH = FabricEntityTypeBuilder.<GlowfishEntity>create(SpawnGroup.WATER_AMBIENT, (t, w)->new GlowfishEntity(w)).dimensions(EntityDimensions.fixed(0.7F, 0.4F)).trackRangeBlocks(4).build();
	public static final EntityType<TumbleweedEntity> TUMBLEWEED = FabricEntityTypeBuilder.<TumbleweedEntity>create(SpawnGroup.MISC, (t, w)->new TumbleweedEntity(w)).dimensions(EntityDimensions.fixed(0.7F, 0.7F)).trackRangeBlocks(12).build();
	public static final EntityType<CowboyEntity> COWBOY = FabricEntityTypeBuilder.<CowboyEntity>create(SpawnGroup.MONSTER, (t, w)->new CowboyEntity(w)).dimensions(EntityDimensions.fixed(0.6F, 1.95F)).spawnableFarFromPlayer().trackRangeBlocks(12).build();
	public static final EntityType<GhostEntity> GHOST = FabricEntityTypeBuilder.<GhostEntity>create(SpawnGroup.MONSTER, (t, w)->new GhostEntity(w)).dimensions(EntityDimensions.fixed(0.6F, 1.95F)).spawnableFarFromPlayer().trackRangeBlocks(12).build();
	public static final EntityType<ScuttlerEntity> SCUTTLER = FabricEntityTypeBuilder.<ScuttlerEntity>create(SpawnGroup.CREATURE, (t, w)->new ScuttlerEntity(w)).dimensions(EntityDimensions.fixed(0.8F, 0.6F)).trackRangeBlocks(12).build();
	public static final EntityType<ToadEntity> TOAD = FabricEntityTypeBuilder.<ToadEntity>create(SpawnGroup.CREATURE, (t, w)->new ToadEntity(w)).dimensions(EntityDimensions.fixed(0.8F, 0.6F)).trackRangeBlocks(12).build();
	public static final EntityType<OwlEntity> OWL = FabricEntityTypeBuilder.<OwlEntity>create(SpawnGroup.CREATURE, (t, w)->new OwlEntity(w)).dimensions(EntityDimensions.changing(0.7F, 0.8F)).trackRangeBlocks(12).build();
	public static final EntityType<TadpoleEntity> TADPOLE = FabricEntityTypeBuilder.<TadpoleEntity>create(SpawnGroup.WATER_CREATURE, (t, w)->new TadpoleEntity(w)).dimensions(EntityDimensions.fixed(0.5F, 0.3F)).trackRangeBlocks(12).build();
	public static final EntityType<DragonflyEntity> DRAGONFLY = FabricEntityTypeBuilder.<DragonflyEntity>create(SpawnGroup.AMBIENT, (t, w)->new DragonflyEntity(w)).dimensions(EntityDimensions.fixed(0.8F, 0.6F)).trackRangeBlocks(12).build();
	public static final EntityType<LightningBugEntity> LIGHTNING_BUG = FabricEntityTypeBuilder.<LightningBugEntity>create(SpawnGroup.AMBIENT, (t, w)->new LightningBugEntity(w)).dimensions(EntityDimensions.fixed(0.4F, 0.4F)).trackRangeBlocks(12).build();
	public static final EntityType<LightningBugEntity> LIGHTNING_BUG_ALTERNATE = FabricEntityTypeBuilder.<LightningBugEntity>create(SpawnGroup.MISC, (t, w)->new LightningBugEntity(w, true)).dimensions(EntityDimensions.fixed(0.4F, 0.4F)).trackRangeBlocks(12).build();
	public static final EntityType<BMBoatEntity> BM_BOAT = FabricEntityTypeBuilder.create(SpawnGroup.MISC, (EntityType.EntityFactory<BMBoatEntity>) BMBoatEntity::new).trackable(128, 3).dimensions(EntityDimensions.fixed(1.375F, 0.5625F)).build();
	public static final EntityType<LightningBottleEntity> LIGHTNING_BOTTLE = FabricEntityTypeBuilder.create(SpawnGroup.MISC, (EntityType.EntityFactory<LightningBottleEntity>) LightningBottleEntity::new).trackable(4, 10).dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build();
	public static final EntityType<RootlingEntity> ROOTLING = FabricEntityTypeBuilder.<RootlingEntity>create(SpawnGroup.CREATURE, (t, w)->new RootlingEntity(w)).dimensions(EntityDimensions.fixed(0.6F, 1.1F)).trackRangeBlocks(12).build();
	public static final EntityType<MothEntity> MOTH = FabricEntityTypeBuilder.<MothEntity>create(SpawnGroup.MONSTER, (t, w)->new MothEntity(w)).dimensions(EntityDimensions.fixed(0.8F, 1.2F)).trackRangeBlocks(12).build();
	public static final EntityType<AdjudicatorEntity> ADJUDICATOR = FabricEntityTypeBuilder.<AdjudicatorEntity>create(SpawnGroup.MONSTER, (t, w)->new AdjudicatorEntity(w)).fireImmune().dimensions(EntityDimensions.fixed(0.6F, 1.95F)).trackRangeBlocks(12).disableSummon().build();
	public static final EntityType<AdjudicatorMimicEntity> ADJUDICATOR_MIMIC = FabricEntityTypeBuilder.<AdjudicatorMimicEntity>create(SpawnGroup.MONSTER, (t, w)->new AdjudicatorMimicEntity(w)).fireImmune().dimensions(EntityDimensions.fixed(0.6F, 1.95F)).trackRangeBlocks(12).disableSummon().build();
	public static final EntityType<StoneGolemEntity> STONE_GOLEM = FabricEntityTypeBuilder.<StoneGolemEntity>create(SpawnGroup.MISC, (t, w)->new StoneGolemEntity(w)).dimensions(EntityDimensions.fixed(0.875F, 0.5F)).trackRangeBlocks(12).build();

	public static final EntityAttribute ATT_PROJECTILE_RESISTANCE = new ClampedEntityAttribute("attribute.name.biomemakeover.projectile_resistance", 0.0D, 0.0D, 30.0D);

	public static void init()
	{
		RegistryHelper.register(Registry.ENTITY_TYPE, EntityType.class, BMEntities.class);
		RegistryHelper.register(Registry.ATTRIBUTE, EntityAttribute.class, BMEntities.class);


	}

	public static void registerSpawnRestrictions(Map<EntityType<?>, SpawnRestriction.Entry> restrictions)
	{
		//TODO: use registrtRestriction to all
		restrictions.put(BMEntities.GLOWFISH, new SpawnRestriction.Entry(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnRestriction.Location.IN_WATER, (e, world, spawnReason, pos, b)->world.getBlockState(pos).isOf(Blocks.WATER) && world.getBlockState(pos.up()).isOf(Blocks.WATER)));
		restrictions.put(BMEntities.SCUTTLER, new SpawnRestriction.Entry(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnRestriction.Location.ON_GROUND, (e, world, spawnReason, pos, b)->world.getBlockState(pos.down()).isOpaque()));
		restrictions.put(BMEntities.DECAYED, new SpawnRestriction.Entry(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnRestriction.Location.IN_WATER, DecayedEntity::validSpawn));
		registerRestriction(restrictions, BMEntities.GIANT_SLIME, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GiantSlimeEntity::canSpawnGiantSlime);

		restrictions.put(BMEntities.TOAD, new SpawnRestriction.Entry(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnRestriction.Location.ON_GROUND, BMEntities::isValidAnimalSpawn));
		restrictions.put(BMEntities.DRAGONFLY, new SpawnRestriction.Entry(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnRestriction.Location.ON_GROUND, BMEntities::isValidAnimalSpawn));
		restrictions.put(BMEntities.LIGHTNING_BUG, new SpawnRestriction.Entry(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnRestriction.Location.ON_GROUND, BMEntities::isValidLightningBugSpawn));
		registerRestriction(restrictions, BMEntities.ROOTLING, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BMEntities::isValidDarkForestAnimalSpawn);
		registerRestriction(restrictions, BMEntities.OWL, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BMEntities::isValidOwlSpawn);
		registerRestriction(restrictions, BMEntities.MOTH, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING, BMEntities::isValidMothSpawn);
	}

	private static <T extends MobEntity> boolean isValidMothSpawn(EntityType<T> tEntityType, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random)
	{
		return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && (spawnReason == SpawnReason.SPAWNER || world.getBlockState(pos.down()).isIn(BlockTags.LEAVES));
	}

	private static <T extends MobEntity> void registerRestriction(Map<EntityType<?>, SpawnRestriction.Entry> restrictions, EntityType<T> type, SpawnRestriction.Location location, Heightmap.Type heightmapType, SpawnRestriction.SpawnPredicate<T> predicate)
	{
		SpawnRestriction.Entry entry = restrictions.put(type, new SpawnRestriction.Entry(heightmapType, location, predicate));
		if(entry != null)
		{
			throw new IllegalStateException("Duplicate registration for type " + Registry.ENTITY_TYPE.getId(type));
		}
	}
	public static boolean isValidAnimalSpawn(EntityType type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random)
	{
		return world.getBlockState(pos.down()).isOf(Blocks.GRASS_BLOCK) && world.getBaseLightLevel(pos, 0) > 8;
	}


	public static boolean isValidDarkForestAnimalSpawn(EntityType type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random)
	{
		return world.getBlockState(pos.down()).isOf(Blocks.GRASS_BLOCK) && world.getBaseLightLevel(pos, 0) > 2;
	}

	public static boolean isValidLightningBugSpawn(EntityType type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random)
	{
		return world.getBlockState(pos.down()).isOf(Blocks.GRASS_BLOCK);
	}

	public static boolean isValidOwlSpawn(EntityType type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random)
	{
		BlockState state = world.getBlockState(pos.down());

		return (state.isOf(Blocks.GRASS_BLOCK) || state.isIn(BlockTags.LEAVES)) && world.getBaseLightLevel(pos, 0) > 2;
	}


	public static final Tag<EntityType<?>> LIGHTNING_BUG_TAG = TagRegistry.entityType(BiomeMakeover.ID("lightning_bug"));
	public static final Tag<EntityType<?>> OWL_TARGETS = TagRegistry.entityType(BiomeMakeover.ID("owl_targets"));
}
