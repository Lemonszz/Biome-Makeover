package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RootsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import party.lemons.biomemakeover.util.BlockWithItem;

public class MushroomRootsBlock extends RootsBlock implements BlockWithItem
{
	public MushroomRootsBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos)
	{
		return floor.isOf(Blocks.MYCELIUM) || super.canPlantOnTop(floor, world, pos);
	}
}
