package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.UUID;

public class EnfeeblementCurseEnchantment extends BMEnchantment
{
    public EnfeeblementCurseEnchantment()
    {
        super(Rarity.UNCOMMON, EnchantmentCategory.VANISHABLE, EquipmentSlot.values());
    }

    @Override
    public void initAttributes()
    {
        addAttributeModifier(Attributes.MAX_HEALTH, UUID.randomUUID().toString(), -2, AttributeModifier.Operation.ADDITION);
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