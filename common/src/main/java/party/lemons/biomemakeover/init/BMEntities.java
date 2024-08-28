package party.lemons.biomemakeover.init;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.levelgen.Heightmap;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.block.blockentity.render.TapestryRenderer;
import party.lemons.biomemakeover.entity.*;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorMimicEntity;
import party.lemons.biomemakeover.entity.render.*;
import party.lemons.biomemakeover.entity.render.feature.CowboyHatModel;
import party.lemons.biomemakeover.entity.render.feature.WitchHatModel;
import party.lemons.taniwha.entity.TEntityTypeBuilder;
import party.lemons.taniwha.entity.golem.GolemHandler;
import party.lemons.taniwha.mixin.spawn.SpawnPlacementsInvoker;

public class BMEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Constants.MOD_ID, Registries.ENTITY_TYPE);
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Constants.MOD_ID, Registries.ATTRIBUTE);

    public static final RegistrySupplier<EntityType<TumbleweedEntity>> TUMBLEWEED = ENTITIES.register(BiomeMakeover.ID("tumbleweed"), ()-> TEntityTypeBuilder.of(TumbleweedEntity::new, MobCategory.MISC).sized(0.7F, 0.7F).clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<LightningBottleEntity>> LIGHTNING_BOTTLE = ENTITIES.register(BiomeMakeover.ID("lightning_bottle"), ()->TEntityTypeBuilder.of( (EntityType.EntityFactory<LightningBottleEntity>)LightningBottleEntity::new, MobCategory.MISC).clientTrackingRange(10).updateInterval(4).sized(0.25F, 0.25F).build());

    public static final RegistrySupplier<EntityType<GlowfishEntity>> GLOWFISH = ENTITIES.register(BiomeMakeover.ID("glowfish"), ()->TEntityTypeBuilder.of(GlowfishEntity::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.4F).clientTrackingRange(4).build());
    public static final RegistrySupplier<EntityType<BlightBatEntity>> BLIGHTBAT = ENTITIES.register(BiomeMakeover.ID("blightbat"), ()->TEntityTypeBuilder.of(BlightBatEntity::new, MobCategory.AMBIENT).sized(0.56F, 0.9F).clientTrackingRange(5).build());
    public static final RegistrySupplier<EntityType<MushroomVillagerEntity>> MUSHROOM_TRADER = ENTITIES.register(BiomeMakeover.ID("mushroom_trader"), ()->TEntityTypeBuilder.of(MushroomVillagerEntity::new, MobCategory.AMBIENT).sized(0.6F, 1.95F).clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<ScuttlerEntity>> SCUTTLER = ENTITIES.register(BiomeMakeover.ID("scuttler"), ()->TEntityTypeBuilder.of(ScuttlerEntity::new, MobCategory.CREATURE).sized(0.8F, 0.6F).clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<GhostEntity>> GHOST = ENTITIES.register(BiomeMakeover.ID("ghost"), ()->TEntityTypeBuilder.of(GhostEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).canSpawnFarFromPlayer().clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<CowboyEntity>> COWBOY = ENTITIES.register(BiomeMakeover.ID("cowboy"), ()->TEntityTypeBuilder.of(CowboyEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).canSpawnFarFromPlayer().clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<DecayedEntity>> DECAYED = ENTITIES.register(BiomeMakeover.ID("decayed"), ()->TEntityTypeBuilder.of(DecayedEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build());
    public static final RegistrySupplier<EntityType<DragonflyEntity>> DRAGONFLY = ENTITIES.register(BiomeMakeover.ID("dragonfly"), ()->TEntityTypeBuilder.of(DragonflyEntity::new, MobCategory.AMBIENT).sized(0.8F, 0.6F).clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<ToadEntity>> TOAD = ENTITIES.register(BiomeMakeover.ID("toad"), ()->TEntityTypeBuilder.of(ToadEntity::new, MobCategory.CREATURE).sized(0.8F, 0.6F).clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<TadpoleEntity>> TADPOLE = ENTITIES.register(BiomeMakeover.ID("tadpole"), ()->TEntityTypeBuilder.of(TadpoleEntity::new, MobCategory.WATER_CREATURE).sized(0.5F, 0.3F).clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<LightningBugEntity>> LIGHTNING_BUG = ENTITIES.register(BiomeMakeover.ID("lightning_bug"), ()->TEntityTypeBuilder.of((EntityType.EntityFactory<LightningBugEntity>) LightningBugEntity::new, MobCategory.AMBIENT).sized(0.4F, 0.4F).clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<LightningBugEntity>> LIGHTNING_BUG_ALTERNATE = ENTITIES.register(BiomeMakeover.ID("lightning_bug_alternate"), ()->TEntityTypeBuilder.of((EntityType.EntityFactory<LightningBugEntity>) (entityType, level) -> new LightningBugEntity(level, true), MobCategory.AMBIENT).sized(0.4F, 0.4F).clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<OwlEntity>> OWL = ENTITIES.register(BiomeMakeover.ID("owl"), ()->TEntityTypeBuilder.of(OwlEntity::new, MobCategory.CREATURE).sized(0.7F, 0.8F).clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<MothEntity>> MOTH = ENTITIES.register(BiomeMakeover.ID("moth"), ()->TEntityTypeBuilder.of(MothEntity::new, MobCategory.MONSTER).sized(0.8F, 1.2F).clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<RootlingEntity>> ROOTLING = ENTITIES.register(BiomeMakeover.ID("rootling"), ()->TEntityTypeBuilder.of(RootlingEntity::new, MobCategory.CREATURE).sized(0.6F, 1.1F).clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<AdjudicatorEntity>> ADJUDICATOR = ENTITIES.register(BiomeMakeover.ID("adjudicator"), ()->TEntityTypeBuilder.of(AdjudicatorEntity::new, MobCategory.MONSTER).fireImmune().sized(0.6F, 1.95F).clientTrackingRange(12).noSummon().build());
    public static final RegistrySupplier<EntityType<AdjudicatorMimicEntity>> ADJUDICATOR_MIMIC = ENTITIES.register(BiomeMakeover.ID("adjudicator_mimic"), ()->TEntityTypeBuilder.of(AdjudicatorMimicEntity::new, MobCategory.MONSTER).fireImmune().sized(0.6F, 1.95F).clientTrackingRange(12).noSummon().build());
    public static final RegistrySupplier<EntityType<StoneGolemEntity>> STONE_GOLEM = ENTITIES.register(BiomeMakeover.ID("stone_golem"), ()->TEntityTypeBuilder.of(StoneGolemEntity::new, MobCategory.MISC).sized(1.6F, 2.5F).clientTrackingRange(12).build());
    public static final RegistrySupplier<EntityType<HelmitCrabEntity>> HELMIT_CRAB = ENTITIES.register(BiomeMakeover.ID("helmit_crab"), ()->TEntityTypeBuilder.of(HelmitCrabEntity::new, MobCategory.CREATURE).sized(0.825F, 0.5F).clientTrackingRange(12).build());

    public static final Attribute ATT_PROJECTILE_RESISTANCE = new RangedAttribute("attribute.name.biomemakeover.projectile_resistance", 0.0D, 0.0D, 30.0D);
    public static final RegistrySupplier<Attribute> ATT_PROJECTILE_RESISTANCE_SUPPLIER = ATTRIBUTES.register(BiomeMakeover.ID("projectile_resistance"), () -> ATT_PROJECTILE_RESISTANCE);

    public static void init()
    {
        ENTITIES.register();
        ATTRIBUTES.register();

        LifecycleEvent.SETUP.register(()->{
            GolemHandler.addPattern(
                    BlockPatternBuilder.start().aisle("~^~", "###", "~#~")
                            .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.CARVED_PUMPKIN)))
                            .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(BMBlocks.CLADDED_STONE.get())))
                            .where('~', (block) -> block.getState().isAir()
                            ).build(),
                    BlockPatternBuilder.start().aisle("~ ~", "###", "~#~")
                            .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(BMBlocks.CLADDED_STONE.get())))
                            .where('~', (block) -> block.getState().isAir()
                            ).build(),
                    new GolemHandler.SummonGolemResult<>(BMEntities.STONE_GOLEM.get()));
        });

        initAttributes();
    }

    public static void initSpawnsAndAttributes() {
        initSpawns();
    }


    public static void initAttributes() {
        EntityAttributeRegistry.register(GLOWFISH, GlowfishEntity::createAttributes);
        EntityAttributeRegistry.register(BLIGHTBAT, BlightBatEntity::createAttributes);
        EntityAttributeRegistry.register(MUSHROOM_TRADER, MushroomVillagerEntity::createMobAttributes);
        EntityAttributeRegistry.register(SCUTTLER, ScuttlerEntity::createAttributes);
        EntityAttributeRegistry.register(GHOST, Monster::createMonsterAttributes);
        EntityAttributeRegistry.register(COWBOY, Pillager::createAttributes);
        EntityAttributeRegistry.register(DECAYED, DecayedEntity::createAttributes);
        EntityAttributeRegistry.register(DRAGONFLY, DragonflyEntity::createAttributes);
        EntityAttributeRegistry.register(TOAD, ToadEntity::createAttributes);
        EntityAttributeRegistry.register(TADPOLE, TadpoleEntity::createAttributes);
        EntityAttributeRegistry.register(LIGHTNING_BUG, LightningBugEntity::createAttributes);
        EntityAttributeRegistry.register(LIGHTNING_BUG_ALTERNATE, LightningBugEntity::createAttributes);
        EntityAttributeRegistry.register(OWL, OwlEntity::createAttributes);
        EntityAttributeRegistry.register(MOTH, MothEntity::createAttributes);
        EntityAttributeRegistry.register(ROOTLING, RootlingEntity::createAttributes);
        EntityAttributeRegistry.register(ADJUDICATOR, AdjudicatorEntity::createAttributes);
        EntityAttributeRegistry.register(ADJUDICATOR_MIMIC, AdjudicatorMimicEntity::createAttributes);
        EntityAttributeRegistry.register(STONE_GOLEM, StoneGolemEntity::createAttributes);
        EntityAttributeRegistry.register(HELMIT_CRAB, HelmitCrabEntity::createAttributes);
    }

    private static void initSpawns()
    {
        registerSpawn(BMFeatures.MUSHROOM_FIELD_BIOMES, GLOWFISH.get(), MobCategory.WATER_AMBIENT, 7, 2, 7);
        SpawnPlacementsInvoker.callRegister(GLOWFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);

        //registerSpawn(BMFeatures.MUSHROOM_FIELD_BIOMES, BLIGHTBAT.get(), MobCategory.AMBIENT, 5, 1, 1);
        //SpawnPlacementsInvoker.callRegister(BLIGHTBAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlightBatEntity::checkSpawnRules);

        //registerSpawn(BMFeatures.MUSHROOM_FIELD_BIOMES, MUSHROOM_TRADER.get(), MobCategory.AMBIENT, 1, 1, 1);

        registerSpawn(BMFeatures.BADLANDS_BIOMES, SCUTTLER.get(), MobCategory.CREATURE, 4, 1, 2);
        SpawnPlacementsInvoker.callRegister(SCUTTLER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ScuttlerEntity::checkSpawnRules);
        SpawnPlacementsInvoker.callRegister(GHOST.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GhostEntity::checkGhostSpawnRules);

        registerSpawn(BMFeatures.SWAMP_BIOMES, DECAYED.get(), MobCategory.MONSTER, 60, 1, 1);
        SpawnPlacementsInvoker.callRegister(DECAYED.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DecayedEntity::checkSpawnRules);

        registerSpawn(BMFeatures.SWAMP_BIOMES, DRAGONFLY.get(), MobCategory.AMBIENT, 20, 3, 8);
        SpawnPlacementsInvoker.callRegister(DRAGONFLY.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DragonflyEntity::checkSpawnRules);

        //registerSpawn(SWAMP_BIOMES, TOAD.get(), MobCategory.CREATURE, 20, 2, 4);
        SpawnPlacementsInvoker.callRegister(TOAD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);

        registerSpawn(BMFeatures.SWAMP_BIOMES, LIGHTNING_BUG.get(), MobCategory.AMBIENT, 20, 1, 1);
        SpawnPlacementsInvoker.callRegister(LIGHTNING_BUG.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LightningBugEntity::checkSpawnRules);

        registerSpawn(BMFeatures.DARK_FOREST_BIOMES, OWL.get(), MobCategory.CREATURE, 20, 1, 4);
        SpawnPlacementsInvoker.callRegister(OWL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, OwlEntity::checkSpawnRules);

        registerSpawn(BMFeatures.DARK_FOREST_BIOMES, ROOTLING.get(), MobCategory.CREATURE, 40, 2, 6);
        SpawnPlacementsInvoker.callRegister(ROOTLING.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BMEntities::checkDFSpawnRules);

        registerSpawn(BMFeatures.DARK_FOREST_BIOMES, MOTH.get(), MobCategory.MONSTER, 90, 2, 3);
        SpawnPlacementsInvoker.callRegister(MOTH.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, MothEntity::checkSpawnRules);

        registerSpawn(BMFeatures.DARK_FOREST_BIOMES, EntityType.FOX, MobCategory.CREATURE, 4, 2, 2);
        registerSpawn(BMFeatures.DARK_FOREST_BIOMES, EntityType.RABBIT, MobCategory.CREATURE, 4, 2, 3);

        registerSpawn(BMFeatures.BEACH_BIOMES, HELMIT_CRAB.get(), MobCategory.CREATURE, 6, 2, 5);
        SpawnPlacementsInvoker.callRegister(HELMIT_CRAB.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, HelmitCrabEntity::checkSpawnRules);
    }

    private static boolean checkDFSpawnRules(EntityType<?> type, ServerLevelAccessor level, MobSpawnType mobSpawnType, BlockPos pos, RandomSource randomSource)
    {
        return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) && level.getRawBrightness(pos, 0) > 2;
    }

    public static void registerModelLayers()
    {
        LayerDefinition HUMANOID_OVERLAY = LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5f), 0.0f), 64, 64);
        LayerDefinition HUMANOID_OVERLAY2 = LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5f), 0.0f), 64, 32);

        EntityModelLayerRegistry.register(TapestryRenderer.LAYER_LOCATION, TapestryRenderer::createBodyLayer);
        EntityModelLayerRegistry.register(CowboyHatModel.LAYER_LOCATION, CowboyHatModel::createBodyLayer);
        EntityModelLayerRegistry.register(WitchHatModel.LAYER_LOCATION, WitchHatModel::createBodyLayer);
        EntityModelLayerRegistry.register(BlightBatRender.BlightBatModel.LAYER_LOCATION, BlightBatRender.BlightBatModel::createBodyLayer);
        EntityModelLayerRegistry.register(ScuttlerModel.LAYER_LOCATION, ScuttlerModel::createBodyLayer);
        EntityModelLayerRegistry.register(GhostModel.LAYER_LOCATION, GhostModel::createBodyLayer);
        EntityModelLayerRegistry.register(CowboyRender.LAYER_LOCATION, IllagerModel::createBodyLayer);
        EntityModelLayerRegistry.register(DecayedModel.LAYER_LOCATION, ()->DecayedModel.createBodyLayer(CubeDeformation.NONE));
        EntityModelLayerRegistry.register(DecayedModel.LAYER_LOCATION_2, ()->HUMANOID_OVERLAY);
        EntityModelLayerRegistry.register(DecayedModel.LAYER_LOCATION_3, ()->HUMANOID_OVERLAY2);
        EntityModelLayerRegistry.register(DragonflyModel.LAYER_LOCATION, DragonflyModel::createBodyLayer);
        EntityModelLayerRegistry.register(ToadModel.LAYER_LOCATION, ToadModel::createBodyLayer);
        EntityModelLayerRegistry.register(TadpoleModel.LAYER_LOCATION, TadpoleModel::createBodyLayer);
        EntityModelLayerRegistry.register(LightningBugModel.LAYER_LOCATION, LightningBugModel::createBodyLayer);
        EntityModelLayerRegistry.register(LightningBugModel.LAYER_LOCATION_INNER, LightningBugModel.LightningBugInner::createBodyLayer);
        EntityModelLayerRegistry.register(LightningBugModel.LAYER_LOCATION_OUTER, LightningBugModel.LightningBugOuter::createBodyLayer);
        EntityModelLayerRegistry.register(OwlModel.LAYER_LOCATION, OwlModel::createBodyLayer);
        EntityModelLayerRegistry.register(MothModel.LAYER_LOCATION, MothModel::createBodyLayer);
        EntityModelLayerRegistry.register(RootlingModel.LAYER_LOCATION, RootlingModel::createBodyLayer);
        EntityModelLayerRegistry.register(AdjudicatorModel.LAYER_LOCATION, AdjudicatorModel::createBodyLayer);
        EntityModelLayerRegistry.register(StoneGolemModel.LAYER_LOCATION, StoneGolemModel::createBodyLayer);
        EntityModelLayerRegistry.register(HelmitCrabModel.LAYER_LOCATION, HelmitCrabModel::createBodyLayer);

    }

    private static void registerSpawn(TagKey<Biome> tag, EntityType<?> type, MobCategory category, int weight, int min, int max)
    {
        BiomeModifications.addProperties(b-> b.hasTag(tag), (ctx, b) -> b.getSpawnProperties().addSpawn(category, new MobSpawnSettings.SpawnerData(type, weight, min, max)));
    }

    public static final TagKey<EntityType<?>> LIGHTNING_BUG_TAG = TagKey.create(Registries.ENTITY_TYPE, BiomeMakeover.ID("lightning_bugs"));
    public static final TagKey<EntityType<?>> OWL_TARGETS = TagKey.create(Registries.ENTITY_TYPE, BiomeMakeover.ID("owl_targets"));

    public static final TagKey<DamageType> GHOST_IMMUNE_DAMAGE = TagKey.create(Registries.DAMAGE_TYPE, BiomeMakeover.ID("ghost_immune_to"));
    public static final TagKey<DamageType> SCUTTLER_IMMUNE_DAMAGE = TagKey.create(Registries.DAMAGE_TYPE, BiomeMakeover.ID("scuttler_immune_to"));
    public static final TagKey<DamageType> TUMBLEWEED_IMMUNE_DAMAGE = TagKey.create(Registries.DAMAGE_TYPE, BiomeMakeover.ID("tumbleweed_immune_to"));


}
