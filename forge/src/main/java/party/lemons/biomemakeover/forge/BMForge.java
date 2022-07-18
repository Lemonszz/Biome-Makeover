package party.lemons.biomemakeover.forge;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.architectury.hooks.item.tool.HoeItemHooks;
import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import party.lemons.biomemakeover.BiomeMakeover;
import net.minecraftforge.fml.common.Mod;
import party.lemons.biomemakeover.BiomeMakeoverClient;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.level.particle.BlossomParticle;
import party.lemons.biomemakeover.level.particle.LightningSparkParticle;
import party.lemons.biomemakeover.level.particle.PoltergeistParticle;
import party.lemons.biomemakeover.level.particle.TeleportParticle;
import party.lemons.biomemakeover.util.loot.BMLootTableInjection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mod(Constants.MOD_ID)
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BMForge
{
    public BMForge()
    {
        EventBuses.registerModEventBus(Constants.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(BMForge::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BMForge::commonSetup);

        BiomeMakeover.init();
        if (Platform.getEnvironment() == Env.CLIENT) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(BMForge::particleSetup);
            BiomeMakeoverClient.init();
        }
        BMEntities.registerModels();
    }

    @SubscribeEvent
    public static void lootLoad(LootTableLoadEvent event)
    {
        for(BMLootTableInjection.InjectedItem item : BMLootTableInjection.getInsertedEntries())
        {
            if(event.getName().equals(item.table()))
            {
                event.getTable().addPool(LootPool.lootPool()
                        .setRolls(item.rolls())
                        .add(LootItem.lootTableItem(item.itemLike())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0f, 2.0f)))).when(LootItemKilledByPlayerCondition.killedByPlayer()).build());

            }
        }
    }

    public static void commonSetup(FMLCommonSetupEvent event)
    {
    }

    public static void clientSetup(FMLClientSetupEvent event)
    {
        if (Platform.getEnvironment() == Env.CLIENT) {
            BiomeMakeoverClient.init();
        }
    }

    public static void particleSetup(ParticleFactoryRegisterEvent event)
    {
        Minecraft.getInstance().particleEngine.register(BMEffects.LIGHTNING_SPARK.get(), LightningSparkParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(BMEffects.POLTERGEIST.get(), PoltergeistParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(BMEffects.BLOSSOM.get(), BlossomParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(BMEffects.TELEPORT.get(), TeleportParticle.Provider::new);
        //TODO: Look into why arch method crashes?
    }

    private static final ResourceLocation DARK_FOREST = new ResourceLocation("dark_forest");
    @SubscribeEvent
    public static void addGen(BiomeLoadingEvent event)
    {
        addBiomeFeatures(event.getCategory() == Biome.BiomeCategory.MUSHROOM, event.getGeneration(), BMWorldGen.MUSHROOM_GEN);
        addBiomeFeatures(event.getCategory() == Biome.BiomeCategory.MESA, event.getGeneration(), BMWorldGen.BADLANDS_GEN);
        addBiomeFeatures(event.getCategory() == Biome.BiomeCategory.SWAMP, event.getGeneration(), BMWorldGen.SWAMP_GEN);
        addBiomeFeatures(event.getName().equals(DARK_FOREST), event.getGeneration(), BMWorldGen.DF_GEN);
    }

    private static void addBiomeFeatures(boolean doAdd, BiomeGenerationSettingsBuilder generation, Map<GenerationStep.Decoration, List<Holder<PlacedFeature>>> features)
    {
        if(doAdd) {
            for (GenerationStep.Decoration step : features.keySet()) {
                for (Holder<PlacedFeature> feature : features.get(step))
                    generation.addFeature(step, feature);
            }
        }
    }
}
