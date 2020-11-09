package party.lemons.biomemakeover.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.init.BMBlocks;

@Mixin(LandPathNodeMaker.class)
public class LandPathNodeMakerMixin
{
    @Inject(method = "getCommonNodeType", at = @At(target = "net/minecraft/block/BlockState.getBlock()Lnet/minecraft/block/Block;", value = "INVOKE_ASSIGN"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private static void getCommonNodeType(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<PathNodeType> cbi, BlockState blockState, Block block)
    {
        if(blockState.isOf(BMBlocks.BARREL_CACTUS) || blockState.isOf(BMBlocks.SAGUARO_CACTUS))
            cbi.setReturnValue(PathNodeType.DAMAGE_CACTUS);
    }
}
