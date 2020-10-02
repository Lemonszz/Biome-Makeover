package party.lemons.biomemakeover.mixin;

import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMBlocks;

@Mixin(Feature.class)
public class FeatureMixin
{
	@Inject(at = @At("RETURN"), method = "isSoil(Lnet/minecraft/block/Block;)Z", cancellable = true)
	private static void isDirt(Block block, CallbackInfoReturnable<Boolean> cbi)
	{
		if(block == BMBlocks.DEEP_MYCELIUM)
			cbi.setReturnValue(true);
	}
}
