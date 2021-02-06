package party.lemons.biomemakeover.util.color;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

public final class StaticBlockColorProvider implements BlockColorProvider
{
	private final int color;

	public StaticBlockColorProvider(int color)
	{
		this.color = color;
	}

	@Override
	public int getColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex)
	{
		return color;
	}
}
