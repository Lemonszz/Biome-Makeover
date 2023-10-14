package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import party.lemons.biomemakeover.BMConfig;

import java.util.function.Supplier;

public class DecayCurseEnchantment extends BMEnchantment {
    public DecayCurseEnchantment(Supplier<BMConfig.EnchantConfig> config) {
        super(config, true, Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.isDamageableItem() || super.canEnchant(stack);
    }
}