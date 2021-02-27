package party.lemons.biomemakeover.enchantments;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class DepthsCurseEnchantment extends BMEnchantment
{
	public DepthsCurseEnchantment()
	{
		super(Rarity.UNCOMMON, EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
	}

	@Override
	public void onTick(LivingEntity entity, ItemStack stack, int level)
	{
		if(entity.isTouchingWater())
		{
			Vec3d vel = entity.getVelocity();
			if(vel.y > -1F)
			{
				double max = 0.05 * level;
				double yV = Math.max(-max, vel.y - max);
				entity.setVelocity(vel.x, yV, vel.z);
				entity.velocityModified = true;
				entity.velocityDirty = true;
			}
		}
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
