package party.lemons.biomemakeover.enchantments;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class InaccuracyCurseEnchantment extends BMEnchantment
{
	public InaccuracyCurseEnchantment()
	{
		super(Rarity.UNCOMMON, EnchantmentTarget.BOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
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
