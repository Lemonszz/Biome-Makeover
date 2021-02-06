package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.function.Supplier;

public class GlowshroomPlantBlock extends BMMushroomPlantBlock
{
	public GlowshroomPlantBlock(Supplier<ConfiguredFeature> giantShroomFeature, Settings settings)
	{
		super(giantShroomFeature, settings);
	}

	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		if(blockState.isIn(BlockTags.MUSHROOM_GROW_BLOCK))
		{
			return true;
		}else
		{
			return this.canPlantOnTop(blockState, world, blockPos);
		}
	}
}
