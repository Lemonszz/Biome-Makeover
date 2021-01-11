package party.lemons.biomemakeover.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class BMEnchantment extends Enchantment
{
	public BMEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes)
	{
		super(weight, type, slotTypes);
	}

	public void onTick(LivingEntity entity, ItemStack stack, int level)
	{
		
	}
}
