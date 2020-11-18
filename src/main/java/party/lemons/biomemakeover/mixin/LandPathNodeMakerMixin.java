package party.lemons.biomemakeover.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMBlocks;

@Mixin(value = LandPathNodeMaker.class, priority = 2000)
public class LandPathNodeMakerMixin
{
	@Inject(at = @At("HEAD"), method = "getCommonNodeType", cancellable = true)
	private static void getCommonNodeType(BlockView world, BlockPos blockPos, CallbackInfoReturnable<PathNodeType> cbi)
	{
		BlockState st = world.getBlockState(blockPos);
		if(st.isOf(BMBlocks.BARREL_CACTUS) || st.isOf(BMBlocks.BARREL_CACTUS_FLOWERED) || st.isOf(BMBlocks.SAGUARO_CACTUS))
			cbi.setReturnValue(PathNodeType.DAMAGE_CACTUS);
	}
}
