package party.lemons.biomemakeover.mixin.soulembers;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMItems;

@Mixin(targets = "net.minecraft.world.inventory/BrewingStandMenu$FuelSlot")
public class BrewingStandFuelSlotMixin {

    @Inject(at = @At("HEAD"), method = "mayPlaceItem", cancellable = true)
    private static void mayPlaceItem(ItemStack itemStack, CallbackInfoReturnable<Boolean> cbi) {
        if(itemStack.getItem() == BMItems.SOUL_EMBERS)
            cbi.setReturnValue(true);
    }
}
