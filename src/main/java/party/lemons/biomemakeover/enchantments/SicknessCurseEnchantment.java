package party.lemons.biomemakeover.enchantments;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

import java.util.UUID;

public class SicknessCurseEnchantment extends BMEnchantment
{
	public SicknessCurseEnchantment()
	{
		super(Rarity.UNCOMMON, EnchantmentTarget.VANISHABLE, EquipmentSlot.values());
	}

	@Override
	public void initAttributes()
	{
		addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, UUID.randomUUID().toString(), -2, EntityAttributeModifier.Operation.ADDITION);
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
