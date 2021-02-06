package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.util.RegistryHelper;
import party.lemons.biomemakeover.world.feature.GhostTownFeature;
import party.lemons.biomemakeover.world.feature.SunkenRuinFeature;
import party.lemons.biomemakeover.world.feature.SunkenRuinFeature.SunkenRuinFeatureConfig;
import party.lemons.biomemakeover.world.feature.mansion.MansionFeature;

public class BMStructures
{
	public static final StructureFeature<StructurePoolFeatureConfig> GHOST_TOWN = new GhostTownFeature(StructurePoolFeatureConfig.CODEC);
	public static final ConfiguredStructureFeature<?, ?> GHOST_TOWN_CONFIGURED = GHOST_TOWN.configure(GhostTownFeature.CONFIG);

	public static final StructureFeature<SunkenRuinFeatureConfig> SUNKEN_RUIN = new SunkenRuinFeature(SunkenRuinFeatureConfig.CODEC);
	public static final ConfiguredStructureFeature<?, ?> SUNKEN_RUIN_CONFIGURED = SUNKEN_RUIN.configure(new SunkenRuinFeatureConfig(0.8F, 0.6F));

	public static final StructureFeature<DefaultFeatureConfig> MANSION = new MansionFeature(DefaultFeatureConfig.CODEC);
	public static final ConfiguredStructureFeature<?, ?> MANSION_CONFIGURED = MANSION.configure(DefaultFeatureConfig.INSTANCE);

	public static final StructurePieceType SUNKEN_RUIN_PIECE = SunkenRuinFeature.Piece::new;
	public static final StructurePieceType MANSION_PIECE = MansionFeature.Piece::new;

	public static void init()
	{
		RegistryHelper.register(Registry.STRUCTURE_PIECE, StructurePieceType.class, BMStructures.class);
		GhostTownFeature.init();

		FabricStructureBuilder.create(BiomeMakeover.ID("ghost_town"), GHOST_TOWN).step(GenerationStep.Feature.UNDERGROUND_DECORATION).defaultConfig(32, 12, 6969).adjustsSurface().register();

		FabricStructureBuilder.create(BiomeMakeover.ID("sunken_ruin"), SUNKEN_RUIN).step(GenerationStep.Feature.LOCAL_MODIFICATIONS).defaultConfig(24, 9, 420).register();

		FabricStructureBuilder.create(BiomeMakeover.ID("mansion"), MANSION).step(GenerationStep.Feature.SURFACE_STRUCTURES).defaultConfig(24, 9, 420).adjustsSurface().register();

		RegistryHelper.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ConfiguredStructureFeature.class, BMStructures.class);
	}
}
