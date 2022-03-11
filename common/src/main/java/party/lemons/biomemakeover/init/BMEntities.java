package party.lemons.biomemakeover.init;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
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
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.item.Item;
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
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.block.blockentity.render.TapestryRenderer;
import party.lemons.biomemakeover.entity.*;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorMimicEntity;
import party.lemons.biomemakeover.entity.render.*;
import party.lemons.biomemakeover.entity.render.feature.CowboyHatModel;
import party.lemons.biomemakeover.entity.render.feature.WitchHatModel;
import party.lemons.biomemakeover.level.golem.GolemHandler;
import party.lemons.biomemakeover.mixin.SpawnPlacementsInvoker;
import party.lemons.biomemakeover.util.access.CarvedPumpkinAccess;
import party.lemons.biomemakeover.util.registry.RegistryHelper;
import party.lemons.biomemakeover.util.registry.boat.BoatTypes;

import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BMEntities
{
    static boolean dfCache = SharedConstants.CHECK_DATA_FIXER_SCHEMA;
    static {SharedConstants.CHECK_DATA_FIXER_SCHEMA = false;}
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Constants.MOD_ID, Registry.ENTITY_TYPE_REGISTRY);

    public static final Supplier<EntityType<TumbleweedEntity>> TUMBLEWEED = ENTITIES.register(BiomeMakeover.ID("tumbleweed"), ()->EntityType.Builder.of(TumbleweedEntity::new, MobCategory.MISC).sized(0.7F, 0.7F).clientTrackingRange(12).build("tumbleweed"));
    public static final Supplier<EntityType<BMBoatEntity>> BM_BOAT = ENTITIES.register(BiomeMakeover.ID("bm_boat"), ()->EntityType.Builder.of( (EntityType.EntityFactory<BMBoatEntity>)BMBoatEntity::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(128).updateInterval(3).build("bm_boat"));
    public static final Supplier<EntityType<LightningBottleEntity>> LIGHTNING_BOTTLE = ENTITIES.register(BiomeMakeover.ID("lightning_bottle"), ()->EntityType.Builder.of( (EntityType.EntityFactory<LightningBottleEntity>)LightningBottleEntity::new, MobCategory.MISC).clientTrackingRange(10).updateInterval(4).sized(0.25F, 0.25F).build("lightning_bottle"));

    public static final Supplier<EntityType<GlowfishEntity>> GLOWFISH = ENTITIES.register(BiomeMakeover.ID("glowfish"), ()->EntityType.Builder.of(GlowfishEntity::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.4F).clientTrackingRange(4).build("glowfish"));
    public static final Supplier<EntityType<BlightBatEntity>> BLIGHTBAT = ENTITIES.register(BiomeMakeover.ID("blightbat"), ()->EntityType.Builder.of(BlightBatEntity::new, MobCategory.AMBIENT).sized(0.56F, 0.9F).clientTrackingRange(5).build("blightbat"));
    public static final Supplier<EntityType<MushroomVillagerEntity>> MUSHROOM_TRADER = ENTITIES.register(BiomeMakeover.ID("mushroom_trader"), ()->EntityType.Builder.of(MushroomVillagerEntity::new, MobCategory.AMBIENT).sized(0.6F, 1.95F).clientTrackingRange(12).build("mushroom_trader"));
    public static final Supplier<EntityType<ScuttlerEntity>> SCUTTLER = ENTITIES.register(BiomeMakeover.ID("scuttler"), ()->EntityType.Builder.of(ScuttlerEntity::new, MobCategory.CREATURE).sized(0.8F, 0.6F).clientTrackingRange(12).build("scuttler"));
    public static final Supplier<EntityType<GhostEntity>> GHOST = ENTITIES.register(BiomeMakeover.ID("ghost"), ()->EntityType.Builder.of(GhostEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).canSpawnFarFromPlayer().clientTrackingRange(12).build("ghost"));
    public static final Supplier<EntityType<CowboyEntity>> COWBOY = ENTITIES.register(BiomeMakeover.ID("cowboy"), ()->EntityType.Builder.of(CowboyEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).canSpawnFarFromPlayer().clientTrackingRange(12).build("cowboy"));
    public static final Supplier<EntityType<DecayedEntity>> DECAYED = ENTITIES.register(BiomeMakeover.ID("decayed"), ()->EntityType.Builder.of(DecayedEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build("decayed"));
    public static final Supplier<EntityType<DragonflyEntity>> DRAGONFLY = ENTITIES.register(BiomeMakeover.ID("dragonfly"), ()->EntityType.Builder.of(DragonflyEntity::new, MobCategory.AMBIENT).sized(0.8F, 0.6F).clientTrackingRange(12).build("dragonfly"));
    public static final Supplier<EntityType<ToadEntity>> TOAD = ENTITIES.register(BiomeMakeover.ID("toad"), ()->EntityType.Builder.of(ToadEntity::new, MobCategory.CREATURE).sized(0.8F, 0.6F).clientTrackingRange(12).build("toad"));
    public static final Supplier<EntityType<TadpoleEntity>> TADPOLE = ENTITIES.register(BiomeMakeover.ID("tadpole"), ()->EntityType.Builder.of(TadpoleEntity::new, MobCategory.WATER_CREATURE).sized(0.5F, 0.3F).clientTrackingRange(12).build("tadpole"));
    public static final Supplier<EntityType<LightningBugEntity>> LIGHTNING_BUG = ENTITIES.register(BiomeMakeover.ID("lightning_bug"), ()->EntityType.Builder.of((EntityType.EntityFactory<LightningBugEntity>) LightningBugEntity::new, MobCategory.AMBIENT).sized(0.4F, 0.4F).clientTrackingRange(12).build("lightning_bug"));
    public static final Supplier<EntityType<LightningBugEntity>> LIGHTNING_BUG_ALTERNATE = ENTITIES.register(BiomeMakeover.ID("lightning_bug_alternate"), ()->EntityType.Builder.of((EntityType.EntityFactory<LightningBugEntity>) (entityType, level) -> new LightningBugEntity(level, true), MobCategory.AMBIENT).sized(0.4F, 0.4F).clientTrackingRange(12).build("lightning_bug_alternate"));
    public static final Supplier<EntityType<OwlEntity>> OWL = ENTITIES.register(BiomeMakeover.ID("owl"), ()->EntityType.Builder.of(OwlEntity::new, MobCategory.CREATURE).sized(0.7F, 0.8F).clientTrackingRange(12).build("owl"));
    public static final Supplier<EntityType<MothEntity>> MOTH = ENTITIES.register(BiomeMakeover.ID("moth"), ()->EntityType.Builder.of(MothEntity::new, MobCategory.MONSTER).sized(0.8F, 1.2F).clientTrackingRange(12).build("moth"));
    public static final Supplier<EntityType<RootlingEntity>> ROOTLING = ENTITIES.register(BiomeMakeover.ID("rootling"), ()->EntityType.Builder.of(RootlingEntity::new, MobCategory.CREATURE).sized(0.6F, 1.1F).clientTrackingRange(12).build("rootling"));
    public static final Supplier<EntityType<AdjudicatorEntity>> ADJUDICATOR = ENTITIES.register(BiomeMakeover.ID("adjudicator"), ()->EntityType.Builder.of(AdjudicatorEntity::new, MobCategory.MONSTER).fireImmune().sized(0.6F, 1.95F).clientTrackingRange(12).noSummon().build("adjudicator"));
    public static final Supplier<EntityType<AdjudicatorMimicEntity>> ADJUDICATOR_MIMIC = ENTITIES.register(BiomeMakeover.ID("adjudicator_mimic"), ()->EntityType.Builder.of(AdjudicatorMimicEntity::new, MobCategory.MONSTER).fireImmune().sized(0.6F, 1.95F).clientTrackingRange(12).noSummon().build("adjudicator_mimic"));
    public static final Supplier<EntityType<StoneGolemEntity>> STONE_GOLEM = ENTITIES.register(BiomeMakeover.ID("stone_golem"), ()->EntityType.Builder.of(StoneGolemEntity::new, MobCategory.MISC).sized(1.6F, 2.5F).clientTrackingRange(12).build("stone_golem"));

    static {SharedConstants.CHECK_DATA_FIXER_SCHEMA = dfCache;}

    public static final Attribute ATT_PROJECTILE_RESISTANCE = new RangedAttribute("attribute.name.biomemakeover.projectile_resistance", 0.0D, 0.0D, 30.0D);

    public static void init()
    {
        boolean dfCache = SharedConstants.CHECK_DATA_FIXER_SCHEMA;
        SharedConstants.CHECK_DATA_FIXER_SCHEMA = false;
        ENTITIES.register();
        SharedConstants.CHECK_DATA_FIXER_SCHEMA = dfCache;

        LifecycleEvent.SETUP.register(()->{
            GolemHandler.addPattern(
                    BlockPatternBuilder.start().aisle("~^~", "###", "~#~")
                            .where('^', BlockInWorld.hasState(((CarvedPumpkinAccess)Blocks.CARVED_PUMPKIN).bm_isGolemHeadBlock()))
                            .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(BMBlocks.CLADDED_STONE.get())))
                            .where('~', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR))
                            ).build(),
                    BlockPatternBuilder.start().aisle("~ ~", "###", "~#~")
                            .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(BMBlocks.CLADDED_STONE.get())))
                            .where('~', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR))
                            ).build(),
                    new GolemHandler.SummonGolemResult<>(BMEntities.STONE_GOLEM.get()));
        });

        initAttributes();
    }

    public static void initSpawnsAndAttributes() {
        initSpawns();
    }


    public static void initAttributes() {
        EntityAttributeRegistry.register(GLOWFISH::get, GlowfishEntity::createAttributes);
        EntityAttributeRegistry.register(BLIGHTBAT::get, BlightBatEntity::createAttributes);
        EntityAttributeRegistry.register(MUSHROOM_TRADER::get, MushroomVillagerEntity::createMobAttributes);
        EntityAttributeRegistry.register(SCUTTLER::get, ScuttlerEntity::createAttributes);
        EntityAttributeRegistry.register(GHOST::get, Monster::createMonsterAttributes);
        EntityAttributeRegistry.register(COWBOY::get, Pillager::createAttributes);
        EntityAttributeRegistry.register(DECAYED::get, DecayedEntity::createAttributes);
        EntityAttributeRegistry.register(DRAGONFLY::get, DragonflyEntity::createAttributes);
        EntityAttributeRegistry.register(TOAD::get, ToadEntity::createAttributes);
        EntityAttributeRegistry.register(TADPOLE::get, TadpoleEntity::createAttributes);
        EntityAttributeRegistry.register(LIGHTNING_BUG::get, LightningBugEntity::createAttributes);
        EntityAttributeRegistry.register(LIGHTNING_BUG_ALTERNATE::get, LightningBugEntity::createAttributes);
        EntityAttributeRegistry.register(OWL::get, OwlEntity::createAttributes);
        EntityAttributeRegistry.register(MOTH::get, MothEntity::createAttributes);
        EntityAttributeRegistry.register(ROOTLING::get, RootlingEntity::createAttributes);
        EntityAttributeRegistry.register(ADJUDICATOR::get, AdjudicatorEntity::createAttributes);
        EntityAttributeRegistry.register(ADJUDICATOR_MIMIC::get, AdjudicatorMimicEntity::createAttributes);
        EntityAttributeRegistry.register(STONE_GOLEM::get, StoneGolemEntity::createAttributes);
    }

    private static void initSpawns()
    {
        Predicate<BiomeModifications.BiomeContext> MUSHROOM_BIOMES = (ctx)->ctx.getProperties().getCategory() == Biome.BiomeCategory.MUSHROOM;
        Predicate<BiomeModifications.BiomeContext> MESA_BIOMES = (ctx)->ctx.getProperties().getCategory() == Biome.BiomeCategory.MESA;
        Predicate<BiomeModifications.BiomeContext> SWAMP_BIOMES = (ctx)->ctx.getProperties().getCategory() == Biome.BiomeCategory.SWAMP;
        Predicate<BiomeModifications.BiomeContext> DARK_FOREST = (ctx)->ctx.getKey().equals(new ResourceLocation("dark_forest"));

        registerSpawn(MUSHROOM_BIOMES, GLOWFISH.get(), MobCategory.WATER_AMBIENT, 7, 2, 7);
        SpawnPlacementsInvoker.callRegister(GLOWFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);

        registerSpawn(MUSHROOM_BIOMES, BLIGHTBAT.get(), MobCategory.AMBIENT, 5, 1, 1);
        SpawnPlacementsInvoker.callRegister(BLIGHTBAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlightBatEntity::checkSpawnRules);

        registerSpawn(MUSHROOM_BIOMES, MUSHROOM_TRADER.get(), MobCategory.AMBIENT, 1, 1, 1);

        registerSpawn(MESA_BIOMES, SCUTTLER.get(), MobCategory.CREATURE, 4, 1, 2);
        SpawnPlacementsInvoker.callRegister(SCUTTLER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ScuttlerEntity::checkSpawnRules);

        registerSpawn(SWAMP_BIOMES, DECAYED.get(), MobCategory.MONSTER, 60, 1, 1);
        SpawnPlacementsInvoker.callRegister(DECAYED.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DecayedEntity::checkSpawnRules);

        registerSpawn(SWAMP_BIOMES, DRAGONFLY.get(), MobCategory.AMBIENT, 20, 3, 8);
        SpawnPlacementsInvoker.callRegister(DRAGONFLY.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DragonflyEntity::checkSpawnRules);

        registerSpawn(SWAMP_BIOMES, TOAD.get(), MobCategory.CREATURE, 20, 2, 4);
        SpawnPlacementsInvoker.callRegister(TOAD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);

        registerSpawn(SWAMP_BIOMES, LIGHTNING_BUG.get(), MobCategory.AMBIENT, 20, 1, 1);
        SpawnPlacementsInvoker.callRegister(LIGHTNING_BUG.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LightningBugEntity::checkSpawnRules);

        registerSpawn(DARK_FOREST, OWL.get(), MobCategory.CREATURE, 20, 1, 4);
        SpawnPlacementsInvoker.callRegister(OWL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, OwlEntity::checkSpawnRules);

        registerSpawn(DARK_FOREST, ROOTLING.get(), MobCategory.CREATURE, 40, 2, 6);
        SpawnPlacementsInvoker.callRegister(ROOTLING.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BMEntities::checkDFSpawnRules);

        registerSpawn(DARK_FOREST, MOTH.get(), MobCategory.MONSTER, 90, 2, 3);
        SpawnPlacementsInvoker.callRegister(MOTH.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, MothEntity::checkSpawnRules);

        registerSpawn(DARK_FOREST, EntityType.FOX, MobCategory.CREATURE, 4, 2, 2);
        registerSpawn(DARK_FOREST, EntityType.RABBIT, MobCategory.CREATURE, 4, 2, 3);
    }

    private static <T extends Mob> boolean checkDFSpawnRules(EntityType<T> tEntityType, ServerLevelAccessor level, MobSpawnType mobSpawnType, BlockPos pos, Random random)
    {
        return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) && level.getRawBrightness(pos, 0) > 2;
    }

    public static void registerModels()
    {
        EnvExecutor.runInEnv(Env.CLIENT, ()->()->{

            EntityRendererRegistry.register(BMEntities.TUMBLEWEED::get, TumbleweedRender::new);
            EntityRendererRegistry.register(BMEntities.BM_BOAT::get, BMBoatRender::new);
            EntityRendererRegistry.register(BMEntities.LIGHTNING_BOTTLE::get, ThrownItemRenderer::new);

            EntityRendererRegistry.register(BMEntities.GLOWFISH::get, GlowfishRender::new);
            EntityRendererRegistry.register(BMEntities.BLIGHTBAT::get, BlightBatRender::new);
            EntityRendererRegistry.register(BMEntities.MUSHROOM_TRADER::get, MushroomTraderRender::new);
            EntityRendererRegistry.register(BMEntities.SCUTTLER::get, ScuttlerRender::new);
            EntityRendererRegistry.register(BMEntities.GHOST::get, GhostRender::new);
            EntityRendererRegistry.register(BMEntities.COWBOY::get, CowboyRender::new);
            EntityRendererRegistry.register(BMEntities.DECAYED::get, DecayedRender::new);
            EntityRendererRegistry.register(BMEntities.DRAGONFLY::get, DragonflyRender::new);
            EntityRendererRegistry.register(BMEntities.TOAD::get, ToadRender::new);
            EntityRendererRegistry.register(BMEntities.TADPOLE::get, TadpoleRender::new);
            EntityRendererRegistry.register(BMEntities.LIGHTNING_BUG::get, LightningBugRender::new);
            EntityRendererRegistry.register(BMEntities.LIGHTNING_BUG_ALTERNATE::get, LightningBugRender::new);
            EntityRendererRegistry.register(BMEntities.OWL::get, OwlRender::new);
            EntityRendererRegistry.register(BMEntities.MOTH::get, MothRender::new);
            EntityRendererRegistry.register(BMEntities.ROOTLING::get, RootlingRender::new);
            EntityRendererRegistry.register(BMEntities.ADJUDICATOR::get, AdjudicatorRender::new);
            EntityRendererRegistry.register(BMEntities.ADJUDICATOR_MIMIC::get, AdjudicatorMimicRender::new);
            EntityRendererRegistry.register(BMEntities.STONE_GOLEM::get, StoneGolemRender::new);
        });
    }

    public static void registerModelLayers()
    {
        BoatTypes.registerModelLayers();

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

    }

    private static void registerSpawn(Predicate<BiomeModifications.BiomeContext> biomes, EntityType<?> type, MobCategory category, int weight, int min, int max)
    {
        BiomeModifications.addProperties(biomes, (ctx, p)->p.getSpawnProperties().addSpawn(category, new MobSpawnSettings.SpawnerData(type, weight, min, max)));
    }

    public static final TagKey<EntityType<?>> LIGHTNING_BUG_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, BiomeMakeover.ID("lightning_bugs"));
    public static final TagKey<EntityType<?>> OWL_TARGETS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, BiomeMakeover.ID("owl_targets"));


}
