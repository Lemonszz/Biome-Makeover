package party.lemons.biomemakeover.enchantments;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class SuffocationCurseEnchantment extends BMEnchantment
{
	public SuffocationCurseEnchantment()
	{
		super(Rarity.UNCOMMON, EnchantmentTarget.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD});
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
