package party.lemons.biomemakeover.mixin;

import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.util.FlammabilityRegistry;

@Mixin(FireBlock.class)
public class FireBlockMixin
{
	@Inject(at = @At("HEAD"), method = "getBurnOdds", cancellable = true)
	private void bm_getBurnOdds(BlockState blockState, CallbackInfoReturnable<Integer> cbi) {

		FlammabilityRegistry.Entry entry;
		if((entry = FlammabilityRegistry.getEntry(blockState.getBlock())) != null)
		{
			cbi.setReturnValue(blockState.hasProperty(BlockStateProperties.WATERLOGGED) && blockState.getValue(BlockStateProperties.WATERLOGGED) ? 0 : entry.burnOdds());
		}
	}

	@Inject(at = @At("HEAD"), method = "getIgniteOdds", cancellable = true)
	private void bm_getIgniteOdds(BlockState blockState, CallbackInfoReturnable<Integer> cbi)
	{
		FlammabilityRegistry.Entry entry;
		if((entry = FlammabilityRegistry.getEntry(blockState.getBlock())) != null)
		{
			cbi.setReturnValue(blockState.hasProperty(BlockStateProperties.WATERLOGGED) && blockState.getValue(BlockStateProperties.WATERLOGGED) ? 0 : entry.catchOdds());
		}
	}
}
