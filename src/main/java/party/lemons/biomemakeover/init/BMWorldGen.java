package party.lemons.biomemakeover.init;

import net.minecraft.block.Blocks;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.world.carver.LargeCaveCarver;
import party.lemons.biomemakeover.world.feature.UndergroundHugeBrownMushroomFeature;
import party.lemons.biomemakeover.world.feature.UndergroundHugeRedMushroomFeature;

public class BMWorldGen
{
	public static LargeCaveCarver LARGE_CAVE_CARVER = new LargeCaveCarver(ProbabilityConfig.CODEC, 256);
	public static ConfiguredCarver<ProbabilityConfig> LARGE_CAVE_CONFIGURIED_CARVER = LARGE_CAVE_CARVER.method_28614(new ProbabilityConfig(0.15F));

	public static UndergroundHugeBrownMushroomFeature UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE = new UndergroundHugeBrownMushroomFeature(HugeMushroomFeatureConfig.CODEC);
	public static UndergroundHugeRedMushroomFeature UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE = new UndergroundHugeRedMushroomFeature(HugeMushroomFeatureConfig.CODEC);
	public static ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeRedMushroomFeature> UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE_CONFIGURED = (ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeRedMushroomFeature>) UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE.configure(new HugeMushroomFeatureConfig(new SimpleBlockStateProvider(Blocks.RED_MUSHROOM_BLOCK.getDefaultState()), new SimpleBlockStateProvider(Blocks.MUSHROOM_STEM.getDefaultState()), 2));
	public static ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeBrownMushroomFeature> UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE_CONFIGURED = (ConfiguredFeature<HugeMushroomFeatureConfig, UndergroundHugeBrownMushroomFeature>) UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE.configure(new HugeMushroomFeatureConfig(new SimpleBlockStateProvider(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState()), new SimpleBlockStateProvider(Blocks.MUSHROOM_STEM.getDefaultState()), 3));

	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_UNDERGROUND = registerConfigFeature("mushroom_field_underground", Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE_CONFIGURED, () ->UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE_CONFIGURED)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 64))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(100));
	public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_UNDERGROUND_SHROOMS = registerConfigFeature("mushroom_field_underground_shrooms", Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(() ->ConfiguredFeatures.PATCH_BROWN_MUSHROOM, () ->ConfiguredFeatures.PATCH_RED_MUSHROOM)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 64))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(40));

	public static void init()
	{
		Registry.register(Registry.FEATURE, BiomeMakeover.ID("brown_underground_mushroom"), UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE);
		Registry.register(Registry.FEATURE, BiomeMakeover.ID("red_underground_mushroom"), UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE);
		BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, BiomeMakeover.ID("brown_underground_mushroom"), UNDERGROUND_HUGE_BROWN_MUSHROOM_FEATURE_CONFIGURED);
		BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, BiomeMakeover.ID("red_underground_mushroom"), UNDERGROUND_HUGE_RED_MUSHROOM_FEATURE_CONFIGURED);

		Registry.register(Registry.CARVER, BiomeMakeover.ID("large_cave"), LARGE_CAVE_CARVER);
		BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_CARVER, BiomeMakeover.ID("large_cave"), LARGE_CAVE_CONFIGURIED_CARVER);
	}

	private static <FC extends FeatureConfig> ConfiguredFeature registerConfigFeature(String id, ConfiguredFeature<FC, ?> configuredFeature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, BiomeMakeover.ID(id), configuredFeature);
	}
}
