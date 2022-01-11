package party.lemons.biomemakeover.init.forge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.util.access.StructureSettingsAccess;
import party.lemons.biomemakeover.util.registry.RegistryHelper;

public class BMWorldGenImpl {
    public static void registerStructure(String name, StructureFeature<?> feature, StructureFeatureConfiguration cfg, boolean terraform)
    {
        RegistryHelper.registerObject(Registry.STRUCTURE_FEATURE, BiomeMakeover.ID(name), feature);

        ResourceLocation location = BiomeMakeover.ID(name);
        StructureFeature.STRUCTURES_REGISTRY.put(location.toString(), feature);
        ((StructureSettingsAccess)new StructureSettings(false)).addStructureSettings(feature, cfg);

        if(terraform)
            StructureFeature.NOISE_AFFECTING_FEATURES.add(feature);
    }
}
