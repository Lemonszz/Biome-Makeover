package party.lemons.biomemakeover.enchantments;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class FlammabilityCurseEnchantment extends BMEnchantment
{
	public FlammabilityCurseEnchantment()
	{
		super(Rarity.UNCOMMON, EnchantmentTarget.ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
	}

	public int getMinPower(int level)
	{
		return 25;
	}

	public int getMaxPower(int level)
	{
		return 50;
	}

	public int getMaxLevel()
	{
		return 3;
	}

	public boolean isTreasure()
	{
		return true;
	}

	public boolean isCursed()
	{
		return true;
	}
}
