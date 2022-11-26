package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class FlammabilityCurseEnchantment extends BMEnchantment {
    public FlammabilityCurseEnchantment()
    {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
    }

    @Override
    public int getMinCost(int i) {
        return 25;
    }

    @Override
    public int getMaxCost(int i) {
        return 25;
    }

    @Override
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
