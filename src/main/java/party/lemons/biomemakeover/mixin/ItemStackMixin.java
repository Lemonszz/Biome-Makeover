package party.lemons.biomemakeover.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import party.lemons.biomemakeover.init.BMEnchantments;

@Mixin(ItemStack.class)
public class ItemStackMixin
{
	@ModifyVariable(at = @At("HEAD"), method = "damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z")
	private int modifyAmount(int amount)
	{
		return amount +  EnchantmentHelper.getLevel(BMEnchantments.DECAY_CURSE, (ItemStack)(Object)this);
	}


}
