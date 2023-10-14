package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import party.lemons.biomemakeover.BMConfig;

import java.util.UUID;
import java.util.function.Supplier;

public class EnfeeblementCurseEnchantment extends TickableAttributeEnchantment
{
    public EnfeeblementCurseEnchantment(Supplier<BMConfig.EnchantConfig> config)
    {
        super(config, true, Rarity.UNCOMMON, EnchantmentCategory.VANISHABLE, EquipmentSlot.values());
    }

    @Override
    public void initAttributes()
    {
        addAttributeModifier(Attributes.MAX_HEALTH, UUID.randomUUID().toString(), -2, AttributeModifier.Operation.ADDITION);
    }
}