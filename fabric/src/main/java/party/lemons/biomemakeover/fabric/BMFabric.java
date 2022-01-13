package party.lemons.biomemakeover.fabric;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.fabric.api.biome.v1.*;
import net.fabricmc.api.ModInitializer;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.BiomeMakeoverClient;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class BMFabric implements ModInitializer
{
    @Override
    public void onInitialize() {
        BiomeMakeover.init();
        if (Platform.getEnvironment() == Env.CLIENT)
            BiomeMakeoverClient.init();

        BMEntities.registerModels();

        BMEffects.registerParticleProvider();
        doWorldGen();
    }

    public void doWorldGen()
    {
        final Predicate<BiomeSelectionContext> MUSHROOM_BIOMES = BiomeSelectors.categories(Biome.BiomeCategory.MUSHROOM);
        final Predicate<BiomeSelectionContext> BADLANDS_BIOMES = BiomeSelectors.categories(Biome.BiomeCategory.MESA);
        final Predicate<BiomeSelectionContext> SWAMP_BIOMES = BiomeSelectors.categories(Biome.BiomeCategory.SWAMP);
        final Predicate<BiomeSelectionContext> DF_BIOMES = BiomeSelectors.includeByKey(Biomes.DARK_FOREST);

        BiomeModification gen = BiomeModifications.create(BiomeMakeover.ID("biomemakeover"));

        addBiomeFeatures(gen, BADLANDS_BIOMES, BMWorldGen.BADLANDS_GEN);
        addBiomeStructures(gen, BADLANDS_BIOMES, BMWorldGen.BADLANDS_STRUCTURES);

        addBiomeFeatures(gen, MUSHROOM_BIOMES, BMWorldGen.MUSHROOM_GEN);

        addBiomeFeatures(gen, SWAMP_BIOMES, BMWorldGen.SWAMP_GEN);
        addBiomeStructures(gen, SWAMP_BIOMES, BMWorldGen.SWAMP_STRUCTURES);

        addBiomeFeatures(gen, DF_BIOMES, BMWorldGen.DF_GEN);
        addBiomeStructures(gen, DF_BIOMES, BMWorldGen.DF_STRUCTURES);

        //TODO: Some sort of removal system that works together with forge?
        gen.add(ModificationPhase.REMOVALS, SWAMP_BIOMES, (biomeSelectionContext, ctx) -> {
            ctx.getGenerationSettings().removeBuiltInFeature(VegetationPlacements.TREES_SWAMP);
        });
    }

    private void addBiomeStructures(BiomeModification gen, Predicate<BiomeSelectionContext> biomes, List<ConfiguredStructureFeature<?,?>> structures)
    {
        for(ConfiguredStructureFeature<?, ?> structureFeature : structures)
        {
            gen.add(ModificationPhase.ADDITIONS, biomes, ctx->{
                Optional<ResourceKey<ConfiguredStructureFeature<?,?>>> k =  BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getResourceKey(structureFeature);
                ctx.getGenerationSettings().addStructure(k.get());
            });
        }
    }

    private void addBiomeFeatures(BiomeModification gen, Predicate<BiomeSelectionContext> biomes, Map<GenerationStep.Decoration, List<PlacedFeature>> features)
    {
        for(GenerationStep.Decoration step : features.keySet())
        {
            for(PlacedFeature feature : features.get(step))
                gen.add(ModificationPhase.ADDITIONS, biomes,
                        ctx -> ctx.getGenerationSettings().addBuiltInFeature(step, feature)
                );
        }
    }


    public static ResourceKey<ConfiguredWorldCarver<?>> rk(ConfiguredWorldCarver carver)
    {
        return BuiltinRegistries.CONFIGURED_CARVER.getResourceKey(carver).get();
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> rk(ConfiguredFeature carver)
    {
        return BuiltinRegistries.CONFIGURED_FEATURE.getResourceKey(carver).get();
    }

    public static ResourceKey<ConfiguredStructureFeature<?, ?>> rk(ConfiguredStructureFeature carver)
    {
        return BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getResourceKey(carver).get();
    }

    //public static ResourceKey<PlacedFeature> rk(PlacedFeature carver)
    //{
        //Optional<ResourceKey<PlacedFeature>> k =  BuiltinRegistries.PLACED_FEATURE.getResourceKey(carver);

      //  return k.get();
    //}
}
