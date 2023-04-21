package party.lemons.biomemakeover.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.level.feature.SunkenRuinFeature;
import party.lemons.biomemakeover.level.feature.mansion.MansionFeature;
import party.lemons.biomemakeover.level.generate.GhostTownLootProcessor;
import party.lemons.biomemakeover.level.generate.ReplaceSelectionProcessor;

public class BMStructures
{
	private static final DeferredRegister<StructureProcessorType<?>> PROCESSOR_TYPES = DeferredRegister.create(Constants.MOD_ID, Registries.STRUCTURE_PROCESSOR);
	private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Constants.MOD_ID, Registries.STRUCTURE_TYPE);
	private static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Constants.MOD_ID, Registries.STRUCTURE_PIECE);

	public static RegistrySupplier<StructureType<MansionFeature>> MANSION = STRUCTURE_TYPES.register(BiomeMakeover.ID("mansion"), ()->()->MansionFeature.CODEC);
	public static final RegistrySupplier<StructurePieceType> MANSION_PIECE = STRUCTURE_PIECES.register(BiomeMakeover.ID("mansion"), ()->MansionFeature.Piece::new);

	public static RegistrySupplier<StructureType<SunkenRuinFeature>> SUNKEN_RUIN = STRUCTURE_TYPES.register(BiomeMakeover.ID("sunken_ruin"), ()->()->SunkenRuinFeature.CODEC);
	public static final RegistrySupplier<StructurePieceType> SUNKEN_RUIN_PIECE = STRUCTURE_PIECES.register(BiomeMakeover.ID("sunken_ruin"), ()->SunkenRuinFeature.SunkenRuinPiece::new);
	public static final RegistrySupplier<StructureProcessorType<GhostTownLootProcessor>> GHOST_TOWN_LOOT_PROCESSOR = PROCESSOR_TYPES.register(BiomeMakeover.ID("ghost_town_loot"), ()->()->GhostTownLootProcessor.CODEC);

	public static final RegistrySupplier<StructureProcessorType<ReplaceSelectionProcessor>> REPLACE_SELECTION = PROCESSOR_TYPES.register(BiomeMakeover.ID("replace_selection"), ()->()-> ReplaceSelectionProcessor.CODEC);

	public static void init()
	{
		PROCESSOR_TYPES.register();
		STRUCTURE_TYPES.register();
		STRUCTURE_PIECES.register();
	}
}
