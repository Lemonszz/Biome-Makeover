package party.lemons.biomemakeover.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.taniwha.block.DripstoneReceiver;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin implements DripstoneReceiver
{
	@Shadow @Final public static IntegerProperty LEVEL;

	@Override
	public boolean canReceiveDrip(Fluid fluid, Level level, BlockState blockState)
	{
		return fluid == Fluids.WATER && blockState.getValue(LEVEL) == 8;
	}

	@Override
	public void receiveStalactiteDrip(BlockState blockState, Level level, BlockPos blockPos, Fluid fluid)
	{
		level.levelEvent(LevelEvent.COMPOSTER_FILL, blockPos, 1);
		level.setBlock(blockPos, BMBlocks.PEAT_COMPOSTER.get().defaultBlockState(), Block.UPDATE_ALL);
	}
}
