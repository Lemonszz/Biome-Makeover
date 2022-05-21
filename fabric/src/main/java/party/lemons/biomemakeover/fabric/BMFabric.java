package party.lemons.biomemakeover.fabric;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.*;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.BiomeMakeoverClient;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.util.loot.BMLootTableInjection;

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

        LifecycleEvent.SETUP.register(()->{
            doWorldGen();
        });

        injectLootTables();
    }

    private void injectLootTables()
    {
        LootTableLoadingCallback.EVENT.register((resourceManager, manager, id, supplier, setter) -> {
            for(BMLootTableInjection.InjectedItem item : BMLootTableInjection.getInsertedEntries())
            {
                if(id.equals(item.table()))
                {
                    supplier.withPool(LootPool.lootPool()
                            .setRolls(item.rolls())
                            .add(LootItem.lootTableItem(item.itemLike())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0f, 2.0f)))).when(LootItemKilledByPlayerCondition.killedByPlayer()).build());
                }
            }
        });
    }

    public void doWorldGen()
    {
        final Predicate<BiomeSelectionContext> MUSHROOM_BIOMES = BiomeSelectors.tag(ConventionalBiomeTags.MUSHROOM);
        final Predicate<BiomeSelectionContext> BADLANDS_BIOMES = BiomeSelectors.tag(BiomeTags.IS_BADLANDS).or(BiomeSelectors.tag(ConventionalBiomeTags.BADLANDS));
        final Predicate<BiomeSelectionContext> SWAMP_BIOMES = BiomeSelectors.tag(ConventionalBiomeTags.SWAMP);
        final Predicate<BiomeSelectionContext> DF_BIOMES = BiomeSelectors.tag(BMWorldGen.DARK_FOREST_BIOMES);

        BiomeModification gen = BiomeModifications.create(BiomeMakeover.ID("biomemakeover"));

        addBiomeFeatures(gen, BADLANDS_BIOMES, BMWorldGen.BADLANDS_GEN);

        addBiomeFeatures(gen, MUSHROOM_BIOMES, BMWorldGen.MUSHROOM_GEN);
        addBiomeCarvers(gen, MUSHROOM_BIOMES, BMWorldGen.MUSHROOM_CARVERS);

        addBiomeFeatures(gen, SWAMP_BIOMES, BMWorldGen.SWAMP_GEN);

        addBiomeFeatures(gen, DF_BIOMES, BMWorldGen.DF_GEN);

        //TODO: Some sort of removal system that works together with forge?
        gen.add(ModificationPhase.REMOVALS, SWAMP_BIOMES, (biomeSelectionContext, ctx) -> {
            ctx.getGenerationSettings().removeBuiltInFeature(VegetationPlacements.TREES_SWAMP.value());
        });
    }

    private void addBiomeCarvers(BiomeModification gen, Predicate<BiomeSelectionContext> biomes, List<ConfiguredWorldCarver> carvers)
    {
        for(ConfiguredWorldCarver<?> carer : carvers)
        {
            gen.add(ModificationPhase.ADDITIONS, biomes, ctx->{
                Optional<ResourceKey<ConfiguredWorldCarver<?>>> k =  BuiltinRegistries.CONFIGURED_CARVER.getResourceKey(carer);

                ctx.getGenerationSettings().addCarver(GenerationStep.Carving.AIR, k.get());
            });
        }
    }


    private void addBiomeFeatures(BiomeModification gen, Predicate<BiomeSelectionContext> biomes, Map<GenerationStep.Decoration, List<Holder<PlacedFeature>>> features)
    {
        for(GenerationStep.Decoration step : features.keySet())
        {
            for(Holder<PlacedFeature> feature : features.get(step))
                gen.add(ModificationPhase.ADDITIONS, biomes,
                        ctx -> ctx.getGenerationSettings().addBuiltInFeature(step, feature.value())
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

    //public static ResourceKey<PlacedFeature> rk(PlacedFeature carver)
    //{
        //Optional<ResourceKey<PlacedFeature>> k =  BuiltinRegistries.PLACED_FEATURE.getResourceKey(carver);

      //  return k.get();
    //}
}
