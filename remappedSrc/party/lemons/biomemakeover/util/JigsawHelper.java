package party.lemons.biomemakeover.util;

import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.registry.BuiltinRegistries;
import party.lemons.biomemakeover.BiomeMakeover;

public class JigsawHelper
{
	public static StructureProcessorList register(StructureProcessorList li, String name)
	{
		BuiltinRegistries.add(BuiltinRegistries.STRUCTURE_PROCESSOR_LIST,  BiomeMakeover.ID(name), li);

		return li;
	}
}
