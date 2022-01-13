package party.lemons.biomemakeover.forge;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StrongholdConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
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
import party.lemons.biomemakeover.entity.render.BMBoatRender;
import party.lemons.biomemakeover.entity.render.TumbleweedRender;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.level.particle.BlossomParticle;
import party.lemons.biomemakeover.level.particle.LightningSparkParticle;
import party.lemons.biomemakeover.level.particle.PoltergeistParticle;
import party.lemons.biomemakeover.level.particle.TeleportParticle;
import party.lemons.biomemakeover.mixin.forge.ChunkGeneratorAccessor;
import party.lemons.biomemakeover.mixin.forge.StructureSettingsAccessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod(Constants.MOD_ID)
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BMForge
{
    public BMForge()
    {
        EventBuses.registerModEventBus(Constants.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(BMForge::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BMForge::particleSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BMForge::commonSetup);

        BiomeMakeover.init();
        BMEntities.registerModels();
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

    private static void addBiomeFeatures(boolean doAdd, BiomeGenerationSettingsBuilder generation, Map<GenerationStep.Decoration, List<PlacedFeature>> features)
    {
        if(doAdd) {
            for (GenerationStep.Decoration step : features.keySet()) {
                for (PlacedFeature feature : features.get(step))
                    generation.addFeature(step, feature);
            }
        }
    }

    public static void addInjectedStructures(TemporaryBiomeInjection.BiomeInjectionHelper biomeInjectionHelper)
    {
        if(biomeInjectionHelper.biome.getBiomeCategory() == Biome.BiomeCategory.MESA)
            biomeInjectionHelper.addStructure(BMWorldGen.Badlands.GHOST_TOWN_CONFIGURED);

        if(biomeInjectionHelper.biome.getBiomeCategory() == Biome.BiomeCategory.SWAMP)
            biomeInjectionHelper.addStructure(BMWorldGen.Swamp.SUNKEN_RUIN_CONFIGURED);

        if(biomeInjectionHelper.biome.getRegistryName().equals(DARK_FOREST))
            biomeInjectionHelper.addStructure(BMWorldGen.DarkForest.MANSION_CONFIGURED);


        // if(biomeInjectionHelper.biome == OverworldBiomes.darkForest())
       //     biomeInjectionHelper.addStructure(BMWorldGen.Swamp.SUNKEN_RUIN_CONFIGURED);
    }

    /**
     * Shamelessly stolen from Repurposed Stuctures (GNU Lesser General Public License v3.0) By TelepathicGrunt
     * https://github.com/TelepathicGrunt/RepurposedStructures
     */
    //TODO: Use forge/arch methods as avaliable (OR MAKE MY OWN???)

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void deepCopyDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel serverLevel) {
            // Workaround for Terraforged
            ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey(((ChunkGeneratorAccessor) serverLevel.getChunkSource().getGenerator()).bm_getCodec());
            if (cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
            ((ChunkGeneratorAccessor) chunkGenerator).bm_setSettings(deepCopyDimensionStructuresSettings(chunkGenerator.getSettings()));
        }
    }

    @SubscribeEvent
    public static void addStructures(WorldEvent.Load event)
    {
        if (event.getWorld() instanceof ServerLevel serverLevel) {
            StructureSettings worldStructureSettings = serverLevel.getChunkSource().getGenerator().getSettings();

            Map<StructureFeature<?>, Multimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = new HashMap<>();
            ((StructureSettingsAccessor) worldStructureSettings).getConfiguredStructures().forEach((key, value) -> tempStructureToMultiMap.put(key, HashMultimap.create(value)));
            TemporaryBiomeInjection.addStructureToBiomes(tempStructureToMultiMap, serverLevel.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY));

            // Turn the entire map and the inner multimaps to immutable to match the source code's require type
            ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> immutableOuterMap = ImmutableMap.builder();
            tempStructureToMultiMap.forEach((key, value) -> {
                ImmutableMultimap.Builder<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> immutableInnerMultiMap = ImmutableMultimap.builder();
                immutableInnerMultiMap.putAll(value);
                immutableOuterMap.put(key, immutableInnerMultiMap.build());
            });

            // Set it in the field.
            ((StructureSettingsAccessor) worldStructureSettings).setConfiguredStructures(immutableOuterMap.build());


            //////////// DIMENSION BASED STRUCTURE SPAWNING ////////////

            // Workaround for Terraforged
            ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey(((ChunkGeneratorAccessor) serverLevel.getChunkSource().getGenerator()).bm_getCodec());
            if (cgRL != null && cgRL.getNamespace().equals("terraforged")) return;

            //add our structure spacing to all chunkgenerators including modded one and datapack ones.
            // Need temp map as some mods use custom chunk generators with immutable maps in themselves.
            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(worldStructureSettings.structureConfig());

            tempMap.putIfAbsent(BMWorldGen.Badlands.GHOST_TOWN, new StructureFeatureConfiguration(32, 12, 6969));
            tempMap.putIfAbsent(BMWorldGen.Swamp.SUNKEN_RUIN, new StructureFeatureConfiguration(24, 9, 420));
            tempMap.putIfAbsent(BMWorldGen.DarkForest.MANSION, new StructureFeatureConfiguration(32, 9, 420));


            ((StructureSettingsAccessor)worldStructureSettings).setStructureConfig(tempMap);
        }
    }

    public static StructureSettings deepCopyDimensionStructuresSettings(StructureSettings settings) {
        // Grab old copy of stronghold spacing settings
        StrongholdConfiguration oldStrongholdSettings = settings.stronghold();

        // Make a deep copy and wrap it in an optional as StructureSettings requires an optional
        Optional<StrongholdConfiguration> newStrongholdSettings = oldStrongholdSettings == null ?
                Optional.empty() :
                Optional.of(new StrongholdConfiguration(
                        oldStrongholdSettings.distance(),
                        oldStrongholdSettings.spread(),
                        oldStrongholdSettings.count()));

        // Create new deep copied StructureSettings
        // We do not need to create a new structure spacing map instance here as our patch into
        // StructureSettings will already create the new map instance for us.
        StructureSettings newStructureSettings = new StructureSettings(newStrongholdSettings, settings.structureConfig());
        ((StructureSettingsAccessor)newStructureSettings).setConfiguredStructures(ImmutableMap.copyOf(((StructureSettingsAccessor)settings).getConfiguredStructures()));
        return newStructureSettings;
    }
}
