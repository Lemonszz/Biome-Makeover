package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec3;

public class DepthsCurseEnchantment extends BMEnchantment {
    public DepthsCurseEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
    }

    @Override
    public void onTick(LivingEntity entity, ItemStack stack, int level)
    {
        if(entity.isInWaterOrBubble())
        {
            Vec3 vel = entity.getDeltaMovement();
            if(vel.y > -1F)
            {
                double max = 0.05 * level;
                double yV = Math.max(-max, vel.y - max);
                entity.setDeltaMovement(vel.x, yV, vel.z);
                entity.hasImpulse = true;
                entity.hurtMarked = true;
            }
        }
    }

    @Override
    public int getMinCost(int i) {
        return 25;
    }

    @Override
    public int getMaxCost(int i) {
        return 50;
    }

    public int getMaxLevel()
    {
        return 3;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public boolean isTradeable()
    {
        return false;
    }
}
