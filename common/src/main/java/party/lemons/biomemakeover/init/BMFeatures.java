package party.lemons.biomemakeover.init;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.level.feature.*;
import party.lemons.biomemakeover.level.feature.foliage.*;
import party.lemons.biomemakeover.level.feature.foliage.WillowTrunkPlacer;

import java.util.function.Supplier;

public class BMFeatures {
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Constants.MOD_ID, Registries.FEATURE);
    private static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE = DeferredRegister.create(Constants.MOD_ID, Registries.FOLIAGE_PLACER_TYPE);
    private static final DeferredRegister<TreeDecoratorType<?>> DECORATOR = DeferredRegister.create(Constants.MOD_ID, Registries.TREE_DECORATOR_TYPE);
    private static final DeferredRegister<TrunkPlacerType<?>> TRUNK = DeferredRegister.create(Constants.MOD_ID, Registries.TRUNK_PLACER_TYPE);

    public static final Supplier<Feature<BlockStateConfiguration>> MESMERMITE_BOULDER_FEATURE = FEATURES.register(BiomeMakeover.ID("mesmerite_boulder"), ()->new MesmeriteBoulderFeature(BlockStateConfiguration.CODEC));
    public static final Supplier<Feature<OreConfiguration>> MESMERITE_UNDERGROUND_FEATURE = FEATURES.register(BiomeMakeover.ID("mesmerite_underground"), ()->new MesmermiteUndergroundFeature(OreConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> ITCHING_IVY_FEATURE = FEATURES.register(BiomeMakeover.ID("itching_ivy"), ()->new ItchingIvyFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<TreeConfiguration>> WATER_TREE = FEATURES.register(BiomeMakeover.ID("water_tree"), ()->new WaterTreeFeature(TreeConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> PAYDIRT_FEATURE = FEATURES.register(BiomeMakeover.ID("paydirt"), ()->new PaydirtFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> SAGUARO_CACTUS_FEATURE = FEATURES.register(BiomeMakeover.ID("saguaro_cactus"), ()->new SaguaroCactusFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> SURFACE_FOSSIL_FEATURE = FEATURES.register(BiomeMakeover.ID("surface_fossil"), ()->new SurfaceFossilFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> HUGE_PURPLE_GLOWSHROOM_CONFIG = FEATURES.register(BiomeMakeover.ID("huge_purple_glowshroom"), ()->new HugePurpleGlowshroomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> HUGE_GREEN_GLOWSHROOM_CONFIG = FEATURES.register(BiomeMakeover.ID("huge_green_glowshroom"), ()->new HugeGreenGlowshroomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final Supplier<Feature<HugeMushroomFeatureConfiguration>> HUGE_ORANGE_GLOWSHROOM_FEATURE = FEATURES.register(BiomeMakeover.ID("huge_orange_glowshroom"), ()->new HugeOrangeGlowshroomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static final Supplier<Feature<ProbabilityFeatureConfiguration>> ORANGE_GLOWSHROOM_FEATURE = FEATURES.register(BiomeMakeover.ID("orange_glowshroom"), ()->new OrangeGlowshroomFeature(ProbabilityFeatureConfiguration.CODEC));
    public static final Supplier<Feature<VegetationPatchConfiguration>> GRASS_PATCH = FEATURES.register(BiomeMakeover.ID("grass_patch"), ()->new GrassPatchFeature(VegetationPatchConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> PEAT_FEATURE = FEATURES.register(BiomeMakeover.ID("peat"), ()->new PeatFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> REED_FEATURE =  FEATURES.register(BiomeMakeover.ID("reeds"), ()->new ReedFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<FissureFeature.FissureConfig>> FISSURE =  FEATURES.register(BiomeMakeover.ID("fissure"), ()->new FissureFeature(FissureFeature.FissureConfig.CODEC));

    public static final Supplier<FoliagePlacerType<WillowFoliagePlacer>> WILLOW_FOLIAGE = FOLIAGE.register(BiomeMakeover.ID("willow_foliage"), ()->new FoliagePlacerType<>(WillowFoliagePlacer.CODEC));
    public static final Supplier<TreeDecoratorType<HangingLeavesDecorator>> HANGING_LEAVES_DECORATOR = DECORATOR.register(BiomeMakeover.ID("hanging_leaves_decorator"), ()->new TreeDecoratorType<>(HangingLeavesDecorator.CODEC));
    public static final Supplier<TreeDecoratorType<WillowingBranchDecorator>> WILLOWING_BRANCH_DECORATOR = DECORATOR.register(BiomeMakeover.ID("willowing_branch_decorator"), ()->new TreeDecoratorType<>(WillowingBranchDecorator.CODEC));
    public static final Supplier<TreeDecoratorType<IvyDecorator>> IVY_DECORATOR =  DECORATOR.register(BiomeMakeover.ID("ivy"), ()->new TreeDecoratorType<>(IvyDecorator.CODEC));

    public static final TagKey<Biome> HAS_REWORKED_MANSION = TagKey.create(Registries.BIOME, BiomeMakeover.ID("has_structure/reworked_mansion"));

    public static final RegistrySupplier<TrunkPlacerType<AncientOakTrunkPlacer>> ANCIENT_OAK_TRUNK = TRUNK.register(BiomeMakeover.ID("ancient_oak"), ()->new TrunkPlacerType<>(AncientOakTrunkPlacer.CODEC));
    public static final RegistrySupplier<TrunkPlacerType<CypressTrunkPlacer>> CYPRESS_TRUNK = TRUNK.register(BiomeMakeover.ID("swamp_cypress"), ()->new TrunkPlacerType<>(CypressTrunkPlacer.CODEC));
    public static final RegistrySupplier<TrunkPlacerType<WillowTrunkPlacer>> WILLOW_TRUNK = TRUNK.register(BiomeMakeover.ID("willow"), ()->new TrunkPlacerType<>(WillowTrunkPlacer.CODEC));
    public static final RegistrySupplier<TrunkPlacerType<BalsaTrunkPlacer>> BLIGHTED_BALSA_TRUNK = TRUNK.register(BiomeMakeover.ID("blighted_balsa"), ()->new TrunkPlacerType<>(BalsaTrunkPlacer.CODEC));

    public static TagKey<Biome> DARK_FOREST_BIOMES = TagKey.create(Registries.BIOME, BiomeMakeover.ID("dark_forest"));
    public static TagKey<Biome> SWAMP_BIOMES = TagKey.create(Registries.BIOME, BiomeMakeover.ID("swamps"));
    public static TagKey<Biome> BADLANDS_BIOMES = TagKey.create(Registries.BIOME, BiomeMakeover.ID("badlands"));
    public static TagKey<Biome> MUSHROOM_FIELD_BIOMES = TagKey.create(Registries.BIOME, BiomeMakeover.ID("mushroom_fields"));
    public static TagKey<Biome> BEACH_BIOMES = TagKey.create(Registries.BIOME, BiomeMakeover.ID("beaches"));

    public static TagKey<Biome> HAS_TUMBLEWEED = TagKey.create(Registries.BIOME, BiomeMakeover.ID("spawns_tumbleweed"));
    public static TagKey<Biome> SWAMP_BONEMEAL = TagKey.create(Registries.BIOME, BiomeMakeover.ID("swamp_bonemeal"));

    public static void init()
    {
        FEATURES.register();
        FOLIAGE.register();
        DECORATOR.register();
        TRUNK.register();

        BiomeModifications.addProperties(biomeContext -> biomeContext.hasTag(MUSHROOM_FIELD_BIOMES), (biomeContext, mutable) -> {
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, key("mushroom_fields/underground_mycelium"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.FLUID_SPRINGS, key("mushroom_fields/blighted_balsa_trees"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("mushroom_fields/green_glowshrooms"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("mushroom_fields/purple_glowshrooms"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("mushroom_fields/orange_glowshrooms"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("mushroom_fields/mycelium_sprouts"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("mushroom_fields/mycelium_roots"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("mushroom_fields/underground_huge_glowshrooms"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("mushroom_fields/tall_brown_mushrooms"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("mushroom_fields/tall_red_mushrooms"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("dark_forest/wild_mushrooms"));
        });

        BiomeModifications.addProperties(biomeContext -> biomeContext.hasTag(BADLANDS_BIOMES), (biomeContext, mutable) -> {
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("badlands/barrel_cactus"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("badlands/saguaro_cactus"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, key("badlands/paydirt"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, key("badlands/surface_fossil"));
        });

        BiomeModifications.removeProperties(p->p.hasTag(SWAMP_BIOMES), ((biomeContext, mutable) -> {
            mutable.getGenerationProperties().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation("trees_swamp")));
        }));

        BiomeModifications.addProperties(biomeContext -> biomeContext.hasTag(SWAMP_BIOMES), (biomeContext, mutable) -> {
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("swamp/swamp_cypress_trees"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("swamp/willow_trees"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("swamp/big_mushrooms"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("swamp/flowers"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("swamp/pads"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, key("swamp/peat"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, key("swamp/reeds"));
        });

        BiomeModifications.addProperties(biomeContext -> biomeContext.hasTag(DARK_FOREST_BIOMES), (biomeContext, mutable) -> {
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("dark_forest/wild_mushrooms"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("dark_forest/grass"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("dark_forest/tall_grass"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, key("dark_forest/flowers"));

            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, key("dark_forest/itching_ivy"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, key("dark_forest/trees"));
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, key("dark_forest/mesmerite_fissure"));
        });
    }

    private static ResourceKey<PlacedFeature> key(String id)
    {
        return ResourceKey.create(Registries.PLACED_FEATURE, BiomeMakeover.ID(id));
    }
}
