package party.lemons.biomemakeover.init;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.SharedConstants;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.level.block.state.predicate.BlockMaterialPredicate;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Material;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.block.blockentity.render.TapestryRenderer;
import party.lemons.biomemakeover.entity.*;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorMimicEntity;
import party.lemons.biomemakeover.entity.render.*;
import party.lemons.biomemakeover.entity.render.feature.CowboyHatModel;
import party.lemons.biomemakeover.entity.render.feature.WitchHatModel;
import party.lemons.biomemakeover.level.golem.GolemHandler;
import party.lemons.biomemakeover.mixin.EntityTypeTagsInvoker;
import party.lemons.biomemakeover.mixin.SpawnPlacementsInvoker;
import party.lemons.biomemakeover.util.access.CarvedPumpkinAccess;
import party.lemons.biomemakeover.util.registry.RegistryHelper;
import party.lemons.biomemakeover.util.registry.boat.BoatTypes;
import party.lemons.biomemakeover.util.registry.modellayer.ModelLayerRegistry;

import java.util.Random;
import java.util.function.Predicate;

public class BMEntities
{
    static boolean dfCache = SharedConstants.CHECK_DATA_FIXER_SCHEMA;
    static {SharedConstants.CHECK_DATA_FIXER_SCHEMA = false;}

    public static final EntityType<TumbleweedEntity> TUMBLEWEED = EntityType.Builder.of(TumbleweedEntity::new, MobCategory.MISC).sized(0.7F, 0.7F).clientTrackingRange(12).build("tumbleweed");
    public static final EntityType<BMBoatEntity> BM_BOAT = EntityType.Builder.of( (EntityType.EntityFactory<BMBoatEntity>)BMBoatEntity::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(128).updateInterval(3).build("bm_boat");
    public static final EntityType<LightningBottleEntity> LIGHTNING_BOTTLE = EntityType.Builder.of( (EntityType.EntityFactory<LightningBottleEntity>)LightningBottleEntity::new, MobCategory.MISC).clientTrackingRange(10).updateInterval(4).sized(0.25F, 0.25F).build("lightning_bottle");

    public static final EntityType<GlowfishEntity> GLOWFISH = EntityType.Builder.of(GlowfishEntity::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.4F).clientTrackingRange(4).build("glowfish");
    public static final EntityType<BlightBatEntity> BLIGHTBAT = EntityType.Builder.of(BlightBatEntity::new, MobCategory.AMBIENT).sized(0.56F, 0.9F).clientTrackingRange(5).build("blightbat");
    public static final EntityType<MushroomVillagerEntity> MUSHROOM_TRADER = EntityType.Builder.of(MushroomVillagerEntity::new, MobCategory.AMBIENT).sized(0.6F, 1.95F).clientTrackingRange(12).build("mushroom_trader");
    public static final EntityType<ScuttlerEntity> SCUTTLER = EntityType.Builder.of(ScuttlerEntity::new, MobCategory.CREATURE).sized(0.8F, 0.6F).clientTrackingRange(12).build("scuttler");
    public static final EntityType<GhostEntity> GHOST = EntityType.Builder.of(GhostEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).canSpawnFarFromPlayer().clientTrackingRange(12).build("ghost");
    public static final EntityType<CowboyEntity> COWBOY = EntityType.Builder.of(CowboyEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).canSpawnFarFromPlayer().clientTrackingRange(12).build("cowboy");
    public static final EntityType<DecayedEntity> DECAYED = EntityType.Builder.of(DecayedEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build("decayed");
    public static final EntityType<DragonflyEntity> DRAGONFLY = EntityType.Builder.of(DragonflyEntity::new, MobCategory.AMBIENT).sized(0.8F, 0.6F).clientTrackingRange(12).build("dragonfly");
    public static final EntityType<ToadEntity> TOAD = EntityType.Builder.of(ToadEntity::new, MobCategory.CREATURE).sized(0.8F, 0.6F).clientTrackingRange(12).build("toad");
    public static final EntityType<TadpoleEntity> TADPOLE = EntityType.Builder.of(TadpoleEntity::new, MobCategory.WATER_CREATURE).sized(0.5F, 0.3F).clientTrackingRange(12).build("tadpole");
    public static final EntityType<LightningBugEntity> LIGHTNING_BUG = EntityType.Builder.of((EntityType.EntityFactory<LightningBugEntity>) LightningBugEntity::new, MobCategory.AMBIENT).sized(0.4F, 0.4F).clientTrackingRange(12).build("lightning_bug");
    public static final EntityType<LightningBugEntity> LIGHTNING_BUG_ALTERNATE = EntityType.Builder.of((EntityType.EntityFactory<LightningBugEntity>) (entityType, level) -> new LightningBugEntity(level, true), MobCategory.AMBIENT).sized(0.4F, 0.4F).clientTrackingRange(12).build("lightning_bug_alternate");
    public static final EntityType<OwlEntity> OWL = EntityType.Builder.of(OwlEntity::new, MobCategory.CREATURE).sized(0.7F, 0.8F).clientTrackingRange(12).build("owl");
    public static final EntityType<MothEntity> MOTH = EntityType.Builder.of(MothEntity::new, MobCategory.MONSTER).sized(0.8F, 1.2F).clientTrackingRange(12).build("moth");
    public static final EntityType<RootlingEntity> ROOTLING = EntityType.Builder.of(RootlingEntity::new, MobCategory.CREATURE).sized(0.6F, 1.1F).clientTrackingRange(12).build("rootling");
    public static final EntityType<AdjudicatorEntity> ADJUDICATOR = EntityType.Builder.of(AdjudicatorEntity::new, MobCategory.MONSTER).fireImmune().sized(0.6F, 1.95F).clientTrackingRange(12).noSummon().build("adjudicator");
    public static final EntityType<AdjudicatorMimicEntity> ADJUDICATOR_MIMIC = EntityType.Builder.of(AdjudicatorMimicEntity::new, MobCategory.MONSTER).fireImmune().sized(0.6F, 1.95F).clientTrackingRange(12).noSummon().build("adjudicator_mimic");
    public static final EntityType<StoneGolemEntity> STONE_GOLEM = EntityType.Builder.of(StoneGolemEntity::new, MobCategory.MISC).sized(1.6F, 2.5F).clientTrackingRange(12).build("stone_golem");

    static {SharedConstants.CHECK_DATA_FIXER_SCHEMA = dfCache;}

    public static final Attribute ATT_PROJECTILE_RESISTANCE = new RangedAttribute("attribute.name.biomemakeover.projectile_resistance", 0.0D, 0.0D, 30.0D);

    public static void init()
    {
        boolean dfCache = SharedConstants.CHECK_DATA_FIXER_SCHEMA;
        SharedConstants.CHECK_DATA_FIXER_SCHEMA = false;
        RegistryHelper.register(Constants.MOD_ID, Registry.ENTITY_TYPE, EntityType.class, BMEntities.class);
        SharedConstants.CHECK_DATA_FIXER_SCHEMA = dfCache;

        initSpawns();
        initAttributes();


        GolemHandler.addPattern(
                BlockPatternBuilder.start().aisle("~^~", "###", "~#~")
                        .where('^', BlockInWorld.hasState(((CarvedPumpkinAccess)Blocks.CARVED_PUMPKIN).bm_isGolemHeadBlock()))
                        .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(BMBlocks.CLADDED_STONE)))
                        .where('~', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR))
        ).build(),
                BlockPatternBuilder.start().aisle("~ ~", "###", "~#~")
                        .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(BMBlocks.CLADDED_STONE)))
                        .where('~', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR))
                        ).build(),
                new GolemHandler.SummonGolemResult<>(STONE_GOLEM));
    }

    public static void initAttributes() {
        EntityAttributeRegistry.register(() -> GLOWFISH, GlowfishEntity::createAttributes);
        EntityAttributeRegistry.register(() -> BLIGHTBAT, BlightBatEntity::createAttributes);
        EntityAttributeRegistry.register(() -> MUSHROOM_TRADER, MushroomVillagerEntity::createMobAttributes);
        EntityAttributeRegistry.register(() -> SCUTTLER, ScuttlerEntity::createAttributes);
        EntityAttributeRegistry.register(() -> GHOST, Monster::createMonsterAttributes);
        EntityAttributeRegistry.register(() -> COWBOY, Pillager::createAttributes);
        EntityAttributeRegistry.register(() -> DECAYED, DecayedEntity::createAttributes);
        EntityAttributeRegistry.register(() -> DRAGONFLY, DragonflyEntity::createAttributes);
        EntityAttributeRegistry.register(() -> TOAD, ToadEntity::createAttributes);
        EntityAttributeRegistry.register(() -> TADPOLE, TadpoleEntity::createAttributes);
        EntityAttributeRegistry.register(() -> LIGHTNING_BUG, LightningBugEntity::createAttributes);
        EntityAttributeRegistry.register(() -> LIGHTNING_BUG_ALTERNATE, LightningBugEntity::createAttributes);
        EntityAttributeRegistry.register(() -> OWL, OwlEntity::createAttributes);
        EntityAttributeRegistry.register(() -> MOTH, MothEntity::createAttributes);
        EntityAttributeRegistry.register(() -> ROOTLING, RootlingEntity::createAttributes);
        EntityAttributeRegistry.register(() -> ADJUDICATOR, AdjudicatorEntity::createAttributes);
        EntityAttributeRegistry.register(() -> ADJUDICATOR_MIMIC, AdjudicatorMimicEntity::createAttributes);
        EntityAttributeRegistry.register(() -> STONE_GOLEM, StoneGolemEntity::createAttributes);
    }

    private static void initSpawns()
    {
        Predicate<BiomeModifications.BiomeContext> MUSHROOM_BIOMES = (ctx)->ctx.getProperties().getCategory() == Biome.BiomeCategory.MUSHROOM;
        Predicate<BiomeModifications.BiomeContext> MESA_BIOMES = (ctx)->ctx.getProperties().getCategory() == Biome.BiomeCategory.MESA;
        Predicate<BiomeModifications.BiomeContext> SWAMP_BIOMES = (ctx)->ctx.getProperties().getCategory() == Biome.BiomeCategory.SWAMP;
        Predicate<BiomeModifications.BiomeContext> DARK_FOREST = (ctx)->ctx.getKey().equals(new ResourceLocation("dark_forest"));

        registerSpawn(MUSHROOM_BIOMES, GLOWFISH, MobCategory.WATER_AMBIENT, 7, 2, 7);
        SpawnPlacementsInvoker.callRegister(GLOWFISH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);

        registerSpawn(MUSHROOM_BIOMES, BLIGHTBAT, MobCategory.AMBIENT, 5, 1, 1);
        SpawnPlacementsInvoker.callRegister(BLIGHTBAT, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlightBatEntity::checkSpawnRules);

        registerSpawn(MUSHROOM_BIOMES, MUSHROOM_TRADER, MobCategory.AMBIENT, 1, 1, 1);

        registerSpawn(MESA_BIOMES, SCUTTLER, MobCategory.CREATURE, 4, 1, 2);
        SpawnPlacementsInvoker.callRegister(SCUTTLER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ScuttlerEntity::checkSpawnRules);

        registerSpawn(SWAMP_BIOMES, DECAYED, MobCategory.MONSTER, 60, 1, 1);
        SpawnPlacementsInvoker.callRegister(DECAYED, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DecayedEntity::checkSpawnRules);

        registerSpawn(SWAMP_BIOMES, DRAGONFLY, MobCategory.AMBIENT, 20, 3, 8);
        SpawnPlacementsInvoker.callRegister(DRAGONFLY, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DragonflyEntity::checkSpawnRules);

        registerSpawn(SWAMP_BIOMES, TOAD, MobCategory.CREATURE, 20, 2, 4);
        SpawnPlacementsInvoker.callRegister(TOAD, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);

        registerSpawn(SWAMP_BIOMES, LIGHTNING_BUG, MobCategory.AMBIENT, 20, 1, 1);
        SpawnPlacementsInvoker.callRegister(LIGHTNING_BUG, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LightningBugEntity::checkSpawnRules);

        registerSpawn(DARK_FOREST, OWL, MobCategory.CREATURE, 20, 1, 4);
        SpawnPlacementsInvoker.callRegister(OWL, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, OwlEntity::checkSpawnRules);

        registerSpawn(DARK_FOREST, ROOTLING, MobCategory.CREATURE, 20, 2, 6);
        SpawnPlacementsInvoker.callRegister(ROOTLING, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BMEntities::checkDFSpawnRules);

        registerSpawn(DARK_FOREST, MOTH, MobCategory.MONSTER, 90, 2, 3);
        SpawnPlacementsInvoker.callRegister(MOTH, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, MothEntity::checkSpawnRules);

    }

    private static <T extends Mob> boolean checkDFSpawnRules(EntityType<T> tEntityType, ServerLevelAccessor level, MobSpawnType mobSpawnType, BlockPos pos, Random random)
    {
        return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) && level.getRawBrightness(pos, 0) > 2;
    }

    public static void registerModels()
    {
        EnvExecutor.runInEnv(Env.CLIENT, ()->()->{

            EntityRendererRegistry.register(() -> BMEntities.TUMBLEWEED, TumbleweedRender::new);
            EntityRendererRegistry.register(() -> BMEntities.BM_BOAT, BMBoatRender::new);
            EntityRendererRegistry.register(() -> BMEntities.LIGHTNING_BOTTLE, ThrownItemRenderer::new);

            EntityRendererRegistry.register(() -> BMEntities.GLOWFISH, GlowfishRender::new);
            EntityRendererRegistry.register(() -> BMEntities.BLIGHTBAT, BlightBatRender::new);
            EntityRendererRegistry.register(() -> BMEntities.MUSHROOM_TRADER, MushroomTraderRender::new);
            EntityRendererRegistry.register(() -> BMEntities.SCUTTLER, ScuttlerRender::new);
            EntityRendererRegistry.register(() -> BMEntities.GHOST, GhostRender::new);
            EntityRendererRegistry.register(() -> BMEntities.COWBOY, CowboyRender::new);
            EntityRendererRegistry.register(() -> BMEntities.DECAYED, DecayedRender::new);
            EntityRendererRegistry.register(() -> BMEntities.DRAGONFLY, DragonflyRender::new);
            EntityRendererRegistry.register(() -> BMEntities.TOAD, ToadRender::new);
            EntityRendererRegistry.register(() -> BMEntities.TADPOLE, TadpoleRender::new);
            EntityRendererRegistry.register(() -> BMEntities.LIGHTNING_BUG, LightningBugRender::new);
            EntityRendererRegistry.register(() -> BMEntities.LIGHTNING_BUG_ALTERNATE, LightningBugRender::new);
            EntityRendererRegistry.register(() -> BMEntities.OWL, OwlRender::new);
            EntityRendererRegistry.register(() -> BMEntities.MOTH, MothRender::new);
            EntityRendererRegistry.register(() -> BMEntities.ROOTLING, RootlingRender::new);
            EntityRendererRegistry.register(() -> BMEntities.ADJUDICATOR, AdjudicatorRender::new);
            EntityRendererRegistry.register(() -> BMEntities.ADJUDICATOR_MIMIC, AdjudicatorMimicRender::new);
            EntityRendererRegistry.register(() -> BMEntities.STONE_GOLEM, StoneGolemRender::new);
        });
    }

    public static void registerModelLayers()
    {
        BoatTypes.registerModelLayers();

        LayerDefinition HUMANOID_OVERLAY = LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5f), 0.0f), 64, 64);
        LayerDefinition HUMANOID_OVERLAY2 = LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5f), 0.0f), 64, 32);

        ModelLayerRegistry.registerModelLayer(TapestryRenderer.LAYER_LOCATION, TapestryRenderer::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(CowboyHatModel.LAYER_LOCATION, CowboyHatModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(WitchHatModel.LAYER_LOCATION, WitchHatModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(BlightBatRender.BlightBatModel.LAYER_LOCATION, BlightBatRender.BlightBatModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(ScuttlerModel.LAYER_LOCATION, ScuttlerModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(GhostModel.LAYER_LOCATION, GhostModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(CowboyRender.LAYER_LOCATION, IllagerModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(DecayedModel.LAYER_LOCATION, ()->DecayedModel.createBodyLayer(CubeDeformation.NONE));
        ModelLayerRegistry.registerModelLayer(DecayedModel.LAYER_LOCATION_2, ()->HUMANOID_OVERLAY);
        ModelLayerRegistry.registerModelLayer(DecayedModel.LAYER_LOCATION_3, ()->HUMANOID_OVERLAY2);
        ModelLayerRegistry.registerModelLayer(DragonflyModel.LAYER_LOCATION, DragonflyModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(ToadModel.LAYER_LOCATION, ToadModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(TadpoleModel.LAYER_LOCATION, TadpoleModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(LightningBugModel.LAYER_LOCATION, LightningBugModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(LightningBugModel.LAYER_LOCATION_INNER, LightningBugModel.LightningBugInner::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(LightningBugModel.LAYER_LOCATION_OUTER, LightningBugModel.LightningBugOuter::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(OwlModel.LAYER_LOCATION, OwlModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(MothModel.LAYER_LOCATION, MothModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(RootlingModel.LAYER_LOCATION, RootlingModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(AdjudicatorModel.LAYER_LOCATION, AdjudicatorModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(StoneGolemModel.LAYER_LOCATION, StoneGolemModel::createBodyLayer);

    }

    private static void registerSpawn(Predicate<BiomeModifications.BiomeContext> biomes, EntityType<?> type, MobCategory category, int weight, int min, int max)
    {
        BiomeModifications.addProperties(biomes, (ctx, p)->p.getSpawnProperties().addSpawn(category, new MobSpawnSettings.SpawnerData(type, weight, min, max)));
    }

    public static final Tag<EntityType<?>> LIGHTNING_BUG_TAG = EntityTypeTagsInvoker.callBind("biomemakeover:lightning_bugs");
    public static final Tag<EntityType<?>> OWL_TARGETS = EntityTypeTagsInvoker.callBind("biomemakeover:owl_targets");
}
