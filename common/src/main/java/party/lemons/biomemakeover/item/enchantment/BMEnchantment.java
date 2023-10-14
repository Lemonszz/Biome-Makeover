package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import party.lemons.biomemakeover.BMConfig;

import java.util.function.Supplier;

public class BMEnchantment extends Enchantment
{
    private final Supplier<BMConfig.EnchantConfig> config;
    private final boolean isCurse;

    public BMEnchantment(Supplier<BMConfig.EnchantConfig> config, boolean isCurse, Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot[] equipmentSlots) {
        super(rarity, enchantmentCategory, equipmentSlots);

        this.config = config;
        this.isCurse = isCurse;
    }

    @Override
    public boolean isCurse() {
        return isCurse;
    }

    @Override
    public int getMaxLevel() {
        return config.get().maxLevel;
    }

    @Override
    public int getMinCost(int i) {
        return config.get().minCost;
    }

    @Override
    public int getMaxCost(int i) {
        return config.get().maxCost;
    }

    @Override
    public boolean isTreasureOnly() {
        return config.get().isTreasureOnly;
    }

    @Override
    public boolean isDiscoverable() {
        return config.get().isDiscoverable;
    }

    @Override
    public boolean isTradeable() {
        return config.get().isTradeable;
    }
}
