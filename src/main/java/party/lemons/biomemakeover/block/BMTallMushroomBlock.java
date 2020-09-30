package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import party.lemons.biomemakeover.util.BlockWithItem;

public class BMTallMushroomBlock extends BMTallFlowerBlock
{
	public BMTallMushroomBlock(Settings settings)
	{
		super(settings);
	}

	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isOpaqueFullCube(world, pos);
	}
}
