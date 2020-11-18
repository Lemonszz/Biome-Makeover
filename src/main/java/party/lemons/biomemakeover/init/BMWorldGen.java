package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.foliage.PineFoliagePlacer;
import net.minecraft.world.gen.placer.DoublePlantPlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import party.lemons.biomemakeover.util.RegistryHelper;
import party.lemons.biomemakeover.util.WoodTypeInfo;
import party.lemons.biomemakeover.world.carver.LargeMyceliumCaveCarver;
import party.lemons.biomemakeover.world.feature.*;
import party.lemons.biomemakeover.world.feature.config.GrassPatchFeatureConfig;
import party.lemons.biomemakeover.world.feature.foliage.BalsaTrunkPlacer;

import java.util.OptionalInt;
import java.util.function.Predicate;

import static net.minecraft.world.gen.GenerationStep.Carver.AIR;
import static net.minecraft.world.gen.GenerationStep.Feature.*;

public class BMWorldGen
{
	private static final Predicate<BiomeSelectionContext> MUSHROOM_BIOMES = BiomeSelectors.categories(Biome.Category.MUSHROOM);
	private static final Predicate<BiomeSelectionContext> BADLANDS_BIOMES = BiomeSelectors.categories(Biome.Category.MESA);

	private static void doModifications()
	{
		mushroomModifications();
		badlandsModifications();
	}

	private static void mushroomModifications()
	{
		BiomeModifications.addCarver(MUSHROOM_BIOMES, AIR, rk(LARGE_CAVE_CONFIGURED_CARVER));
		BiomeModifications.addFeature(MUSHROOM_BIOMES, UNDERGROUND_DECORATION, rk(MYCELIUM_PATCH));

		BiomeModifications.addFeature(MUSHROOM_BIOMES, VEGETAL_DECORATION, rk(BLIGHTED_BALSA_TREES));
		BiomeModifications.addFeature(MUSHROOM_BIOMES, VEGETAL_DECORATION, rk(MUSHROOM_FIELD_UNDERGROUND));
		BiomeModifications.addFeature(MUSHROOM_BIOMES, VEGETAL_DECORATION, rk(MUSHROOM_FIELD_ROOTS_UNDERGROUND));
		BiomeModifications.addFeature(MUSHROOM_BIOMES, VEGETAL_DECORATION, rk(MUSHROOM_FIELD_SPROUTS_UNDERGROUND));
		BiomeModifications.addFeature(MUSHROOM_BIOMES, VEGETAL_DECORATION, rk(MUSHROOM_FIELD_GLOWSHROOMS));
		BiomeModifications.addFeature(MUSHROOM_BIOMES, VEGETAL_DECORATION, rk(MUSHROOM_FIELD_UNDERGROUND_SHROOMS));
		BiomeModifications.addFeature(MUSHROOM_BIOMES, VEGETAL_DECORATION, rk(MUSHROOM_FIELD_SPROUTS));
		BiomeModifications.addFeature(MUSHROOM_BIOMES, VEGETAL_DECORATION, rk(MUSHROOM_FIELD_ROOTS));
		BiomeModifications.addFeature(MUSHROOM_BIOMES, VEGETAL_DECORATION, rk(MUSHROOM_FIELD_UNDERGROUND_GLOWSHROOMS));
		BiomeModifications.addFeature(MUSHROOM_BIOMES, VEGETAL_DECORATION, rk(ORANGE_GLOWSHROOM_PATCH));
		BiomeModifications.addFeature(MUSHROOM_BIOMES, VEGETAL_DECORATION, rk(MUSHROOM_FIELD_TALL_SHROOMS));
		BiomeModifications.addFeature(MUSHROOM_BIOMES, VEGETAL_DECORATION, rk(MUSHROOM_FIELD_TALL_SHROOMS_UNDERGROUND));

		BiomeModifications.addSpawn(MUSHROOM_BIOMES, SpawnGroup.AMBIENT, BMEntities.MUSHROOM_TRADER, 1, 1, 1);
		BiomeModifications.addSpawn(MUSHROOM_BIOMES, SpawnGroup.AMBIENT, BMEntities.BLIGHTBAT, 5, 1, 1);
		BiomeModifications.addSpawn(MUSHROOM_BIOMES, SpawnGroup.WATER_AMBIENT, BMEntities.GLOWFISH, 7, 2, 7);
	}

	private static void badlandsModifications()
	{
		BiomeModifications.addStructure(BADLANDS_BIOMES, rk(BMStructures.GHOST_TOWN_CONFIGURED));

		BiomeModifications.addFeature(BADLANDS_BIOMES, SURFACE_STRUCTURES, rk(SURFACE_FOSSIL));
		BiomeModifications.addFeature(BADLANDS_BIOMES, LOCAL_MODIFICATIONS, rk(SAGUARO_CACTUS));
		BiomeModifications.addFeature(BADLANDS_BIOMES, UNDERGROUND_DECORATION, rk(PAY_DIRT));
		BiomeModifications.addFeature(BADLANDS_BIOMES, VEGETAL_DECORATION, rk(BARREL_CACTUS));

		BiomeModifications.addSpawn(BADLANDS_BIOMES, SpawnGroup.CREATURE, BMEntities.SCUTTLER, 4, 1, 2);
	}


	//Structure

	//Configs
	public static final RandomPatchFeatureConfig MUSHROOM_SPROUTS_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.MYCELIUM_SPROUTS.getDefaultState()), SimpleBlockPlacer.INSTANCE)).tries(32).build();
	public static final RandomPatchFeatureConfig MUSHROOM_SPROUTS_UNDERGROUND_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.MYCELIUM_SPROUTS.getDefaultState()), SimpleBlockPlacer.INSTANCE)).cannotProject().tries(32).build();
	public static final RandomPatchFeatureConfig MUSHROOM_ROOTS_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.MYCELIUM_ROOTS.getDefaultState()), SimpleBlockPlacer.INSTANCE)).tries(20).build();
	public static final RandomPatchFeatureConfig MUSHROOM_ROOTS_UNDERGROUND_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.MYCELIUM_ROOTS.getDefaultState()), SimpleBlockPlacer.INSTANCE)).cannotProject().tries(20).build();
	public static final GrassPatchFeatureConfig UNDERGROUND_MYCELIUM_CONFIG = new GrassPatchFeatureConfig(new SimpleBlockStateProvider(Blocks.MYCELIUM.getDefaultState()), new SimpleBlockStateProvider(Blocks.DIRT.getDefaultState()), 8);

	//Carvers
	public static final LargeMyceliumCaveCarver LARGE_MYCELIUM_CAVE_CARVER = new LargeMyceliumCaveCarver(ProbabilityConfig.CODEC, 256);
	public static final ConfiguredCarver<ProbabilityConfig> LARGE_CAVE_CONFIGURED_CARVER = LARGE_MYCELIUM_CAVE_CARVER.method_28614(new ProbabilityConfig(0.15F));

	//Features
	public static final UndergroundHugeBrownMushroomFeature UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE = new UndergroundHugeBrownMushroomFeature(HugeMushroomFeatureConfig.CODEC);
	public static final UndergroundHugeRedMushroomFeature UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE = new UndergroundHugeRedMushroomFeature(HugeMushroomFeatureConfig.CODEC);
	public static final UndergroundHugePurpleGlowshroomFeature UNDERGROUND_HUGE_PURPLE_GLOWSHROOM_FEATURE = new UndergroundHugePurpleGlowshroomFeature(HugeMushroomFeatureConfig.CODEC);
	public static final UndergroundHugeGreenGlowshroomFeature UNDERGROUND_HUGE_GREEN_GLOWSHROOM_FEATURE = new UndergroundHugeGreenGlowshroomFeature(HugeMushroomFeatureConfig.CODEC);
	public static final HugeOrangeGlowshroomFeature HUGE_ORANGE_GLOWSHROOM_FEATURE = new HugeOrangeGlowshroomFeature(HugeMushroomFeatureConfig.CODEC);
	public static final GrassPatchFeature UNDERGROUND_MYCELIUM = new GrassPatchFeature(GrassPatchFeatureConfig.CODEC);
	public static final OrangeMushroomFeature ORANGE_MUSHROOM_FEATURE = new OrangeMushroomFeature(ProbabilityConfig.CODEC);

	//Tree
	public static final TrunkPlacerType<BalsaTrunkPlacer> BLIGHTED_BALDA_TRUNK = new TrunkPlacerType<>(BalsaTrunkPlacer.CODEC);
	public static final ConfiguredFeature<TreeFeatureConfig, ?> BLIGHTED_BALSA = Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.BLIGHTED_BALSA_WOOD_INFO.get(WoodTypeInfo.Type.LOG).getDefaultState()), new SimpleBlockStateProvider(BMBlocks.BLIGHTED_BALSA_LEAVES.getDefaultState()), new PineFoliagePlacer(UniformIntDistribution.of(1), UniformIntDistribution.of(1), UniformIntDistribution.of(3, 1)), new BalsaTrunkPlacer(4, 6, 8), new ThreeLayersFeatureSize(1, 1, 0, 1, 2,OptionalInt.empty()))).ignoreVines().build());
	public static final ConfiguredFeature<?, ?> BLIGHTED_BALSA_TREES = BLIGHTED_BALSA.applyChance(15);

	//Badlands
	public static final SurfaceFossilFeature SURFACE_FOSSIL_FEATURE = new SurfaceFossilFeature(DefaultFeatureConfig.CODEC);
	public static final ConfiguredFeature<?,?> SURFACE_FOSSIL = SURFACE_FOSSIL_FEATURE.configure(DefaultFeatureConfig.DEFAULT).applyChance(100);
	public static final SaguaroCactusFeature SAGUARO_CACTUS_FEATURE = new SaguaroCactusFeature(DefaultFeatureConfig.CODEC);
	public static final ConfiguredFeature<?, ?> SAGUARO_CACTUS = SAGUARO_CACTUS_FEATURE.configure(DefaultFeatureConfig.DEFAULT).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).applyChance(12);
	public static final PayDirtFeature PAY_DIRT_FEATURE = new PayDirtFeature(DefaultFeatureConfig.CODEC);
	public static final ConfiguredFeature<?, ?> PAY_DIRT = PAY_DIRT_FEATURE.configure(DefaultFeatureConfig.DEFAULT).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(30, 0, 80))).spreadHorizontally().repeat(10);
	public static final ConfiguredFeature<?, ?> BARREL_CACTUS = Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.BARREL_CACTUS_FLOWERED.getDefaultState()), SimpleBlockPlacer.INSTANCE)).tries(6).build());

	//Conf Features
	//2 tall Shrooms patch
	public static final ConfiguredFeature<?,?> TALL_RED_MUSHROOM_PATCH = Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.TALL_RED_MUSHROOM.getDefaultState()), new DoublePlantPlacer())).tries(64).cannotProject().build());
	public static final ConfiguredFeature<?,?> TALL_BROWN_MUSHROOM_PATCH = Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.TALL_BROWN_MUSHROOM.getDefaultState()), new DoublePlantPlacer())).tries(64).cannotProject().build());

	//Shroom roots/sprouts
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_SPROUTS = Feature.RANDOM_PATCH.configure(MUSHROOM_SPROUTS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(2);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_SPROUTS_UNDERGROUND = Feature.RANDOM_PATCH.configure(MUSHROOM_SPROUTS_UNDERGROUND_CONFIG).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 55))).spreadHorizontally().repeat(20);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_ROOTS = Feature.RANDOM_PATCH.configure(MUSHROOM_ROOTS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(2);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_ROOTS_UNDERGROUND = Feature.RANDOM_PATCH.configure(MUSHROOM_ROOTS_UNDERGROUND_CONFIG).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 55))).spreadHorizontally().repeat(20);

	//Underground mycelium patches
	public static final ConfiguredFeature<?, ?> MYCELIUM_PATCH = UNDERGROUND_MYCELIUM.configure(UNDERGROUND_MYCELIUM_CONFIG).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 55))).spreadHorizontally().repeatRandomly(10);

	//Lil shrooms patch
	public static final ConfiguredFeature<?, ?> PURPLE_GLOWSHROOM_PATCH = Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.PURPLE_GLOWSHROOM.getDefaultState()), SimpleBlockPlacer.INSTANCE).tries(64).cannotProject().build()));
	public static final ConfiguredFeature<?, ?> GREEN_GLOWSHROOM_PATCH = Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.GREEN_GLOWSHROOM.getDefaultState()), SimpleBlockPlacer.INSTANCE).tries(64).cannotProject().build()));
	public static final ConfiguredFeature<?, ?> ORANGE_GLOWSHROOM_PATCH = ORANGE_MUSHROOM_FEATURE.configure(new ProbabilityConfig(0.06F)).decorate(ConfiguredFeatures.Decorators.SQUARE_TOP_SOLID_HEIGHTMAP).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 60))).repeat(2);

	//
	public static final ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeRedMushroomFeature> UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE_CONFIGURED = (ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeRedMushroomFeature>) UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE.configure(new HugeMushroomFeatureConfig(new SimpleBlockStateProvider(Blocks.RED_MUSHROOM_BLOCK.getDefaultState()), new SimpleBlockStateProvider(Blocks.MUSHROOM_STEM.getDefaultState()), 2));
	public static final ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeBrownMushroomFeature> UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE_CONFIGURED = (ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeBrownMushroomFeature>) UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE.configure(new HugeMushroomFeatureConfig(new SimpleBlockStateProvider(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState()), new SimpleBlockStateProvider(Blocks.MUSHROOM_STEM.getDefaultState()), 3));
	public static final ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugePurpleGlowshroomFeature> UNDERGROUND_HUGE_PURPLE_GLOWSHROOM_FEATURE_CONFIGURED = (ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugePurpleGlowshroomFeature>) UNDERGROUND_HUGE_PURPLE_GLOWSHROOM_FEATURE.configure(new HugeMushroomFeatureConfig(new SimpleBlockStateProvider(BMBlocks.PURPLE_GLOWSHROOM_BLOCK.getDefaultState()), new SimpleBlockStateProvider(BMBlocks.GLOWSHROOM_STEM.getDefaultState()), 1));
	public static final ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeGreenGlowshroomFeature> UNDERGROUND_HUGE_GREEN_GLOWSHROOM_FEATURE_CONFIGURED = (ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeGreenGlowshroomFeature>) UNDERGROUND_HUGE_GREEN_GLOWSHROOM_FEATURE.configure(new HugeMushroomFeatureConfig(new SimpleBlockStateProvider(BMBlocks.GREEN_GLOWSHROOM_BLOCK.getDefaultState()), new SimpleBlockStateProvider(BMBlocks.GLOWSHROOM_STEM.getDefaultState()), 2));
	public static final ConfiguredFeature<HugeMushroomFeatureConfig, HugeOrangeGlowshroomFeature> HUGE_ORANGE_GLOWSHROOM_FEATURE_CONFIGURED = (ConfiguredFeature<HugeMushroomFeatureConfig, HugeOrangeGlowshroomFeature>) HUGE_ORANGE_GLOWSHROOM_FEATURE.configure(new HugeMushroomFeatureConfig(new SimpleBlockStateProvider(BMBlocks.ORANGE_GLOWSHROOM_BLOCK.getDefaultState()), new SimpleBlockStateProvider(BMBlocks.GLOWSHROOM_STEM.getDefaultState()), 1));

	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_UNDERGROUND = Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE_CONFIGURED, () ->UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE_CONFIGURED)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 55))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(100);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_GLOWSHROOMS = Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->UNDERGROUND_HUGE_GREEN_GLOWSHROOM_FEATURE_CONFIGURED, () ->UNDERGROUND_HUGE_PURPLE_GLOWSHROOM_FEATURE_CONFIGURED)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 55))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(100);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_TALL_SHROOMS = Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->TALL_BROWN_MUSHROOM_PATCH, () ->TALL_RED_MUSHROOM_PATCH)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(4);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_TALL_SHROOMS_UNDERGROUND = Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->TALL_BROWN_MUSHROOM_PATCH, () ->TALL_RED_MUSHROOM_PATCH)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 55))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).applyChance(3);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_UNDERGROUND_SHROOMS = Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->ConfiguredFeatures.PATCH_BROWN_MUSHROOM, () ->ConfiguredFeatures.PATCH_RED_MUSHROOM)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 55))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(40);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_UNDERGROUND_GLOWSHROOMS = Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->PURPLE_GLOWSHROOM_PATCH, () ->GREEN_GLOWSHROOM_PATCH)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 55))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).applyChance(30).repeat(12);

	public static final StructureProcessorType<GhostTownFeature.GhostTownLootProcessor> GHOST_TOWN_PROCESSOR = ()->GhostTownFeature.GhostTownLootProcessor.CODEC;

	public static void init()
	{
		RegistryHelper.register(Registry.STRUCTURE_PIECE, StructurePieceType.class, BMWorldGen.class);
		RegistryHelper.register(Registry.STRUCTURE_PROCESSOR, StructureProcessorType.class, BMWorldGen.class);
		RegistryHelper.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ConfiguredStructureFeature.class, BMWorldGen.class);
		RegistryHelper.register(Registry.FEATURE, Feature.class, BMWorldGen.class);
		RegistryHelper.register(Registry.CARVER, Carver.class, BMWorldGen.class);
		RegistryHelper.register(BuiltinRegistries.CONFIGURED_FEATURE, ConfiguredFeature.class, BMWorldGen.class);
		RegistryHelper.register(BuiltinRegistries.CONFIGURED_CARVER, ConfiguredCarver.class, BMWorldGen.class);
		RegistryHelper.register(Registry.TRUNK_PLACER_TYPE, TrunkPlacerType.class, BMWorldGen.class);

		doModifications();
	}

	public static RegistryKey<ConfiguredCarver<?>> rk(ConfiguredCarver carver)
	{
		return BuiltinRegistries.CONFIGURED_CARVER.getKey(carver).get();
	}

	public static RegistryKey<ConfiguredFeature<?, ?>> rk(ConfiguredFeature carver)
	{
		return BuiltinRegistries.CONFIGURED_FEATURE.getKey(carver).get();
	}

	public static RegistryKey<ConfiguredStructureFeature<?, ?>> rk(ConfiguredStructureFeature carver)
	{
		return BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getKey(carver).get();
	}
}
