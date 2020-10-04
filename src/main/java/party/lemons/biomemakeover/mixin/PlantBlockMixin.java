package party.lemons.biomemakeover.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlantBlock.class)
public class PlantBlockMixin
{
	@Inject(at = @At("RETURN"), method = "canPlantOnTop", cancellable = true)
	public void canPlantOnTop(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cbi)
	{
		if(floor.isOf(Blocks.MYCELIUM))
			cbi.setReturnValue(true);
	}
}
