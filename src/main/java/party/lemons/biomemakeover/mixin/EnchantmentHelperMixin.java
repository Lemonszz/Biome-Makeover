package party.lemons.biomemakeover.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.util.EntityUtil;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin
{
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;forEachEnchantment(Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;Ljava/lang/Iterable;)V"), method = "getProtectionAmount", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private static void applyProjectileResistance(Iterable<ItemStack> equipment, DamageSource source, CallbackInfoReturnable<Integer> cbi, MutableInt resistance)
	{
		if(source.isProjectile())
		{
			EntityUtil.applyProjectileResistance(equipment, resistance);
		}
	}
}
