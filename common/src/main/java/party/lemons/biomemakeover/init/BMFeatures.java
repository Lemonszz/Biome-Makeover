package party.lemons.biomemakeover.init;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.level.feature.*;
import party.lemons.biomemakeover.level.feature.foliage.HangingLeavesDecorator;
import party.lemons.biomemakeover.level.feature.foliage.IvyDecorator;
import party.lemons.biomemakeover.level.feature.foliage.WillowFoliagePlacer;
import party.lemons.biomemakeover.level.feature.foliage.WillowingBranchDecorator;
import party.lemons.biomemakeover.level.feature.mansion.MansionFeature;

import java.util.function.Supplier;

public class BMFeatures {
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Constants.MOD_ID, Registry.FEATURE_REGISTRY);
    private static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE = DeferredRegister.create(Constants.MOD_ID, Registry.FOLIAGE_PLACER_TYPE_REGISTRY);
    private static final DeferredRegister<TreeDecoratorType<?>> DECORATOR = DeferredRegister.create(Constants.MOD_ID, Registry.TREE_DECORATOR_TYPE_REGISTRY);
    private static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(Constants.MOD_ID, Registry.STRUCTURE_FEATURE_REGISTRY);

    public static final Supplier<Feature<BlockStateConfiguration>> MESMERMITE_BOULDER_FEATURE = FEATURES.register(BiomeMakeover.ID("mesmerite_boulder_feature"), ()->new MesmeriteBoulderFeature(BlockStateConfiguration.CODEC));
    public static final Supplier<Feature<OreConfiguration>> MESMERITE_UNDERGROUND_FEATURE = FEATURES.register(BiomeMakeover.ID("mesmerite_underground_feature"), ()->new MesmermiteUndergroundFeature(OreConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> ITCHING_IVY_FEATURE = FEATURES.register(BiomeMakeover.ID("itching_ivy_feature"), ()->new ItchingIvyFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<TreeConfiguration>> WATER_TREE = FEATURES.register(BiomeMakeover.ID("water_tree"), ()->new WaterTreeFeature(TreeConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> PAYDIRT_FEATURE = FEATURES.register(BiomeMakeover.ID("paydirt_feature"), ()->new PaydirtFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> SAGUARO_CACTUS_FEATURE = FEATURES.register(BiomeMakeover.ID("saguaro_cactus_feature"), ()->new SaguaroCactusFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> SURFACE_FOSSIL_FEATURE = FEATURES.register(BiomeMakeover.ID("surface_fossil_feature"), ()->new SurfaceFossilFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> HUGE_PURPLE_GLOWSHROOM_CONFIG = FEATURES.register(BiomeMakeover.ID("huge_purple_glowshroom_config"), ()->new HugePurpleGlowshroomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> HUGE_GREEN_GLOWSHROOM_CONFIG = FEATURES.register(BiomeMakeover.ID("huge_green_glowshroom_config"), ()->new HugeGreenGlowshroomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> HUGE_ORANGE_GLOWSHROOM_FEATURE = FEATURES.register(BiomeMakeover.ID("huge_orange_glowshroom_feature"), ()->new HugeOrangeGlowshroomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final Supplier<Feature<ProbabilityFeatureConfiguration>> ORANGE_GLOWSHROOM_FEATURE = FEATURES.register(BiomeMakeover.ID("orange_glowshroom_feature"), ()->new OrangeGlowshroomFeature(ProbabilityFeatureConfiguration.CODEC));
    public static final Supplier<Feature<VegetationPatchConfiguration>> GRASS_PATCH = FEATURES.register(BiomeMakeover.ID("grass_patch"), ()->new GrassPatchFeature(VegetationPatchConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> PEAT_FEATURE = FEATURES.register(BiomeMakeover.ID("peat_feature"), ()->new PeatFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> REED_FEATURE =  FEATURES.register(BiomeMakeover.ID("reed_feature"), ()->new ReedFeature(NoneFeatureConfiguration.CODEC));

    public static final Supplier<FoliagePlacerType<WillowFoliagePlacer>> WILLOW_FOLIAGE = FOLIAGE.register(BiomeMakeover.ID("willow_foliage"), ()->new FoliagePlacerType<>(WillowFoliagePlacer.CODEC));
    public static final Supplier<TreeDecoratorType<HangingLeavesDecorator>> HANGING_LEAVES_DECORATOR = DECORATOR.register(BiomeMakeover.ID("hanging_leaves_decorator"), ()->new TreeDecoratorType<>(HangingLeavesDecorator.CODEC));
    public static final Supplier<TreeDecoratorType<WillowingBranchDecorator>> WILLOWING_BRANCH_DECORATOR = DECORATOR.register(BiomeMakeover.ID("willowing_brand_decorator"), ()->new TreeDecoratorType<>(WillowingBranchDecorator.CODEC));
    public static final Supplier<TreeDecoratorType<IvyDecorator>> IVY_DECORATOR =  DECORATOR.register(BiomeMakeover.ID("ivy_decorator"), ()->new TreeDecoratorType<>(IvyDecorator.CODEC));

    public static final Supplier<StructureFeature<SunkenRuinFeature.SunkenRuinFeatureConfig>> SUNKEN_RUIN = STRUCTURES.register(BiomeMakeover.ID("sunken_ruin"), ()->new SunkenRuinFeature(SunkenRuinFeature.SunkenRuinFeatureConfig.CODEC));
    public static final Supplier<StructureFeature<JigsawConfiguration>> GHOST_TOWN = STRUCTURES.register(BiomeMakeover.ID("ghost_town"), ()->new GhostTownFeature(JigsawConfiguration.CODEC));
    public static final Supplier<StructureFeature<NoneFeatureConfiguration>> MANSION = STRUCTURES.register(BiomeMakeover.ID("mansion"), ()->new MansionFeature(NoneFeatureConfiguration.CODEC));


    public static void init()
    {
        FEATURES.register();
        FOLIAGE.register();
        DECORATOR.register();
        STRUCTURES.register();
    }
}
