package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class InsomniaCurseEnchantment extends BMEnchantment
{
    public InsomniaCurseEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
    }

    @Override
    public void onTick(LivingEntity entity, ItemStack stack, int level)
    {
        if(entity instanceof Player player)
        {
            if(!player.isSleeping())
            {
                player.awardStat(Stats.TIME_SINCE_REST, level);
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

    @Override
    public int getMaxLevel()
    {
        return 5;
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
