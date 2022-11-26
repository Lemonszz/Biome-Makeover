package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class InaccuracyCurseEnchantment extends BMEnchantment {
    public InaccuracyCurseEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.BOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int i) {
        return 25;
    }

    @Override
    public int getMaxCost(int i) {
        return 50;
    }

    public int getMaxLevel() {
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