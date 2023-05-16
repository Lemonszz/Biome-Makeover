package party.lemons.biomemakeover.level.generate;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMStructures;
import party.lemons.biomemakeover.util.RandomUtil;

public class GhostTownLootProcessor extends StructureProcessor
{
	public static final GhostTownLootProcessor INSTANCE = new GhostTownLootProcessor();
	public static final Codec<GhostTownLootProcessor> CODEC = Codec.unit(()->INSTANCE);

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo info, StructureTemplate.StructureBlockInfo info2, StructurePlaceSettings data)
	{
		BlockState blockState = info2.state();
		if(blockState.getBlock() == Blocks.BARREL)
		{
			if(info2.nbt() == null)
				info2 = new StructureTemplate.StructureBlockInfo(blockPos, info2.state(), new CompoundTag());

			if(!info2.nbt().contains("LootTable"))
				info2.nbt().putString("LootTable", BiomeMakeover.ID("ghost_town/loot_" + RandomUtil.RANDOM.nextInt(3)).toString());

			/*
			BlockEntity be = worldView.getBlockEntity(pos);
			if(be instanceof BarrelBlockEntity)
			{
				BarrelBlockEntity barrel = (BarrelBlockEntity) be;
				barrel.setLootTable(, RandomUtil.RANDOM.nextLong());
			}*/
		}

		return info2;
	}

	@Override
	protected StructureProcessorType<?> getType()
	{
		return BMStructures.GHOST_TOWN_LOOT_PROCESSOR.get();
	}
}