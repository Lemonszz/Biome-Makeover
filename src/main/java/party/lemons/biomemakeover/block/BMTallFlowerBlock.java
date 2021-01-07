package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import party.lemons.biomemakeover.util.BlockWithItem;

import static net.minecraft.state.property.Properties.WATERLOGGED;

public class BMTallFlowerBlock extends TallFlowerBlock implements BlockWithItem
{
	public BMTallFlowerBlock(Settings settings)
	{
		super(settings);
	}
}
