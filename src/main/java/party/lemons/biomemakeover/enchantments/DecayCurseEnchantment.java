package party.lemons.biomemakeover.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class DecayCurseEnchantment extends Enchantment
{
	public DecayCurseEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
		super(rarity, EnchantmentTarget.BREAKABLE, slots);
	}

	public boolean isAcceptableItem(ItemStack stack) {
		return stack.isDamageable() ? true : super.isAcceptableItem(stack);
	}

	public int getMinPower(int level) {
		return 25;
	}

	public int getMaxPower(int level) {
		return 50;
	}

	public int getMaxLevel() {
		return 5;
	}

	public boolean isTreasure() {
		return true;
	}

	public boolean isCursed() {
		return true;
	}
}
