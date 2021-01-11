package party.lemons.biomemakeover.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;

public class InsomniaCurseEnchantment extends BMEnchantment
{
	public InsomniaCurseEnchantment()
	{
		super(Rarity.UNCOMMON, EnchantmentTarget.ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
	}

	@Override
	public void onTick(LivingEntity entity, ItemStack stack, int level)
	{
		if(entity instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) entity;

			if (!player.isSleeping())
			{
				player.increaseStat(Stats.TIME_SINCE_REST, level);
			}
		}
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
