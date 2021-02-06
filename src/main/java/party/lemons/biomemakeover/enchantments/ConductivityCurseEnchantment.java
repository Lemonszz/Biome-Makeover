package party.lemons.biomemakeover.enchantments;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ConductivityCurseEnchantment extends BMEnchantment
{
	public ConductivityCurseEnchantment()
	{
		super(Rarity.UNCOMMON, EnchantmentTarget.ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
	}

	@Override
	public void onTick(LivingEntity entity, ItemStack stack, int level)
	{
		ServerWorld world = (ServerWorld) entity.world;
		Random random = world.random;

		if(random.nextInt(11000 - (level * 1000)) == 0 && world.isThundering())
		{
			BlockPos pos = entity.getBlockPos();
			if(world.hasRain(pos))
			{
				LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
				lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
				world.spawnEntity(lightningEntity);
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
		return 5;
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
