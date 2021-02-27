package party.lemons.biomemakeover.enchantments;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class HeavyCurseEnchantment extends BMEnchantment
{
	public HeavyCurseEnchantment()
	{
		super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	@Override
	public void initAttributes()
	{
		addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, UUID.randomUUID().toString(), -0.25, EntityAttributeModifier.Operation.ADDITION);
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

	public boolean isAcceptableItem(ItemStack stack) {
		return stack.getItem() instanceof AxeItem ? true : super.isAcceptableItem(stack);
	}
}
