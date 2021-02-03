package party.lemons.biomemakeover.util.color;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.Biome;
import party.lemons.biomemakeover.util.MathUtils;
import party.lemons.biomemakeover.util.access.ChunkRenderRegionAccess;

public class FoliageShiftBlockColorProvider extends FoliageBlockColorProvider
{
	protected final int rShift, gShift, bShift;

	public FoliageShiftBlockColorProvider(int rShift, int gShift, int bShift)
	{
		this.rShift = rShift;
		this.gShift = gShift;
		this.bShift = bShift;
	}

	@Override
	public int getColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex)
	{
		int color = super.getColor(state, world, pos, tintIndex);
		int[] boosts = getColorBoosts(world, state, pos, tintIndex);

		return MathUtils.colourBoost(color, boosts[0], boosts[1], boosts[2]);
	}

	protected int[] getColorBoosts(BlockRenderView world, BlockState state, BlockPos pos, int tintIndex)
	{
		return new int[]{rShift, gShift, bShift};
	}

	public static class Lillies extends FoliageShiftBlockColorProvider
	{
		public Lillies()
		{
			super(10, -10, 10);
		}

		@Override
		protected int[] getColorBoosts(BlockRenderView world, BlockState state, BlockPos pos, int tintIndex)
		{
			if(world instanceof ChunkRenderRegionAccess)
			{
				if(((ChunkRenderRegionAccess)world).getWorld().getBiome(pos).getCategory() == Biome.Category.SWAMP)
				{
					return new int[]{-20, 40, -20};
				}
			}

			return super.getColorBoosts(world, state, pos, tintIndex);
		}
	}

	public static class Willow extends FoliageShiftBlockColorProvider
	{
		public Willow()
		{
			super(0,0,0);
		}

		@Override
		protected int[] getColorBoosts(BlockRenderView world, BlockState state, BlockPos pos, int tintIndex)
		{
			if(world instanceof ChunkRenderRegionAccess)
			{
				if(((ChunkRenderRegionAccess)world).getWorld().getBiome(pos).getCategory() == Biome.Category.SWAMP)
				{
					return new int[]{-10, 15, -10};
				}
			}

			return super.getColorBoosts(world, state, pos, tintIndex);
		}
	}
}
