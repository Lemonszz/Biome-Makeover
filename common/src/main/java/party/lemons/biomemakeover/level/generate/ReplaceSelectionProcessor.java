package party.lemons.biomemakeover.level.generate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMStructures;

public class ReplaceSelectionProcessor extends StructureProcessor
{
	public static final Codec<ReplaceSelectionProcessor> CODEC =  RecordCodecBuilder.create(instance ->
			instance.group(
							BlockStateProvider.CODEC.fieldOf("output").forGetter(c -> c.output),
							BuiltInRegistries.BLOCK.byNameCodec().fieldOf("target").forGetter(i->i.target)
					)
					.apply(instance, ReplaceSelectionProcessor::new));

	private final BlockStateProvider output;
	private final Block target;

	public ReplaceSelectionProcessor(BlockStateProvider output, Block target)
	{
		this.output = output;
		this.target = target;
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo input, StructurePlaceSettings structurePlaceSettings)
	{
		BlockState blockState = input.state();
		if(blockState.is(target))
		{
			BlockState newState = output.getState(structurePlaceSettings.getRandom(input.pos()), input.pos());
			return new StructureTemplate.StructureBlockInfo(input.pos(), newState, input.nbt());
		}

		return input;
	}

	@Override
	protected StructureProcessorType<?> getType()
	{
		return BMStructures.REPLACE_SELECTION.get();
	}
}
