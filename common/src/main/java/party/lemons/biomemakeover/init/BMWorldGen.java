package party.lemons.biomemakeover.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
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
import org.apache.commons.lang3.tuple.Pair;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.block.SmallLilyPadBlock;
import party.lemons.biomemakeover.level.feature.*;
import party.lemons.biomemakeover.level.feature.foliage.*;
import party.lemons.biomemakeover.level.feature.mansion.MansionFeature;
import party.lemons.biomemakeover.level.generate.foliage.WillowTrunkPlacer;
import party.lemons.biomemakeover.mixin.FoliagePlacerTypeInvoker;
import party.lemons.biomemakeover.mixin.TreeDecoratorTypeInvoker;
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
        public static final ConfiguredFeature<?, ?> MESMERITE_BOULDER = MESMERMITE_BOULDER_FEATURE.configured(new BlockStateConfiguration(BMBlocks.MESMERITE.defaultBlockState()));
        public static final PlacedFeature MESMERITE_BOULDER_PLACED = MESMERITE_BOULDER.placed(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(10), BiomeFilter.biome());

        //Mesmerite Underground
        public static final Feature<OreConfiguration> MESMERITE_UNDERGROUND_FEATURE = new MesmermiteUndergroundFeature(OreConfiguration.CODEC);
        public static final ConfiguredFeature<?, ?> MESMERITE_UNDERGROUND = MESMERITE_UNDERGROUND_FEATURE.configured(new OreConfiguration(OreFeatures.NATURAL_STONE, BMBlocks.MESMERITE.defaultBlockState(), 64));
        public static final PlacedFeature MESMERITE_UNDERGROUND_PLACED = MESMERITE_UNDERGROUND.placed(RarityFilter.onAverageOnceEvery(4), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome());

        //Itching Ivy
        public static final Feature<NoneFeatureConfiguration> ITCHING_IVY_FEATURE = new ItchingIvyFeature(NoneFeatureConfiguration.CODEC);
        public static final ConfiguredFeature<?, ?> ITCHING_IVY = ITCHING_IVY_FEATURE.configured(NoneFeatureConfiguration.INSTANCE);
        public static final PlacedFeature ITCHING_IVY_PLACED = ITCHING_IVY.placed(InSquarePlacement.spread(), RarityFilter.onAverageOnceEvery(4), BiomeFilter.biome());

        //Wild Shrooms
        public static final ConfiguredFeature<?, ?> WILD_MUSHROOMS = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.WILD_MUSHROOMS))), List.of(), 12));
        public static final PlacedFeature WILD_MUSHROOMS_PLACED = WILD_MUSHROOMS.placed(CountPlacement.of(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

        //Grass
        public static final PlacedFeature DF_GRASS_PLACED =  VegetationFeatures.PATCH_GRASS.placed(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

        //Tall Grass
        public static final ConfiguredFeature<?, ?> DF_TALL_GRASS = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.TALL_GRASS)))));
        public static final PlacedFeature DF_TALL_GRASS_PLACED = DF_TALL_GRASS.placed(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome(), RarityFilter.onAverageOnceEvery(3));

        //Flowers
        public static final ConfiguredFeature<?, ?> PEONY = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.PEONY))), List.of(), 7));
        public static final ConfiguredFeature<?, ?> LILAC = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.LILAC))), List.of(), 7));
        public static final ConfiguredFeature<?, ?> ROSE = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.ROSE_BUSH))), List.of(), 7));
        public static final ConfiguredFeature<?, ?> FOXGLOVE = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.FOXGLOVE))), List.of(), 7));
        public static final ConfiguredFeature<?, ?> BLACK_THISTLE = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.BLACK_THISTLE))), List.of(), 7));
        public static final PlacedFeature PEONY_PLACED = PEONY.placed();
        public static final PlacedFeature LILAC_PLACED = LILAC.placed();
        public static final PlacedFeature ROSE_PLACED = ROSE.placed();
        public static final PlacedFeature FOXGLOVE_PLACED = FOXGLOVE.placed();
        public static final PlacedFeature BLACK_THISTLE_PLACED = BLACK_THISTLE.placed();

        public static final ConfiguredFeature<?, ?> FLOWERS = Feature.SIMPLE_RANDOM_SELECTOR.configured(new SimpleRandomFeatureConfiguration(List.of(()->PEONY_PLACED, ()->LILAC_PLACED, ()->ROSE_PLACED, ()->FOXGLOVE_PLACED, ()->BLACK_THISTLE_PLACED)));
        public static final PlacedFeature FLOWERS_PLACED = FLOWERS.placed(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, CountPlacement.of(ClampedInt.of(UniformInt.of(-1, 8), 0, 8)), BiomeFilter.biome());

        //Small Dark Oak
        public static final ConfiguredFeature<?, ?> DARK_OAK_SMALL = Feature.TREE.configured(new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.DARK_OAK_LOG.defaultBlockState()),
                new FancyTrunkPlacer(7,8,2),
                BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES.defaultBlockState()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 2),
                new TwoLayersFeatureSize(2, 0, 0, OptionalInt.of(3))
        ).ignoreVines().decorators(List.of(new BeehiveDecorator(0.002f))).build());

        //Small Ancient Oak
        public static final TreeDecoratorType<IvyDecorator> IVY_DECORATOR = TreeDecoratorTypeInvoker.callRegister(BiomeMakeover.ID("ivy").toString(), IvyDecorator.CODEC);

        public static final ConfiguredFeature<?, ?> ANCIENT_OAK_SMALL = Feature.TREE.configured(new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BMBlocks.ANCIENT_OAK_WOOD_INFO.getBlock(WoodTypeInfo.Type.LOG).defaultBlockState()),
                new FancyTrunkPlacer(8, 11, 2),
                BlockStateProvider.simple(BMBlocks.ANCIENT_OAK_LEAVES.defaultBlockState()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 2),
                new TwoLayersFeatureSize(2, 0, 0, OptionalInt.of(3)))
                .decorators(ImmutableList.of(IvyDecorator.INSTANCE, new BeehiveDecorator(0.002f))).ignoreVines().build());

        //Ancient Oak
        public static final TrunkPlacerType<AncientOakTrunkPlacer> ANCIENT_OAK_TRUNK = TrunkPlacerTypeInvoker.callRegister(BiomeMakeover.ID("ancient_oak").toString(), AncientOakTrunkPlacer.CODEC);

        public static final ConfiguredFeature<?, ?> ANCIENT_OAK = Feature.TREE.configured(new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BMBlocks.ANCIENT_OAK_WOOD_INFO.getBlock(WoodTypeInfo.Type.LOG).defaultBlockState()),
                new AncientOakTrunkPlacer(10, 2, 14),
                BlockStateProvider.simple(BMBlocks.ANCIENT_OAK_LEAVES.defaultBlockState()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new ThreeLayersFeatureSize(2, 3, 0, 1, 2, OptionalInt.empty()))
                .decorators(ImmutableList.of(IvyDecorator.INSTANCE, new BeehiveDecorator(0.002f))).ignoreVines().build());

        //Tree Placement
        public static final PlacedFeature ANCIENT_OAK_SMALL_CHECKED = ANCIENT_OAK_SMALL.filteredByBlockSurvival(BMBlocks.ANCIENT_OAK_SAPLING);
        public static final PlacedFeature DARK_OAK_SMALL_CHECKED = DARK_OAK_SMALL.filteredByBlockSurvival(BMBlocks.ANCIENT_OAK_SAPLING);
        public static final PlacedFeature ANCIENT_OAK_CHECKED = ANCIENT_OAK.filteredByBlockSurvival(BMBlocks.ANCIENT_OAK_SAPLING);

        public static final ConfiguredFeature<RandomFeatureConfiguration, ?> DF_TREES = Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ANCIENT_OAK_SMALL_CHECKED, 0.1f), new WeightedPlacedFeature(DARK_OAK_SMALL_CHECKED, 0.2F), new WeightedPlacedFeature(ANCIENT_OAK_CHECKED, 0.05F)), TreePlacements.DARK_OAK_CHECKED));
        public static final PlacedFeature DF_TREES_PLACED = DF_TREES.placed(CountPlacement.of(3), InSquarePlacement.spread(), TREE_THRESHOLD, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());

        //Mansion
        public static final StructureFeature<NoneFeatureConfiguration> MANSION = new MansionFeature(NoneFeatureConfiguration.CODEC);
        public static final ConfiguredStructureFeature<?, ?> MANSION_CONFIGURED = MANSION.configured(NoneFeatureConfiguration.INSTANCE);

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
            registerStructure("mansion", MANSION, new StructureFeatureConfiguration(32, 9, 420), true);

            RegistryHelper.register(Constants.MOD_ID, Registry.FEATURE, Feature.class, DarkForest.class);

            RegistryHelper.gatherFields(Constants.MOD_ID, ConfiguredFeature.class, DarkForest.class, CFG_FEATURES);
            RegistryHelper.gatherFields(Constants.MOD_ID, PlacedFeature.class, DarkForest.class, PL_FEATURES);
            setFeatures();

            Registry.register(Registry.STRUCTURE_PIECE, BiomeMakeover.ID("mansion"), MANSION_PIECE);
            Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, BiomeMakeover.ID("mansion"), MANSION_CONFIGURED);
        }
    }

    public static final class Swamp
    {
        //Trees
        public static final FoliagePlacerType<WillowFoliagePlacer> WILLOW_FOLIAGE = FoliagePlacerTypeInvoker.callRegister(BiomeMakeover.ID("willow").toString(), WillowFoliagePlacer.CODEC);
        public static final TreeDecoratorType<HangingLeavesDecorator> HANGING_LEAVES_DECORATOR = TreeDecoratorTypeInvoker.callRegister(BiomeMakeover.ID("hanging_leaves").toString(), HangingLeavesDecorator.CODEC);
        public static final TreeDecoratorType<WillowingBranchDecorator> WILLOWING_BRANCH_DECORATOR = TreeDecoratorTypeInvoker.callRegister(BiomeMakeover.ID("willowing_branch").toString(), WillowingBranchDecorator.CODEC);

        public static final Feature<TreeConfiguration> WATER_TREE = new WaterTreeFeature(TreeConfiguration.CODEC);

        //Cypress
        public static final TrunkPlacerType<CypressTrunkPlacer> CYPRESS_TRUNK = TrunkPlacerTypeInvoker.callRegister(BiomeMakeover.ID("swamp_cypress").toString(), CypressTrunkPlacer.CODEC);
        public static final ConfiguredFeature<TreeConfiguration, ?> SWAMP_CYPRESS = WATER_TREE.configured(new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getBlock(WoodTypeInfo.Type.LOG)),
                new CypressTrunkPlacer(16, 3, 2),
                BlockStateProvider.simple(BMBlocks.SWAMP_CYPRESS_LEAVES),
                new WillowFoliagePlacer(ConstantInt.of(1), ConstantInt.of(2), 3, false),
                new TwoLayersFeatureSize(1,0,1, OptionalInt.of(3)))
                .decorators(ImmutableList.of(new HangingLeavesDecorator(BlockStateProvider.simple(BMBlocks.SWAMP_CYPRESS_LEAVES)), new BeehiveDecorator(0.01f), new LeaveVineDecorator()))
                .ignoreVines().build());

        public static final PlacedFeature SWAMP_CYPRESS_CHECKED = SWAMP_CYPRESS.filteredByBlockSurvival(BMBlocks.SWAMP_CYPRESS_SAPLING);
        public static final ConfiguredFeature<?, ?> SWAMP_CYPRESS_FILTERED =  Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(SWAMP_CYPRESS_CHECKED, 0.8f)), SWAMP_CYPRESS_CHECKED));
        public static final PlacedFeature SWAMP_CYPRESS_TREES_PLACED = SWAMP_CYPRESS_FILTERED.placed(InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(3), RarityFilter.onAverageOnceEvery(2), PlacementUtils.countExtra(4, 0.5F, 4), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());

        //Willow
        public static final TrunkPlacerType<WillowTrunkPlacer> WILLOW_TRUNK = TrunkPlacerTypeInvoker.callRegister(BiomeMakeover.ID("willow").toString(), WillowTrunkPlacer.CODEC);
        public static final ConfiguredFeature<TreeConfiguration, ?> WILLOW = Feature.TREE.configured(new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BMBlocks.WILLOW_WOOD_INFO.getBlock(WoodTypeInfo.Type.LOG)),
                new WillowTrunkPlacer(6, 3, 2),
                BlockStateProvider.simple(BMBlocks.WILLOW_LEAVES),
                new WillowFoliagePlacer(ConstantInt.of(1), ConstantInt.of(1), 3, false),
                new TwoLayersFeatureSize(1,0,1, OptionalInt.of(3)))
                .decorators(ImmutableList.of(new HangingLeavesDecorator(BlockStateProvider.simple(BMBlocks.WILLOW_LEAVES)), WillowingBranchDecorator.INSTANCE, new BeehiveDecorator(0.02f)))
                .ignoreVines().build());

        public static final PlacedFeature WILLOW_CHECKED = WILLOW.filteredByBlockSurvival(BMBlocks.WILLOW_SAPLING);
        public static final ConfiguredFeature<?, ?> WILLOW_FILTERED =  Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(WILLOW_CHECKED, 0.8f)), WILLOW_CHECKED));
        public static final PlacedFeature WILLOW_TREES_PLACED = WILLOW_FILTERED.placed(InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(1), CountPlacement.of(4), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());

        //Peat
        public static final PeatFeature PEAT_FEATURE = new PeatFeature(NoneFeatureConfiguration.CODEC);
        public static final ConfiguredFeature<?, ?> PEAT = PEAT_FEATURE.configured(FeatureConfiguration.NONE);
        public static final PlacedFeature PEAT_PLACED = PEAT.placed(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());

        //Reeds
        public static final ReedFeature REED_FEATURE = new ReedFeature(NoneFeatureConfiguration.CODEC);
        public static final ConfiguredFeature<?, ?> REEDS = REED_FEATURE.configured(NoneFeatureConfiguration.NONE);
        public static final PlacedFeature REEDS_PLACED = REEDS.placed(InSquarePlacement.spread(), CountPlacement.of(10),  PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());

        //Huge Shrooms
        public static final PlacedFeature SWAMP_HUGE_SHROOMS = VegetationFeatures.MUSHROOM_ISLAND_VEGETATION.placed(PlacementUtils.countExtra(0, 0.05f, 2) ,InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        //FLowers
        public static final ConfiguredFeature<?, ?> SWAMP_AZALEA = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.SWAMP_AZALEA))), List.of(), 12));
        public static final ConfiguredFeature<?, ?> MARIGOLD = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.MARIGOLD))), List.of(), 12));
        public static final PlacedFeature SWAMP_AZALEA_PLACED = SWAMP_AZALEA.placed();
        public static final PlacedFeature MARIGOLD_PLACED = MARIGOLD.placed();

        public static final ConfiguredFeature<?, ?> SWAMP_FLOWERS = Feature.SIMPLE_RANDOM_SELECTOR.configured(new SimpleRandomFeatureConfiguration(List.of(()->SWAMP_AZALEA_PLACED, ()->MARIGOLD_PLACED)));
        public static final PlacedFeature SWAMP_FLOWERS_PLACED = SWAMP_FLOWERS.placed(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, CountPlacement.of(ClampedInt.of(UniformInt.of(-3, 1), 0, 1)), BiomeFilter.biome());

        //Small + Flowered Pads
        public static final WeightedStateProvider PADS = new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                .add(BMBlocks.SMALL_LILY_PAD.defaultBlockState().setValue(SmallLilyPadBlock.PADS, 1), 1)
                .add(BMBlocks.SMALL_LILY_PAD.defaultBlockState().setValue(SmallLilyPadBlock.PADS, 2), 1)
                .add(BMBlocks.SMALL_LILY_PAD.defaultBlockState().setValue(SmallLilyPadBlock.PADS, 3), 1)
                .add(BMBlocks.SMALL_LILY_PAD.defaultBlockState().setValue(SmallLilyPadBlock.PADS, 0), 1)
                .add(BMBlocks.WATER_LILY.defaultBlockState(), 2)
        );

        public static final ConfiguredFeature<?, ?> SMALL_AND_FLOWERED_PADS = Feature.RANDOM_PATCH.configured(new RandomPatchConfiguration(10, 7, 3, () -> Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(PADS)).onlyWhenEmpty()));
        public static final PlacedFeature LILY_PAD_PATCH_PLACED = SMALL_AND_FLOWERED_PADS.placed(VegetationPlacements.worldSurfaceSquaredWithCount(1));

        //Sunken Ruin
        public static final StructureFeature<SunkenRuinFeature.SunkenRuinFeatureConfig> SUNKEN_RUIN = new SunkenRuinFeature(SunkenRuinFeature.SunkenRuinFeatureConfig.CODEC);
        public static final ConfiguredStructureFeature<?, ?> SUNKEN_RUIN_CONFIGURED = SUNKEN_RUIN.configured(new SunkenRuinFeature.SunkenRuinFeatureConfig(0.8F, 0.6F));
        public static final StructurePieceType SUNKEN_RUIN_PIECE = SunkenRuinFeature.SunkenRuinPiece::new;

        public static void init()
        {
            registerStructure("sunken_ruin", SUNKEN_RUIN, new StructureFeatureConfiguration(24, 9, 420), false);

            RegistryHelper.register(Constants.MOD_ID, Registry.FEATURE, Feature.class, Swamp.class);

            RegistryHelper.gatherFields(Constants.MOD_ID, ConfiguredFeature.class, Swamp.class, CFG_FEATURES);
            RegistryHelper.gatherFields(Constants.MOD_ID, PlacedFeature.class, Swamp.class, PL_FEATURES);

            Registry.register(Registry.STRUCTURE_PIECE, BiomeMakeover.ID("sunken_ruin"), SUNKEN_RUIN_PIECE);
            Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, BiomeMakeover.ID("sunken_ruin"), SUNKEN_RUIN_CONFIGURED);
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
        public static final ConfiguredFeature<?, ?> BARREL_CACTUS = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(BMBlocks.BARREL_CACTUS.defaultBlockState(), 5).add(BMBlocks.BARREL_CACTUS_FLOWERED.defaultBlockState(), 1)))), List.of(), 20));
        public static final PlacedFeature BARREL_CACTUS_PLACED = BARREL_CACTUS.placed(RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);

        //Saguaro Cactus
        public static final Feature<NoneFeatureConfiguration> SAGUARO_CACTUS_FEATURE = new SaguaroCactusFeature(NoneFeatureConfiguration.CODEC);
        public static final ConfiguredFeature<?, ?> SAGUARO_CACTUS = SAGUARO_CACTUS_FEATURE.configured(FeatureConfiguration.NONE);
        public static final PlacedFeature SAGUARO_CACTUS_PLACED = SAGUARO_CACTUS.placed(RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);

        //Paydirt
        public static final Feature<NoneFeatureConfiguration> PAYDIRT_FEATURE = new PaydirtFeature(NoneFeatureConfiguration.CODEC);
        public static final ConfiguredFeature<?, ?> PAYDIRT = PAYDIRT_FEATURE.configured(FeatureConfiguration.NONE);
        public static final PlacedFeature PAYDIRT_PLACED = PAYDIRT.placed(RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE,  BiomeFilter.biome());

        //Surface Fossils
        public static final Feature<NoneFeatureConfiguration> SURFACE_FOSSIL_FEATURE = new SurfaceFossilFeature(NoneFeatureConfiguration.CODEC);
        public static final ConfiguredFeature<?, ?> SURFACE_FOSSIL = SURFACE_FOSSIL_FEATURE.configured(FeatureConfiguration.NONE);
        public static final PlacedFeature SURFACE_FOSSIL_PLACED = SURFACE_FOSSIL.placed(RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE,  BiomeFilter.biome());

        //Ghost Town
        public static final JigsawFeature GHOST_TOWN = new GhostTownFeature(JigsawConfiguration.CODEC);
        public static final ConfiguredStructureFeature<?, ?> GHOST_TOWN_CONFIGURED = GHOST_TOWN.configured(GhostTownFeature.CONFIG);

        public static void init()
        {
            GhostTownFeature.init();

            registerStructure("ghost_town", GHOST_TOWN, new StructureFeatureConfiguration(32, 12, 6969), true);


            RegistryHelper.register(Constants.MOD_ID, Registry.FEATURE, Feature.class, Badlands.class);
            Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, BiomeMakeover.ID("ghost_town"), GHOST_TOWN_CONFIGURED);

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
        private static final int UG_END = 0;
        private static final HeightRangePlacement UG_HEIGHT = HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(UG_END), VerticalAnchor.absolute(UG_START)));

        //Underground Mycelium
        public static final ConfiguredFeature<SimpleBlockConfiguration, ?> UNDERGROUND_MYCELIUM_VEGETATION = Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                .add(BMBlocks.MYCELIUM_SPROUTS.defaultBlockState(), 50)
                .add(BMBlocks.MYCELIUM_ROOTS.defaultBlockState(), 30)
                .add(Blocks.RED_MUSHROOM.defaultBlockState(), 10)
                .add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 10)
                .add(BMBlocks.GREEN_GLOWSHROOM.defaultBlockState(), 1)
                .add(BMBlocks.PURPLE_GLOWSHROOM.defaultBlockState(), 1)
                .add(BMBlocks.TALL_BROWN_MUSHROOM.defaultBlockState(), 3)
                .add(BMBlocks.TALL_RED_MUSHROOM.defaultBlockState(), 3)
        )));

        public static final VegetationPatchConfiguration UNDERGROUND_MYCELIUM_CONFIG = new VegetationPatchConfiguration(BlockTags.STONE_ORE_REPLACEABLES.getName(), BlockStateProvider.simple(Blocks.MYCELIUM), UNDERGROUND_MYCELIUM_VEGETATION::placed, CaveSurface.FLOOR, UniformInt.of(1, 3), 0.25f, 3, 0.1f, UniformInt.of(7, 12), 0.8f);
        public static final ConfiguredFeature<?, ?> UNDERGROUND_MYCELIUM = Feature.VEGETATION_PATCH.configured(UNDERGROUND_MYCELIUM_CONFIG);
        public static final PlacedFeature UNDERGROUND_MYCELIUM_PLACED = UNDERGROUND_MYCELIUM.placed(CountPlacement.of(12), InSquarePlacement.spread(), UG_HEIGHT, EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());

        //Lichen
        public static final ConfiguredFeature<?, ?> LICHEN_BLOCK = Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.GLOW_LICHEN.defaultBlockState().setValue(MultifaceBlock.getFaceProperty(Direction.UP), true))));
        public static final VegetationPatchConfiguration UNDERGROUND_LICHEN_CONFIG = new VegetationPatchConfiguration(BlockTags.STONE_ORE_REPLACEABLES.getName(), BlockStateProvider.simple(Blocks.STONE), LICHEN_BLOCK::placed, CaveSurface.CEILING, UniformInt.of(1, 1), 0F, 1, 0.6f, UniformInt.of(4, 6), 0F);
        public static final ConfiguredFeature<?, ?> UNDERGROUND_LICHEN = Feature.VEGETATION_PATCH.configured(UNDERGROUND_LICHEN_CONFIG);
        public static final PlacedFeature UNDERGROUND_LICHEN_PLACED = UNDERGROUND_LICHEN.placed(CountPlacement.of(100), InSquarePlacement.spread(), UG_HEIGHT, EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());

        //Mushroom Sprouts
        public static final RandomPatchConfiguration MUSHROOM_SPROUTS_CONFIG = FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.MYCELIUM_SPROUTS))), List.of(), 9);
        public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_SPROUTS = Feature.RANDOM_PATCH.configured(MUSHROOM_SPROUTS_CONFIG);
        public static final PlacedFeature SPROUTS_PLACED = MUSHROOM_FIELD_SPROUTS.placed(CountPlacement.of(10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

        //Mushroom Roots
        public static final RandomPatchConfiguration MUSHROOM_ROOTS_CONFIG = FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.MYCELIUM_ROOTS))), List.of(), 5);
        public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_ROOTS = Feature.RANDOM_PATCH.configured(MUSHROOM_ROOTS_CONFIG);
        public static final PlacedFeature ROOTS_PLACED = MUSHROOM_FIELD_ROOTS.placed(CountPlacement.of(7), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

        //Underground Huge Shroomies
        public static final PlacedFeature UNDERGROUND_HUGE_BROWN_SHROOM_PLACED = TreeFeatures.HUGE_BROWN_MUSHROOM.placed(CountPlacement.of(10), InSquarePlacement.spread(), UG_HEIGHT);
        public static final PlacedFeature UNDERGROUND_HUGE_RED_SHROOM_PLACED = TreeFeatures.HUGE_RED_MUSHROOM.placed(CountPlacement.of(10), InSquarePlacement.spread(), UG_HEIGHT);

        //Underground Huge Glowshroomies
        public static final Feature<HugeMushroomFeatureConfiguration> HUGE_PURPLE_GLOWSHROOM_CONFIG = new HugePurpleGlowshroomFeature(HugeMushroomFeatureConfiguration.CODEC);
        public static final ConfiguredFeature<?, ?> HUGE_PURPLE_GLOWSHROOM = HUGE_PURPLE_GLOWSHROOM_CONFIG.configured(new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(BMBlocks.PURPLE_GLOWSHROOM_BLOCK), BlockStateProvider.simple(BMBlocks.GLOWSHROOM_STEM), 1));
        public static final PlacedFeature HUGE_PURPLE_GLOWSHROOM_PLACED = HUGE_PURPLE_GLOWSHROOM.placed();

        public static final Feature<HugeMushroomFeatureConfiguration> HUGE_GREEN_GLOWSHROOM_CONFIG = new HugeGreenGlowshroomFeature(HugeMushroomFeatureConfiguration.CODEC);
        public static final ConfiguredFeature<?, ?> HUGE_GREEN_GLOWSHROOM = HUGE_GREEN_GLOWSHROOM_CONFIG.configured(new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(BMBlocks.GREEN_GLOWSHROOM_BLOCK), BlockStateProvider.simple(BMBlocks.GLOWSHROOM_STEM), 1));
        public static final PlacedFeature HUGE_GREEN_GLOWSHROOM_PLACED = HUGE_GREEN_GLOWSHROOM.placed();

        public static final ConfiguredFeature<?, ?> UNDERGROUND_GLOWSHROOMS = Feature.SIMPLE_RANDOM_SELECTOR.configured(new SimpleRandomFeatureConfiguration(List.of(()->HUGE_GREEN_GLOWSHROOM_PLACED, ()->HUGE_PURPLE_GLOWSHROOM_PLACED)));
        public static final PlacedFeature UNDERGROUND_GLOWSHROOMS_PLACED = UNDERGROUND_GLOWSHROOMS.placed(CountPlacement.of(120), InSquarePlacement.spread(), UG_HEIGHT);

        public static final Feature<HugeMushroomFeatureConfiguration> HUGE_ORANGE_GLOWSHROOM_FEATURE = new HugeOrangeGlowshroomFeature(HugeMushroomFeatureConfiguration.CODEC);
        public static final ConfiguredFeature<?, ?> HUGE_ORANGE_GLOWSHROOM = HUGE_ORANGE_GLOWSHROOM_FEATURE.configured(new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(BMBlocks.ORANGE_GLOWSHROOM_BLOCK), BlockStateProvider.simple(BMBlocks.GLOWSHROOM_STEM), 1));

        //Underground small Shroomies
        public static final ConfiguredFeature<?, ?> GREEN_GLOWSHROOM = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.GREEN_GLOWSHROOM)))));
        public static final PlacedFeature GREEN_GLOWSHROOM_PLACED = GREEN_GLOWSHROOM.placed(CountPlacement.of(2), InSquarePlacement.spread(), UG_HEIGHT);

        public static final ConfiguredFeature<?, ?> PURPLE_GLOWSHROOM = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.PURPLE_GLOWSHROOM)))));
        public static final PlacedFeature PURPLE_GLOWSHROOM_PLACED = PURPLE_GLOWSHROOM.placed(CountPlacement.of(2), InSquarePlacement.spread(), UG_HEIGHT);

        //Orange Shroomies
        public static final Feature<ProbabilityFeatureConfiguration> ORANGE_GLOWSHROOM_FEATURE = new OrangeGlowshroomFeature(ProbabilityFeatureConfiguration.CODEC);
        public static final ConfiguredFeature<?, ?> ORANGE_GLOWSHROOM = ORANGE_GLOWSHROOM_FEATURE.configured(new ProbabilityFeatureConfiguration(0.1F));
        public static final PlacedFeature ORANGE_GLOWSHROOM_PLACED = ORANGE_GLOWSHROOM.placed(CountPlacement.of(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR);

        //Tall Shroomies
        public static final ConfiguredFeature<?, ?> TALL_BROWN_MUSHROOMS = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.TALL_BROWN_MUSHROOM))), List.of(), 20));
        public static final ConfiguredFeature<?, ?> TALL_RED_MUSHROOMS = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(BMBlocks.TALL_RED_MUSHROOM))), List.of(), 20));
        public static final PlacedFeature TALL_BROWN_MUSHROOMS_PLACED = TALL_BROWN_MUSHROOMS.placed(RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);
        public static final PlacedFeature TALL_RED_MUSHROOMS_PLACED = TALL_RED_MUSHROOMS.placed(RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE);

        //Blighted Balsa
        public static final TrunkPlacerType<BalsaTrunkPlacer> BLIGHTED_BALSA_TRUNK = TrunkPlacerTypeInvoker.callRegister(BiomeMakeover.ID("blighted_balse").toString(), BalsaTrunkPlacer.CODEC);
        public static final ConfiguredFeature<TreeConfiguration, ?> BLIGHTED_BALSA = Feature.TREE.configured(new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BMBlocks.BLIGHTED_BALSA_WOOD_INFO.getBlock(WoodTypeInfo.Type.LOG)),
                new BalsaTrunkPlacer(4, 6, 8),
                BlockStateProvider.simple(BMBlocks.BLIGHTED_BALSA_LEAVES),
                new AcaciaFoliagePlacer(UniformInt.of(1, 1), ConstantInt.of(0)),
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
                ).ignoreVines().build());

        public static final PlacedFeature BLIGHTED_BALSA_CHECKED = BLIGHTED_BALSA.filteredByBlockSurvival(BMBlocks.BLIGHTED_BALSA_SAPLING);
        public static final ConfiguredFeature<?, ?> BLIGHTED_BALSA_FILTERED =  Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(BLIGHTED_BALSA_CHECKED, 0.8f)), BLIGHTED_BALSA_CHECKED));
        public static final PlacedFeature BLIGHTED_BALSA_TREES_PLACED =BLIGHTED_BALSA_FILTERED.placed(VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(12)));

        public static void init()
        {
            RegistryHelper.register(Constants.MOD_ID, Registry.FEATURE, Feature.class, MushroomFields.class);

            RegistryHelper.gatherFields(Constants.MOD_ID, ConfiguredFeature.class, MushroomFields.class, CFG_FEATURES);
            RegistryHelper.gatherFields(Constants.MOD_ID, PlacedFeature.class, MushroomFields.class, PL_FEATURES);

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
                    SPROUTS_PLACED,
                    ROOTS_PLACED,
                    UNDERGROUND_GLOWSHROOMS_PLACED,
                    UNDERGROUND_HUGE_BROWN_SHROOM_PLACED,
                    UNDERGROUND_HUGE_RED_SHROOM_PLACED,
                    GREEN_GLOWSHROOM_PLACED,
                    PURPLE_GLOWSHROOM_PLACED,
                    ORANGE_GLOWSHROOM_PLACED,
                    TALL_BROWN_MUSHROOMS_PLACED,
                    TALL_RED_MUSHROOMS_PLACED,
                    DarkForest.WILD_MUSHROOMS_PLACED
            ));
        }
    }
    private static final List<Pair<ResourceLocation, ConfiguredFeature>> CFG_FEATURES = Lists.newArrayList();
    private static final List<Pair<ResourceLocation, PlacedFeature>> PL_FEATURES = Lists.newArrayList();

    private static boolean registered = false;
    public static Map<GenerationStep.Decoration, List<PlacedFeature>> MUSHROOM_GEN = Maps.newHashMap();
    public static Map<GenerationStep.Decoration, List<PlacedFeature>> BADLANDS_GEN = Maps.newHashMap();
    public static List<ConfiguredStructureFeature<?, ?>> BADLANDS_STRUCTURES = Lists.newArrayList();
    public static Map<GenerationStep.Decoration, List<PlacedFeature>> SWAMP_GEN = Maps.newHashMap();
    public static List<ConfiguredStructureFeature<?, ?>> SWAMP_STRUCTURES = Lists.newArrayList();
    public static Map<GenerationStep.Decoration, List<PlacedFeature>> DF_GEN = Maps.newHashMap();
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

    @ExpectPlatform
    public static void registerStructure(String name, StructureFeature<?> feature, StructureFeatureConfiguration cfg, boolean terraform)
    {
        throw new AssertionError();
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
}
