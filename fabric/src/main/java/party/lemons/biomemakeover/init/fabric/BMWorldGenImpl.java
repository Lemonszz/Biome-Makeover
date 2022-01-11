package party.lemons.biomemakeover.init.fabric;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import party.lemons.biomemakeover.BiomeMakeover;

public class BMWorldGenImpl {
    public static void registerStructure(String name, StructureFeature<?> feature, StructureFeatureConfiguration cfg, boolean terraform)
    {
        FabricStructureBuilder<?, ?> STRUCTURE = FabricStructureBuilder.create(BiomeMakeover.ID(name), feature).step(GenerationStep.Decoration.SURFACE_STRUCTURES).defaultConfig(cfg);

        if(terraform)
            STRUCTURE = STRUCTURE.adjustsSurface();

        STRUCTURE.register();
    }
}
