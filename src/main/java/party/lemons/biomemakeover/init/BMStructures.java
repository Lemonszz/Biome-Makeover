package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.util.RegistryHelper;
import party.lemons.biomemakeover.world.feature.GhostTownFeature;

public class BMStructures
{
	public static final StructureFeature<StructurePoolFeatureConfig> GHOST_TOWN = new GhostTownFeature(StructurePoolFeatureConfig.CODEC);
	public static final ConfiguredStructureFeature<?, ?> GHOST_TOWN_CONFIGURED = GHOST_TOWN.configure(GhostTownFeature.CONFIG);

	public static void init()
	{
		RegistryHelper.register(Registry.STRUCTURE_PIECE, StructurePieceType.class, BMStructures.class);
		GhostTownFeature.init();

		FabricStructureBuilder.create(BiomeMakeover.ID("ghost_town"), GHOST_TOWN)
				.step(GenerationStep.Feature.UNDERGROUND_DECORATION)
				.defaultConfig(32, 12, 6969)
				.adjustsSurface()
				.register();

		RegistryHelper.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ConfiguredStructureFeature.class, BMStructures.class);
	}
}
