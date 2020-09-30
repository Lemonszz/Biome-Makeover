package party.lemons.biomemakeover.init;

import net.minecraft.block.Blocks;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placer.DoublePlantPlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import party.lemons.biomemakeover.util.RegistryHelper;
import party.lemons.biomemakeover.world.carver.LargeMyceliumCaveCarver;
import party.lemons.biomemakeover.world.feature.UndergroundHugeBrownMushroomFeature;
import party.lemons.biomemakeover.world.feature.UndergroundHugePurpleGlowshroomFeature;
import party.lemons.biomemakeover.world.feature.UndergroundHugeRedMushroomFeature;

public class BMWorldGen
{
	public static final RandomPatchFeatureConfig MUSHROOM_SPROUTS_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.MYCELIUM_SPROUTS.getDefaultState()), SimpleBlockPlacer.INSTANCE)).tries(32).build();
	public static final RandomPatchFeatureConfig MUSHROOM_ROOTS_CONFIG = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.MYCELIUM_ROOTS.getDefaultState()), SimpleBlockPlacer.INSTANCE)).tries(20).build();

	public static final LargeMyceliumCaveCarver LARGE_MYCELIUM_CAVE_CARVER = new LargeMyceliumCaveCarver(ProbabilityConfig.CODEC, 256);
	public static final ConfiguredCarver<ProbabilityConfig> LARGE_CAVE_CONFIGURED_CARVER = LARGE_MYCELIUM_CAVE_CARVER.method_28614(new ProbabilityConfig(0.15F));

	public static final UndergroundHugeBrownMushroomFeature UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE = new UndergroundHugeBrownMushroomFeature(HugeMushroomFeatureConfig.CODEC);
	public static final UndergroundHugeRedMushroomFeature UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE = new UndergroundHugeRedMushroomFeature(HugeMushroomFeatureConfig.CODEC);
	public static final UndergroundHugePurpleGlowshroomFeature UNDERGROUND_HUGE_PURPLE_GLOWSHROOM_FEATURE = new UndergroundHugePurpleGlowshroomFeature(HugeMushroomFeatureConfig.CODEC);
	public static final ConfiguredFeature<?,?> TALL_RED_MUSHROOM_PATCH = Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.TALL_RED_MUSHROOM.getDefaultState()), new DoublePlantPlacer())).tries(64).cannotProject().build());
	public static final ConfiguredFeature<?,?> TALL_BROWN_MUSHROOM_PATCH = Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.TALL_BROWN_MUSHROOM.getDefaultState()), new DoublePlantPlacer())).tries(64).cannotProject().build());

	public static final ConfiguredFeature<?, ?> PATCH_PURPLE_GLOWSHROOM = Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BMBlocks.PURPLE_GLOWSHROOM.getDefaultState()), SimpleBlockPlacer.INSTANCE).tries(64).cannotProject().build()));

	public static final ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeRedMushroomFeature> UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE_CONFIGURED = (ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeRedMushroomFeature>) UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE.configure(new HugeMushroomFeatureConfig(new SimpleBlockStateProvider(Blocks.RED_MUSHROOM_BLOCK.getDefaultState()), new SimpleBlockStateProvider(Blocks.MUSHROOM_STEM.getDefaultState()), 2));
	public static final ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeBrownMushroomFeature> UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE_CONFIGURED = (ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeBrownMushroomFeature>) UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE.configure(new HugeMushroomFeatureConfig(new SimpleBlockStateProvider(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState()), new SimpleBlockStateProvider(Blocks.MUSHROOM_STEM.getDefaultState()), 3));
	public static final ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugePurpleGlowshroomFeature> UNDERGROUND_HUGE_PURPLE_GLOWSHROOM_FEATURE_CONFIGURED = (ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugePurpleGlowshroomFeature>) UNDERGROUND_HUGE_PURPLE_GLOWSHROOM_FEATURE.configure(new HugeMushroomFeatureConfig(new SimpleBlockStateProvider(BMBlocks.PURPLE_GLOWSHROOM_BLOCK.getDefaultState()), new SimpleBlockStateProvider(Blocks.MUSHROOM_STEM.getDefaultState()), 1));
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_SPROUTS = Feature.RANDOM_PATCH.configure(MUSHROOM_SPROUTS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(2);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_ROOTS = Feature.RANDOM_PATCH.configure(MUSHROOM_ROOTS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(2);

	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_UNDERGROUND = Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE_CONFIGURED, () ->UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE_CONFIGURED)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 55))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(100);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_GLOWSHROOMS = Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->UNDERGROUND_HUGE_PURPLE_GLOWSHROOM_FEATURE_CONFIGURED, () ->UNDERGROUND_HUGE_PURPLE_GLOWSHROOM_FEATURE_CONFIGURED)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 55))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(100);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_TALL_SHROOMS = Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->TALL_BROWN_MUSHROOM_PATCH, () ->TALL_RED_MUSHROOM_PATCH)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(4);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_TALL_SHROOMS_UNDERGROUND = Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->TALL_BROWN_MUSHROOM_PATCH, () ->TALL_RED_MUSHROOM_PATCH)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 55))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).applyChance(3);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_UNDERGROUND_SHROOMS = Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->ConfiguredFeatures.PATCH_BROWN_MUSHROOM, () ->ConfiguredFeatures.PATCH_RED_MUSHROOM)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 55))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(40);
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_UNDERGROUND_GLOWSHROOMS = Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->PATCH_PURPLE_GLOWSHROOM, () ->PATCH_PURPLE_GLOWSHROOM)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 55))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).applyChance(30).repeat(12);

	public static void init()
	{
		RegistryHelper.register(Registry.FEATURE, Feature.class, BMWorldGen.class);
		RegistryHelper.register(Registry.CARVER, Carver.class, BMWorldGen.class);
		RegistryHelper.register(BuiltinRegistries.CONFIGURED_FEATURE, ConfiguredFeature.class, BMWorldGen.class);
		RegistryHelper.register(BuiltinRegistries.CONFIGURED_CARVER, ConfiguredCarver.class, BMWorldGen.class);
	}
}
