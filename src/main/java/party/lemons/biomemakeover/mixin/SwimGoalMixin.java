package party.lemons.biomemakeover.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMEnchantments;

@Mixin(SwimGoal.class)
public class SwimGoalMixin
{
	@Shadow @Final private MobEntity mob;

	@Inject(at = @At("HEAD"), method = "canStart", cancellable = true)
	public void canStart(CallbackInfoReturnable<Boolean> cbi)
	{
		if(EnchantmentHelper.getEquipmentLevel(BMEnchantments.DEPTH_CURSE, mob) > 0)
			cbi.setReturnValue(false);
	}
}
