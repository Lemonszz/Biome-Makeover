package party.lemons.biomemakeover.fabric;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.*;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.BiomeMakeoverClient;
import party.lemons.biomemakeover.init.BMEffects;
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
        if (Platform.getEnvironment() == Env.CLIENT) {
            BiomeMakeoverClient.init();
        }

        BMEffects.registerParticleProvider();

        injectLootTables();
    }

    private void injectLootTables()
    {
        LootTableEvents.MODIFY.register(Event.DEFAULT_PHASE, new LootTableEvents.Modify()
        {
            @Override
            public void modifyLootTable(ResourceManager resourceManager, LootTables lootManager, ResourceLocation id, LootTable.Builder tableBuilder, LootTableSource source)
            {
                for (BMLootTableInjection.InjectedItem item : BMLootTableInjection.getInsertedEntries()) {
                    if (id.equals(item.table())) {
                        tableBuilder.withPool(
                                LootPool.lootPool().setRolls(item.rolls())
                                        .add(LootItem.lootTableItem(item.itemLike())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0f, 2.0f)))).when(LootItemKilledByPlayerCondition.killedByPlayer()));
                    }
                }
            }
        });
    }
}
