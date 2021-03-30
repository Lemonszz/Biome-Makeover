package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.util.extensions.LootBlocker;

@Mixin(HostileEntity.class)
public class HostileEntityMixin
{
	@Inject(at = @At("HEAD"), method = "shouldDropLoot", cancellable = true)
	private void shouldDropLoot(CallbackInfoReturnable<Boolean> cbi)
	{
		if(((LootBlocker) this).isLootBlocked())
			cbi.setReturnValue(false);
	}
}
