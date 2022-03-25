package party.lemons.biomemakeover.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;

@Mixin(ComposterBlock.InputContainer.class)
public abstract class InputContainerMixin_EctoCompost extends SimpleContainer {
    @Shadow
    private boolean changed;

    @Shadow
    @Final
    private BlockState state;

    @Shadow
    @Final
    private LevelAccessor level;

    @Shadow
    @Final
    private BlockPos pos;

    @Inject(at = @At("RETURN"), method = "canPlaceItemThroughFace", cancellable = true)
    public void canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction, CallbackInfoReturnable<Boolean> cbi) {
        cbi.setReturnValue(ComposterBlock.COMPOSTABLES.containsKey(itemStack.getItem()) || ((!itemStack.isEmpty() && itemStack.getItem() == BMItems.ECTOPLASM.get()) && state.getBlock() == Blocks.COMPOSTER && state.getValue(ComposterBlock.LEVEL) > 0));
    }

    @Inject(at = @At("HEAD"), method = "setChanged")
    void setChanged(CallbackInfo cbi)
    {
        ItemStack stack = getItem(0);
        if(!stack.isEmpty() && stack.getItem() == BMItems.ECTOPLASM)
        {
            this.changed = true;
            level.levelEvent(1500, this.pos, 1);
            this.removeItemNoUpdate(0);

            level.setBlock(pos, BMBlocks.ECTOPLASM_COMPOSTER.get().defaultBlockState().setValue(ComposterBlock.LEVEL, state.getValue(ComposterBlock.LEVEL)), 3);
        }
    }
}