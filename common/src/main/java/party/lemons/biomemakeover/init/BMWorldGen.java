package party.lemons.biomemakeover.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.data.worldgen.StructureSets;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import org.apache.commons.lang3.tuple.Pair;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.block.SmallLilyPadBlock;
import party.lemons.biomemakeover.level.feature.*;
import party.lemons.biomemakeover.level.feature.foliage.*;
import party.lemons.biomemakeover.level.feature.mansion.MansionFeature;
import party.lemons.biomemakeover.level.generate.foliage.WillowTrunkPlacer;
import party.lemons.biomemakeover.mixin.TrunkPlacerTypeInvoker;
import party.lemons.biomemakeover.util.registry.RegistryHelper;
import party.lemons.biomemakeover.util.registry.WoodTypeInfo;

import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

import static net.minecraft.data.worldgen.placement.VegetationPlacements.TREE_THRESHOLD;

public class BMWorldGen
{

    public static class DarkForest
    {
        //Mesmerite Boulder
        public static final Feature<BlockStateConfiguration> MESMERMITE_BOULDER_FEATURE = new MesmeriteBoulderFeature(BlockStateConfiguration.CODEC);
        public static final Holder<ConfiguredFeature<BlockStateConfiguration, ?>> MESMERITE_BOULDER = configure("mesmerite_boulder", MESMERMITE_BOULDER_FEATURE, new BlockStateConfiguration(BMBlocks.MESMERITE.defaultBlockState()));
        public static final Holder<PlacedFeature> MESMERITE_BOULDER_PLACED = place("mesmerite_boulder", MESMERITE_BOULDER, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(10), BiomeFilter.biome());

        //Mesmerite Underground
        public static final Feature<OreConfiguration> MESMERITE_UNDERGROUND_FEATURE = new MesmermiteUndergroundFeature(OreConfiguration.CODEC);
        public static final Holder<ConfiguredFeature<OreConfiguration, ?>> MESMERITE_UNDERGROUND = configure("mesmerite_underground", MESMERITE_UNDERGROUND_FEATURE, new OreConfiguration(OreFeatures.NATURAL_STONE, BMBlocks.MESMERITE.defaultBlockState(), 64));
        public static final Holder<PlacedFeature> MESMERITE_UNDERGROUND_PLACED = place("mesmerite_underground", MESMERITE_UNDERGROUND, RarityFilter.onAverageOnceEvery(4), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome());

        //Itching Ivy
        public static final Feature<NoneFeatureConfiguration> ITCHING_IVY_FEATURE = new ItchingIvyFeature(NoneFeatureConfiguration.CODEC);
        public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> ITCHING_IVY = configure("itching_ivy", ITCHING_IVY_FEATURE);
        public static final Holder<PlacedFeature> ITCHING_IVY_PLACED = place("itching_ivy", ITCHING_IVY, InSquarePlacement.spread(), RarityFilter.onAverageOnceEvery(4), BiomeFilter.biome());

        //Wild Shrooms
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> WILD_MUSHROOMS = configure("wild_mushrooms", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.WILD_MUSHROOMS)), List.of(), 12));
        public static final Holder<PlacedFeature> WILD_MUSHROOMS_PLACED = place("wild_mushrooms", WILD_MUSHROOMS, CountPlacement.of(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

        //Grass
        public static final Holder<PlacedFeature> DF_GRASS_PLACED =  place("df_grass", VegetationFeatures.PATCH_GRASS, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

        //Tall Grass
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> DF_TALL_GRASS = configure("df_tall_grass", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.TALL_GRASS))));
        public static final Holder<PlacedFeature> DF_TALL_GRASS_PLACED = place("df_tall_grass", DF_TALL_GRASS, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome(), RarityFilter.onAverageOnceEvery(3));

        //Flowers
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PEONY = configure("peony", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.PEONY)), List.of(), 7));
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> LILAC = configure("lilac", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.LILAC)), List.of(), 7));
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> ROSE = configure("rose", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.ROSE_BUSH)), List.of(), 7));
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> FOXGLOVE = configure("foxglove", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.FOXGLOVE)), List.of(), 7));
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> BLACK_THISTLE = configure("black_thistle", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.BLACK_THISTLE)), List.of(), 7));
        public static final Holder<PlacedFeature> PEONY_PLACED = place("peony", PEONY);
        public static final Holder<PlacedFeature> LILAC_PLACED = place("lilac", LILAC);
        public static final Holder<PlacedFeature> ROSE_PLACED = place("rose", ROSE);
        public static final Holder<PlacedFeature> FOXGLOVE_PLACED = place("foxglove", FOXGLOVE);
        public static final Holder<PlacedFeature> BLACK_THISTLE_PLACED = place("black_thistle", BLACK_THISTLE);

        public static final Holder<ConfiguredFeature<SimpleRandomFeatureConfiguration, ?>> FLOWERS = configure("df_flowers", Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(List.of(PEONY_PLACED, LILAC_PLACED, ROSE_PLACED, FOXGLOVE_PLACED, BLACK_THISTLE_PLACED))));
        public static final Holder<PlacedFeature> FLOWERS_PLACED = place("df_flowers", FLOWERS, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, CountPlacement.of(ClampedInt.of(UniformInt.of(-1, 8), 0, 8)), BiomeFilter.biome());

        //Small Dark Oak
        public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> DARK_OAK_SMALL = configure("dark_oak_small", Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.DARK_OAK_LOG.defaultBlockState()),
                new FancyTrunkPlacer(7,8,2),
                BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES.defaultBlockState()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 2),
                new TwoLayersFeatureSize(2, 0, 0, OptionalInt.of(3))
        ).ignoreVines().decorators(List.of(new BeehiveDecorator(0.002f))).build());

        //Small Ancient Oak
        public static final TreeDecoratorType<IvyDecorator> IVY_DECORATOR = new TreeDecoratorType<>(IvyDecorator.CODEC);

        public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> ANCIENT_OAK_SMALL = configure("ancient_oak_small", Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BMBlocks.ANCIENT_OAK_WOOD_INFO.getBlock(WoodTypeInfo.Type.LOG).defaultBlockState()),
                new FancyTrunkPlacer(8, 11, 2),
                BlockStateProvider.simple(BMBlocks.ANCIENT_OAK_LEAVES.defaultBlockState()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 2),
                new TwoLayersFeatureSize(2, 0, 0, OptionalInt.of(3)))
                .decorators(ImmutableList.of(IvyDecorator.INSTANCE, new BeehiveDecorator(0.002f))).ignoreVines().build());

        //Ancient Oak
        public static final TrunkPlacerType<AncientOakTrunkPlacer> ANCIENT_OAK_TRUNK = TrunkPlacerTypeInvoker.callRegister(BiomeMakeover.ID("ancient_oak").toString(), AncientOakTrunkPlacer.CODEC);

        public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> ANCIENT_OAK = configure("ancient_oak", Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BMBlocks.ANCIENT_OAK_WOOD_INFO.getBlock(WoodTypeInfo.Type.LOG).defaultBlockState()),
                new AncientOakTrunkPlacer(10, 2, 14),
                BlockStateProvider.simple(BMBlocks.ANCIENT_OAK_LEAVES.defaultBlockState()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new ThreeLayersFeatureSize(2, 3, 0, 1, 2, OptionalInt.empty()))
                .decorators(ImmutableList.of(IvyDecorator.INSTANCE, new BeehiveDecorator(0.002f))).ignoreVines().build());

        //Tree Placement
        public static final Holder<PlacedFeature> ANCIENT_OAK_SMALL_CHECKED = place("ancient_oak_small_checked", ANCIENT_OAK_SMALL, PlacementUtils.filteredByBlockSurvival(BMBlocks.ANCIENT_OAK_SAPLING));
        public static final Holder<PlacedFeature> DARK_OAK_SMALL_CHECKED =  place("dark_oak_small", DARK_OAK_SMALL, PlacementUtils.filteredByBlockSurvival(BMBlocks.ANCIENT_OAK_SAPLING));
        public static final Holder<PlacedFeature> ANCIENT_OAK_CHECKED =  place("ancient_oak", ANCIENT_OAK, PlacementUtils.filteredByBlockSurvival(BMBlocks.ANCIENT_OAK_SAPLING));

        public static final Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> DF_TREES = configure("df_trees", Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ANCIENT_OAK_SMALL_CHECKED, 0.1f), new WeightedPlacedFeature(DARK_OAK_SMALL_CHECKED, 0.2F), new WeightedPlacedFeature(ANCIENT_OAK_CHECKED, 0.05F)), TreePlacements.DARK_OAK_CHECKED));
        public static final Holder<PlacedFeature> DF_TREES_PLACED = place("df_trees", DF_TREES, CountPlacement.of(3), InSquarePlacement.spread(), TREE_THRESHOLD, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());

        //Mansion
        public static final StructureFeature<NoneFeatureConfiguration> MANSION = new MansionFeature(NoneFeatureConfiguration.CODEC);
        public static final ConfiguredStructureFeature<?, ?> MANSION_CONFIGURED = MANSION.configured(NoneFeatureConfiguration.INSTANCE, BiomeTags.HAS_WOODLAND_MANSION, true);

        public static final StructurePieceType MANSION_PIECE = MansionFeature.Piece::new;

        public static void setFeatures()
        {
            DF_GEN.put(GenerationStep.Decoration.VEGETAL_DECORATION, Lists.newArrayList(
                    WILD_MUSHROOMS_PLACED,
                    DF_GRASS_PLACED,
                    DF_TALL_GRASS_PLACED,
                    FLOWERS_PLACED
            ));

            DF_GEN.put(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, Lists.newArrayList(
                    ITCHING_IVY_PLACED
            ));

            DF_GEN.put(GenerationStep.Decoration.UNDERGROUND_DECORATION, Lists.newArrayList(
            ));

            DF_GEN.put(GenerationStep.Decoration.UNDERGROUND_ORES, Lists.newArrayList(
                    MESMERITE_UNDERGROUND_PLACED,
                    DF_TREES_PLACED
            ));

            DF_GEN.put(GenerationStep.Decoration.LOCAL_MODIFICATIONS, Lists.newArrayList(
                    MESMERITE_BOULDER_PLACED
            ));

            DF_GEN.put(GenerationStep.Decoration.SURFACE_STRUCTURES, Lists.newArrayList(
            ));

            DF_STRUCTURES.add(MANSION_CONFIGURED);

        }

        public static void init()
        {
            Holder<ConfiguredStructureFeature<?,?>> mansionHolder = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, BiomeMakeover.ID("mansion")), MANSION_CONFIGURED);
            StructureSets.register(ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, BiomeMakeover.ID("mansion")), mansionHolder, new RandomSpreadStructurePlacement(32, 9, RandomSpreadType.LINEAR, 420));

            RegistryHelper.register(Constants.MOD_ID, Registry.STRUCTURE_FEATURE, StructureFeature.class, DarkForest.class);
            RegistryHelper.register(Constants.MOD_ID, Registry.FEATURE, Feature.class, DarkForest.class);
            RegistryHelper.register(Constants.MOD_ID, Registry.TREE_DECORATOR_TYPES, TreeDecoratorType.class, DarkForest.class);

            RegistryHelper.gatherFields(Constants.MOD_ID, ConfiguredFeature.class, DarkForest.class, CFG_FEATURES);
            RegistryHelper.gatherFields(Constants.MOD_ID, PlacedFeature.class, DarkForest.class, PL_FEATURES);
            setFeatures();

            Registry.register(Registry.STRUCTURE_PIECE, BiomeMakeover.ID("mansion"), MANSION_PIECE);
        //    Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, BiomeMakeover.ID("mansion"), MANSION_CONFIGURED);
        }
    }

    public static final class Swamp
    {
        //Trees
        public static final FoliagePlacerType<WillowFoliagePlacer> WILLOW_FOLIAGE = new FoliagePlacerType<>(WillowFoliagePlacer.CODEC);
        public static final TreeDecoratorType<HangingLeavesDecorator> HANGING_LEAVES_DECORATOR = new TreeDecoratorType<>(HangingLeavesDecorator.CODEC);
        public static final TreeDecoratorType<WillowingBranchDecorator> WILLOWING_BRANCH_DECORATOR = new TreeDecoratorType<>(WillowingBranchDecorator.CODEC);

        public static final Feature<TreeConfiguration> WATER_TREE = new WaterTreeFeature(TreeConfiguration.CODEC);

        //Cypress
        public static final TrunkPlacerType<CypressTrunkPlacer> CYPRESS_TRUNK = TrunkPlacerTypeInvoker.callRegister(BiomeMakeover.ID("swamp_cypress").toString(), CypressTrunkPlacer.CODEC);
        public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> SWAMP_CYPRESS = configure("swamp_cypress", WATER_TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getBlock(WoodTypeInfo.Type.LOG)),
                new CypressTrunkPlacer(16, 3, 2),
                BlockStateProvider.simple(BMBlocks.SWAMP_CYPRESS_LEAVES),
                new WillowFoliagePlacer(ConstantInt.of(1), ConstantInt.of(2), 3, false),
                new TwoLayersFeatureSize(1,0,1, OptionalInt.of(3)))
                .decorators(ImmutableList.of(new HangingLeavesDecorator(BlockStateProvider.simple(BMBlocks.SWAMP_CYPRESS_LEAVES)), new BeehiveDecorator(0.01f), new LeaveVineDecorator()))
                .ignoreVines().build());

        public static final Holder<PlacedFeature> SWAMP_CYPRESS_CHECKED = place("swamp_cypress_checked", SWAMP_CYPRESS, PlacementUtils.filteredByBlockSurvival(BMBlocks.SWAMP_CYPRESS_SAPLING));
        public static final Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> SWAMP_CYPRESS_FILTERED =  configure("swamp_cypress_filtered", Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(SWAMP_CYPRESS_CHECKED, 0.8f)), SWAMP_CYPRESS_CHECKED));
        public static final Holder<PlacedFeature> SWAMP_CYPRESS_TREES_PLACED = place("swamp_cypress_trees_placed", SWAMP_CYPRESS_FILTERED, InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(3), RarityFilter.onAverageOnceEvery(2), PlacementUtils.countExtra(4, 0.5F, 4), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());

        //Willow
        public static final TrunkPlacerType<WillowTrunkPlacer> WILLOW_TRUNK = TrunkPlacerTypeInvoker.callRegister(BiomeMakeover.ID("willow").toString(), WillowTrunkPlacer.CODEC);
        public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> WILLOW = configure("willow", Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BMBlocks.WILLOW_WOOD_INFO.getBlock(WoodTypeInfo.Type.LOG)),
                new WillowTrunkPlacer(6, 3, 2),
                BlockStateProvider.simple(BMBlocks.WILLOW_LEAVES),
                new WillowFoliagePlacer(ConstantInt.of(1), ConstantInt.of(1), 3, false),
                new TwoLayersFeatureSize(1,0,1, OptionalInt.of(3)))
                .decorators(ImmutableList.of(new HangingLeavesDecorator(BlockStateProvider.simple(BMBlocks.WILLOW_LEAVES)), WillowingBranchDecorator.INSTANCE, new BeehiveDecorator(0.02f)))
                .ignoreVines().build());

        public static final Holder<PlacedFeature> WILLOW_CHECKED = place("willow_checked", WILLOW, PlacementUtils.filteredByBlockSurvival(BMBlocks.WILLOW_SAPLING));
        public static final Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> WILLOW_FILTERED =  configure("willow_filtered", Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(WILLOW_CHECKED, 0.8f)), WILLOW_CHECKED));
        public static final Holder<PlacedFeature> WILLOW_TREES_PLACED = place("willow_trees_placed", WILLOW_FILTERED, InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(1), CountPlacement.of(4), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());

        //Peat
        public static final PeatFeature PEAT_FEATURE = new PeatFeature(NoneFeatureConfiguration.CODEC);
        public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> PEAT = configure("peat", PEAT_FEATURE);
        public static final Holder<PlacedFeature> PEAT_PLACED = place("peat", PEAT, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());

        //Reeds
        public static final ReedFeature REED_FEATURE = new ReedFeature(NoneFeatureConfiguration.CODEC);
        public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> REEDS = configure("reeds", REED_FEATURE);
        public static final Holder<PlacedFeature> REEDS_PLACED = place("reeds", REEDS, InSquarePlacement.spread(), CountPlacement.of(10),  PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());

        //Huge Shrooms
        public static final Holder<PlacedFeature> SWAMP_HUGE_SHROOMS = place("swamp_huge_shrooms", VegetationFeatures.MUSHROOM_ISLAND_VEGETATION, PlacementUtils.countExtra(0, 0.05f, 2) ,InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        //FLowers
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> SWAMP_AZALEA = configure("swamp_azalea", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.SWAMP_AZALEA)), List.of(), 12));
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> MARIGOLD = configure("marigold", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.MARIGOLD)), List.of(), 12));
        public static final Holder<PlacedFeature> SWAMP_AZALEA_PLACED = place("swamp_azalea", SWAMP_AZALEA);
        public static final Holder<PlacedFeature> MARIGOLD_PLACED = place("marigold", MARIGOLD);

        public static final Holder<ConfiguredFeature<SimpleRandomFeatureConfiguration, ?>> SWAMP_FLOWERS = configure("swamp_flowers", Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(List.of(SWAMP_AZALEA_PLACED, MARIGOLD_PLACED))));
        public static final Holder<PlacedFeature> SWAMP_FLOWERS_PLACED = place("swamp_flowers", SWAMP_FLOWERS, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, CountPlacement.of(ClampedInt.of(UniformInt.of(-3, 1), 0, 1)), BiomeFilter.biome());

        //Small + Flowered Pads
        public static final WeightedStateProvider PADS = new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                .add(BMBlocks.SMALL_LILY_PAD.defaultBlockState().setValue(SmallLilyPadBlock.PADS, 1), 1)
                .add(BMBlocks.SMALL_LILY_PAD.defaultBlockState().setValue(SmallLilyPadBlock.PADS, 2), 1)
                .add(BMBlocks.SMALL_LILY_PAD.defaultBlockState().setValue(SmallLilyPadBlock.PADS, 3), 1)
                .add(BMBlocks.SMALL_LILY_PAD.defaultBlockState().setValue(SmallLilyPadBlock.PADS, 0), 1)
                .add(BMBlocks.WATER_LILY.defaultBlockState(), 2)
        );

        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> SMALL_AND_FLOWERED_PADS = configure("small_and_flowered_pads", Feature.RANDOM_PATCH, new RandomPatchConfiguration(10, 7, 3, place("pads", configure("pads_place", Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(PADS)))));
        public static final Holder<PlacedFeature> LILY_PAD_PATCH_PLACED = place("small_and_flowered_pads", SMALL_AND_FLOWERED_PADS, VegetationPlacements.worldSurfaceSquaredWithCount(1));

        //Sunken Ruin
        public static final StructureFeature<SunkenRuinFeature.SunkenRuinFeatureConfig> SUNKEN_RUIN = new SunkenRuinFeature(SunkenRuinFeature.SunkenRuinFeatureConfig.CODEC);
        public static final ConfiguredStructureFeature<?, ?> SUNKEN_RUIN_CONFIGURED = SUNKEN_RUIN.configured(new SunkenRuinFeature.SunkenRuinFeatureConfig(0.8F, 0.6F), BiomeTags.HAS_SWAMP_HUT);
        public static final StructurePieceType SUNKEN_RUIN_PIECE = SunkenRuinFeature.SunkenRuinPiece::new;

        public static void init()
        {

            Holder<ConfiguredStructureFeature<?,?>> ruinHolder = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, BiomeMakeover.ID("sunken_ruin")), SUNKEN_RUIN_CONFIGURED);
            StructureSets.register(ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, BiomeMakeover.ID("sunken_ruin")), ruinHolder, new RandomSpreadStructurePlacement(24, 9, RandomSpreadType.LINEAR, 420));

            RegistryHelper.register(Constants.MOD_ID, Registry.STRUCTURE_FEATURE, StructureFeature.class, Swamp.class);
            RegistryHelper.register(Constants.MOD_ID, Registry.FEATURE, Feature.class, Swamp.class);
            RegistryHelper.register(Constants.MOD_ID, Registry.TREE_DECORATOR_TYPES, TreeDecoratorType.class, Swamp.class);
            RegistryHelper.register(Constants.MOD_ID, Registry.FOLIAGE_PLACER_TYPES, FoliagePlacerType.class, Swamp.class);

            RegistryHelper.gatherFields(Constants.MOD_ID, ConfiguredFeature.class, Swamp.class, CFG_FEATURES);
            RegistryHelper.gatherFields(Constants.MOD_ID, PlacedFeature.class, Swamp.class, PL_FEATURES);

            Registry.register(Registry.STRUCTURE_PIECE, BiomeMakeover.ID("sunken_ruin"), SUNKEN_RUIN_PIECE);
     //       Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, BiomeMakeover.ID("sunken_ruin"), SUNKEN_RUIN_CONFIGURED);
            setFeatures();
        }

        public static void setFeatures()
        {
            SWAMP_GEN.put(GenerationStep.Decoration.VEGETAL_DECORATION, Lists.newArrayList(
                    SWAMP_CYPRESS_TREES_PLACED,
                    WILLOW_TREES_PLACED,
                    SWAMP_HUGE_SHROOMS,
                    SWAMP_FLOWERS_PLACED,
                    LILY_PAD_PATCH_PLACED
            ));

            SWAMP_GEN.put(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, Lists.newArrayList(
                    PEAT_PLACED,
                    REEDS_PLACED
            ));

            SWAMP_STRUCTURES.add(SUNKEN_RUIN_CONFIGURED);

        }
    }

    public static final class Badlands
    {
        //Barrel Cactus
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> BARREL_CACTUS = configure("barrel_cactus", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BMBlocks.BARREL_CACTUS.defaultBlockState(), 5).add(BMBlocks.BARREL_CACTUS_FLOWERED.defaultBlockState(), 1))), List.of(), 20));
        public static final Holder<PlacedFeature> BARREL_CACTUS_PLACED = place("barrel_cactus", BARREL_CACTUS, RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);

        //Saguaro Cactus
        public static final Feature<NoneFeatureConfiguration> SAGUARO_CACTUS_FEATURE = new SaguaroCactusFeature(NoneFeatureConfiguration.CODEC);
        public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> SAGUARO_CACTUS = configure("saguro_cactus", SAGUARO_CACTUS_FEATURE);
        public static final Holder<PlacedFeature> SAGUARO_CACTUS_PLACED = place("saguaro_cactus", SAGUARO_CACTUS, RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);

        //Paydirt
        public static final Feature<NoneFeatureConfiguration> PAYDIRT_FEATURE = new PaydirtFeature(NoneFeatureConfiguration.CODEC);
        public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> PAYDIRT = configure("paydirt", PAYDIRT_FEATURE);
        public static final Holder<PlacedFeature> PAYDIRT_PLACED = place("paydirt", PAYDIRT, RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE,  BiomeFilter.biome());

        //Surface Fossils
        public static final Feature<NoneFeatureConfiguration> SURFACE_FOSSIL_FEATURE = new SurfaceFossilFeature(NoneFeatureConfiguration.CODEC);
        public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> SURFACE_FOSSIL = configure("surface_fossil", SURFACE_FOSSIL_FEATURE);
        public static final Holder<PlacedFeature> SURFACE_FOSSIL_PLACED = place("surface_fossil", SURFACE_FOSSIL, RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE,  BiomeFilter.biome());

        //Ghost Town
        public static final JigsawFeature GHOST_TOWN = new GhostTownFeature(JigsawConfiguration.CODEC);
        public static final ConfiguredStructureFeature<?, ?> GHOST_TOWN_CONFIGURED = GHOST_TOWN.configured(GhostTownFeature.CONFIG, BiomeTags.IS_BADLANDS, true,Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE, GhostTownFeature.SPAWNS)));
        public static void init()
        {
            GhostTownFeature.init();

            Holder<ConfiguredStructureFeature<?,?>> townHolder = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, BiomeMakeover.ID("ghost_town")), GHOST_TOWN_CONFIGURED);
            StructureSets.register(ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, BiomeMakeover.ID("ghost_town")), townHolder, new RandomSpreadStructurePlacement(32, 12, RandomSpreadType.LINEAR, 6969));

            RegistryHelper.register(Constants.MOD_ID, Registry.STRUCTURE_FEATURE, StructureFeature.class, Badlands.class);
            RegistryHelper.register(Constants.MOD_ID, Registry.FEATURE, Feature.class, Badlands.class);
          //  Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, BiomeMakeover.ID("ghost_town"), GHOST_TOWN_CONFIGURED);

            RegistryHelper.gatherFields(Constants.MOD_ID, ConfiguredFeature.class, Badlands.class, CFG_FEATURES);
            RegistryHelper.gatherFields(Constants.MOD_ID, PlacedFeature.class, Badlands.class, PL_FEATURES);

            setFeatures();

        }

        public static void setFeatures()
        {
            BADLANDS_GEN.put(GenerationStep.Decoration.VEGETAL_DECORATION, Lists.newArrayList(
                    BARREL_CACTUS_PLACED,
                    SAGUARO_CACTUS_PLACED
            ));

            BADLANDS_GEN.put(GenerationStep.Decoration.UNDERGROUND_DECORATION, Lists.newArrayList(
                    PAYDIRT_PLACED
            ));

            BADLANDS_GEN.put(GenerationStep.Decoration.SURFACE_STRUCTURES, Lists.newArrayList(
                    SURFACE_FOSSIL_PLACED
            ));

            BADLANDS_STRUCTURES.add(GHOST_TOWN_CONFIGURED);
        }
    }

    public static final class MushroomFields
    {
        private static final int UG_START = 60;
        private static final int UG_END = -30;
        private static final HeightRangePlacement UG_HEIGHT = HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(UG_END), VerticalAnchor.absolute(UG_START)));

        //Underground Mycelium
        public static final Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> UNDERGROUND_MYCELIUM_VEGETATION = configure("underground_mycelium_vegetation", Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                .add(BMBlocks.MYCELIUM_SPROUTS.defaultBlockState(), 50)
                .add(BMBlocks.MYCELIUM_ROOTS.defaultBlockState(), 30)
                .add(Blocks.RED_MUSHROOM.defaultBlockState(), 10)
                .add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 10)
                .add(BMBlocks.GREEN_GLOWSHROOM.defaultBlockState(), 1)
                .add(BMBlocks.PURPLE_GLOWSHROOM.defaultBlockState(), 1)
                .add(BMBlocks.TALL_BROWN_MUSHROOM.defaultBlockState(), 3)
                .add(BMBlocks.TALL_RED_MUSHROOM.defaultBlockState(), 3)
        )));

        public static final VegetationPatchConfiguration UNDERGROUND_MYCELIUM_CONFIG = new VegetationPatchConfiguration(BMBlocks.ORE_REPLACEABLE, BlockStateProvider.simple(Blocks.MYCELIUM), place("underground_mycelium_vegetation", UNDERGROUND_MYCELIUM_VEGETATION), CaveSurface.FLOOR, UniformInt.of(1, 3), 0.25f, 3, 0.1f, UniformInt.of(7, 12), 0.8f);
        public static final Feature<VegetationPatchConfiguration> GRASS_PATCH = new GrassPatchFeature(VegetationPatchConfiguration.CODEC);
        public static final Holder<ConfiguredFeature<VegetationPatchConfiguration, ?>> UNDERGROUND_MYCELIUM = configure("underground_mycelium", GRASS_PATCH, UNDERGROUND_MYCELIUM_CONFIG);
        public static final Holder<PlacedFeature> UNDERGROUND_MYCELIUM_PLACED = place("underground_mycelium", UNDERGROUND_MYCELIUM, CountPlacement.of(12), InSquarePlacement.spread(), UG_HEIGHT, EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());

        //Lichen
        public static final Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> LICHEN_BLOCK = configure("lichen", Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.GLOW_LICHEN.defaultBlockState().setValue(MultifaceBlock.getFaceProperty(Direction.UP), true))));
        public static final VegetationPatchConfiguration UNDERGROUND_LICHEN_CONFIG = new VegetationPatchConfiguration(BlockTags.STONE_ORE_REPLACEABLES, BlockStateProvider.simple(Blocks.STONE), place("lichen", LICHEN_BLOCK), CaveSurface.CEILING, UniformInt.of(1, 1), 0F, 1, 0.6f, UniformInt.of(4, 6), 0F);
        public static final Holder<ConfiguredFeature<VegetationPatchConfiguration, ?>> UNDERGROUND_LICHEN = configure("undergroud_lichen", Feature.VEGETATION_PATCH, UNDERGROUND_LICHEN_CONFIG);
        public static final Holder<PlacedFeature> UNDERGROUND_LICHEN_PLACED = place("underground_lichen", UNDERGROUND_LICHEN, CountPlacement.of(100), InSquarePlacement.spread(), UG_HEIGHT, EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());

        //Mushroom Sprouts
        public static final RandomPatchConfiguration MUSHROOM_SPROUTS_CONFIG = FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.MYCELIUM_SPROUTS)), List.of(), 9);
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> MUSHROOM_FIELD_SPROUTS = configure("mushroom_field_sprouts", Feature.RANDOM_PATCH, MUSHROOM_SPROUTS_CONFIG);
        public static final Holder<PlacedFeature> SPROUTS_PLACED = place("mushroom_field_sprouts", MUSHROOM_FIELD_SPROUTS, CountPlacement.of(10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

        //Mushroom Roots
        public static final RandomPatchConfiguration MUSHROOM_ROOTS_CONFIG = FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.MYCELIUM_ROOTS)), List.of(), 5);
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> MUSHROOM_FIELD_ROOTS = configure("mushroom_roots", Feature.RANDOM_PATCH, MUSHROOM_ROOTS_CONFIG);
        public static final Holder<PlacedFeature> ROOTS_PLACED = place("mushroom_roots", MUSHROOM_FIELD_ROOTS, CountPlacement.of(7), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

        //Underground Huge Shroomies
        public static final Holder<PlacedFeature> UNDERGROUND_HUGE_BROWN_SHROOM_PLACED = place("ug_huge_brown_shroom", TreeFeatures.HUGE_BROWN_MUSHROOM, CountPlacement.of(10), InSquarePlacement.spread(), UG_HEIGHT);
        public static final Holder<PlacedFeature> UNDERGROUND_HUGE_RED_SHROOM_PLACED = place("ug_huge_red_shroom", TreeFeatures.HUGE_RED_MUSHROOM, CountPlacement.of(10), InSquarePlacement.spread(), UG_HEIGHT);

        //Underground Huge Glowshroomies
        public static final Feature<HugeMushroomFeatureConfiguration> HUGE_PURPLE_GLOWSHROOM_CONFIG = new HugePurpleGlowshroomFeature(HugeMushroomFeatureConfiguration.CODEC);
        public static final Holder<ConfiguredFeature<HugeMushroomFeatureConfiguration, ?>> HUGE_PURPLE_GLOWSHROOM = configure("huge_purple_shroom", HUGE_PURPLE_GLOWSHROOM_CONFIG, new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(BMBlocks.PURPLE_GLOWSHROOM_BLOCK), BlockStateProvider.simple(BMBlocks.GLOWSHROOM_STEM), 1));
        public static final Holder<PlacedFeature> HUGE_PURPLE_GLOWSHROOM_PLACED = place("huge_pruple_shroom", HUGE_PURPLE_GLOWSHROOM);

        public static final Feature<HugeMushroomFeatureConfiguration> HUGE_GREEN_GLOWSHROOM_CONFIG = new HugeGreenGlowshroomFeature(HugeMushroomFeatureConfiguration.CODEC);
        public static final Holder<ConfiguredFeature<HugeMushroomFeatureConfiguration, ?>> HUGE_GREEN_GLOWSHROOM = configure("huge_green_shroom", HUGE_GREEN_GLOWSHROOM_CONFIG, new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(BMBlocks.GREEN_GLOWSHROOM_BLOCK), BlockStateProvider.simple(BMBlocks.GLOWSHROOM_STEM), 1));
        public static final Holder<PlacedFeature> HUGE_GREEN_GLOWSHROOM_PLACED = place("huge_green_shroom", HUGE_GREEN_GLOWSHROOM);

        public static final Holder<ConfiguredFeature<RandomBooleanFeatureConfiguration, ?>> UNDERGROUND_GLOWSHROOMS = configure("underground_glowshrooms", Feature.RANDOM_BOOLEAN_SELECTOR, new RandomBooleanFeatureConfiguration(HUGE_GREEN_GLOWSHROOM_PLACED, HUGE_PURPLE_GLOWSHROOM_PLACED));
        public static final Holder<PlacedFeature> UNDERGROUND_GLOWSHROOMS_PLACED = place("underground_glowshrooms", UNDERGROUND_GLOWSHROOMS, CountPlacement.of(120), InSquarePlacement.spread(), UG_HEIGHT);

        public static final Feature<HugeMushroomFeatureConfiguration> HUGE_ORANGE_GLOWSHROOM_FEATURE = new HugeOrangeGlowshroomFeature(HugeMushroomFeatureConfiguration.CODEC);
        public static final Holder<ConfiguredFeature<HugeMushroomFeatureConfiguration, ?>> HUGE_ORANGE_GLOWSHROOM = configure("huge_orange_shroom", HUGE_ORANGE_GLOWSHROOM_FEATURE, new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(BMBlocks.ORANGE_GLOWSHROOM_BLOCK), BlockStateProvider.simple(BMBlocks.GLOWSHROOM_STEM), 1));

        //Underground small Shroomies
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> GREEN_GLOWSHROOM = configure("green_glowshrooms", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.GREEN_GLOWSHROOM))));
        public static final Holder<PlacedFeature> GREEN_GLOWSHROOM_PLACED = place("green_glowshroom", GREEN_GLOWSHROOM, CountPlacement.of(2), InSquarePlacement.spread(), UG_HEIGHT);

        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PURPLE_GLOWSHROOM = configure("purple_glowshroom", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.PURPLE_GLOWSHROOM))));
        public static final Holder<PlacedFeature> PURPLE_GLOWSHROOM_PLACED = place("purple_glowshroom", PURPLE_GLOWSHROOM, CountPlacement.of(2), InSquarePlacement.spread(), UG_HEIGHT);

        //Orange Shroomies
        public static final Feature<ProbabilityFeatureConfiguration> ORANGE_GLOWSHROOM_FEATURE = new OrangeGlowshroomFeature(ProbabilityFeatureConfiguration.CODEC);
        public static final Holder<ConfiguredFeature<ProbabilityFeatureConfiguration, ?>> ORANGE_GLOWSHROOM = configure("orange_glowshroom", ORANGE_GLOWSHROOM_FEATURE, new ProbabilityFeatureConfiguration(0.1F));
        public static final Holder<PlacedFeature> ORANGE_GLOWSHROOM_PLACED = place("orange_glowshroom", ORANGE_GLOWSHROOM, CountPlacement.of(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR);

        //Tall Shroomies
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> TALL_BROWN_MUSHROOMS = configure("tall_brown_mushrooms", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.TALL_BROWN_MUSHROOM)), List.of(), 20));
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> TALL_RED_MUSHROOMS = configure("tall_red_mushroom", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.TALL_RED_MUSHROOM)), List.of(), 20));
        public static final Holder<PlacedFeature> TALL_BROWN_MUSHROOMS_PLACED = place("tall_brown_mushrooms", TALL_BROWN_MUSHROOMS, RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);
        public static final Holder<PlacedFeature> TALL_RED_MUSHROOMS_PLACED = place("tall_red_mushrooms", TALL_RED_MUSHROOMS, RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);

        //Blighted Balsa
        public static final TrunkPlacerType<BalsaTrunkPlacer> BLIGHTED_BALSA_TRUNK = TrunkPlacerTypeInvoker.callRegister(BiomeMakeover.ID("blighted_balse").toString(), BalsaTrunkPlacer.CODEC);
        public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> BLIGHTED_BALSA = configure("blighted_balsa", Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BMBlocks.BLIGHTED_BALSA_WOOD_INFO.getBlock(WoodTypeInfo.Type.LOG)),
                new BalsaTrunkPlacer(4, 6, 8),
                BlockStateProvider.simple(BMBlocks.BLIGHTED_BALSA_LEAVES),
                new AcaciaFoliagePlacer(UniformInt.of(1, 1), ConstantInt.of(0)),
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
                ).ignoreVines().build());

        public static final Holder<PlacedFeature> BLIGHTED_BALSA_CHECKED = place("blighted_balsa_checked", BLIGHTED_BALSA, PlacementUtils.filteredByBlockSurvival(BMBlocks.BLIGHTED_BALSA_SAPLING));
        public static final Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> BLIGHTED_BALSA_FILTERED =  configure("blighted_balsa_filtered", Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(BLIGHTED_BALSA_CHECKED, 0.8f)), BLIGHTED_BALSA_CHECKED));
        public static final Holder<PlacedFeature> BLIGHTED_BALSA_TREES_PLACED = place("blighted_balsa_placed", BLIGHTED_BALSA_FILTERED, VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(12)));

        //Bigger Caves
        public static final ConfiguredWorldCarver<CaveCarverConfiguration> BIGGER_CAVES = WorldCarver.CAVE.configured(new CaveCarverConfiguration(0.3f, UniformHeight.of(VerticalAnchor.absolute(UG_END), VerticalAnchor.absolute(UG_START)), UniformFloat.of(0.1f, 0.9f), VerticalAnchor.aboveBottom(8), CarverDebugSettings.of(false, Blocks.OAK_BUTTON.defaultBlockState()), UniformFloat.of(0.1f, 4f), UniformFloat.of(0.1f, 4f), UniformFloat.of(-1.0f, -0.4f)));

        public static void init()
        {
            RegistryHelper.register(Constants.MOD_ID, Registry.FEATURE, Feature.class, MushroomFields.class);
            Registry.register(BuiltinRegistries.CONFIGURED_CARVER, BiomeMakeover.ID("bigger_caves"), BIGGER_CAVES);

            RegistryHelper.gatherFields(Constants.MOD_ID, ConfiguredFeature.class, MushroomFields.class, CFG_FEATURES);
            RegistryHelper.gatherFields(Constants.MOD_ID, PlacedFeature.class, MushroomFields.class, PL_FEATURES);

            MUSHROOM_CARVERS.add(BIGGER_CAVES);
            setFeatures();
        }

        public static void setFeatures()
        {
            //Underground
            MUSHROOM_GEN.put(GenerationStep.Decoration.UNDERGROUND_DECORATION, Lists.newArrayList(
                    UNDERGROUND_MYCELIUM_PLACED,
                    UNDERGROUND_LICHEN_PLACED

                    ));

            MUSHROOM_GEN.put(GenerationStep.Decoration.FLUID_SPRINGS, Lists.newArrayList(
                    BLIGHTED_BALSA_TREES_PLACED //Moving trees a step earlier to generate before Terralith giant mushrooms.
                    ));

                    //Vegetal
            MUSHROOM_GEN.put(GenerationStep.Decoration.VEGETAL_DECORATION, Lists.newArrayList(
                    GREEN_GLOWSHROOM_PLACED,
                    PURPLE_GLOWSHROOM_PLACED,
                    ORANGE_GLOWSHROOM_PLACED,
                    SPROUTS_PLACED,
                    ROOTS_PLACED,
                    UNDERGROUND_GLOWSHROOMS_PLACED,
                    UNDERGROUND_HUGE_BROWN_SHROOM_PLACED,
                    UNDERGROUND_HUGE_RED_SHROOM_PLACED,
                    TALL_BROWN_MUSHROOMS_PLACED,
                    TALL_RED_MUSHROOMS_PLACED,
                    DarkForest.WILD_MUSHROOMS_PLACED
            ));
        }
    }
    private static final List<Pair<ResourceLocation, ConfiguredFeature>> CFG_FEATURES = Lists.newArrayList();
    private static final List<Pair<ResourceLocation, PlacedFeature>> PL_FEATURES = Lists.newArrayList();

    private static boolean registered = false;
    public static Map<GenerationStep.Decoration, List<Holder<PlacedFeature>>> MUSHROOM_GEN = Maps.newHashMap();
    public static List<ConfiguredWorldCarver> MUSHROOM_CARVERS = Lists.newArrayList();
    public static Map<GenerationStep.Decoration, List<Holder<PlacedFeature>>> BADLANDS_GEN = Maps.newHashMap();
    public static List<ConfiguredStructureFeature<?, ?>> BADLANDS_STRUCTURES = Lists.newArrayList();
    public static Map<GenerationStep.Decoration, List<Holder<PlacedFeature>>> SWAMP_GEN = Maps.newHashMap();
    public static List<ConfiguredStructureFeature<?, ?>> SWAMP_STRUCTURES = Lists.newArrayList();
    public static Map<GenerationStep.Decoration, List<Holder<PlacedFeature>>> DF_GEN = Maps.newHashMap();
    public static List<ConfiguredStructureFeature<?, ?>> DF_STRUCTURES = Lists.newArrayList();


    public static void init()
    {
        if(registered)
            return;
        registered = true;

        MushroomFields.init();
        Badlands.init();
        Swamp.init();
        DarkForest.init();

        registerStuff();
    }

    public static void registerStuff()
    {
        for(Pair<ResourceLocation, ConfiguredFeature> f : CFG_FEATURES)
        {
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, f.getLeft(), f.getRight());
        }

        for(Pair<ResourceLocation, PlacedFeature> f : PL_FEATURES)
        {
            Registry.register(BuiltinRegistries.PLACED_FEATURE, f.getLeft(), f.getRight());
        }
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<FC, ?>> configure(String id, F feature, FC featureConfiguration) {
        return BuiltinRegistries.registerExact(BuiltinRegistries.CONFIGURED_FEATURE, Constants.MOD_ID  + ":" + id, new ConfiguredFeature<>(feature, featureConfiguration));
    }

    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> configure(String string, Feature<NoneFeatureConfiguration> feature) {
        return configure(string, feature, FeatureConfiguration.NONE);
    }

    public static Holder<PlacedFeature> place(String string, Holder<? extends ConfiguredFeature<?, ?>> holder, List<PlacementModifier> list) {
        return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, Constants.MOD_ID + ":" + string, new PlacedFeature(Holder.hackyErase(holder), List.copyOf(list)));
    }

    public static Holder<PlacedFeature> place(String string, Holder<? extends ConfiguredFeature<?, ?>> holder, PlacementModifier ... placementModifiers) {
        return place(string, holder, List.of(placementModifiers));
    }
}
