package party.lemons.biomemakeover.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMItems;

@Mixin(targets = "net/minecraft/screen/BrewingStandScreenHandler$FuelSlot")
public class BrewingStandFuelSlotMixin
{
	@Inject(at = @At("HEAD"), method = "matches(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
	private static void matches(ItemStack stack, CallbackInfoReturnable<Boolean> cbi)
	{
		if(stack.getItem() == BMItems.SOUL_EMBERS) cbi.setReturnValue(true);
	}
}
