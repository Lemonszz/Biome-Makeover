package party.lemons.biomemakeover.util.color;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

public class FoliageBlockColorProvider implements BlockColorProvider
{
	@Override
	public int getColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex)
	{
		if(tintIndex == 0)
			return world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor();

		return 0xFFFFFF;
	}
}
