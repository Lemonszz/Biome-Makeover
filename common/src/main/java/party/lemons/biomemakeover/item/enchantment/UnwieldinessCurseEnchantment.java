package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import party.lemons.biomemakeover.BMConfig;

import java.util.UUID;
import java.util.function.Supplier;

public class UnwieldinessCurseEnchantment extends TickableAttributeEnchantment {
    public UnwieldinessCurseEnchantment(Supplier<BMConfig.EnchantConfig> config) {
        super(config, true, Rarity.UNCOMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    public void initAttributes() {
        addAttributeModifier(Attributes.ATTACK_SPEED, UUID.randomUUID().toString(), -0.25, AttributeModifier.Operation.ADDITION);
    }
    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || super.canEnchant(stack);
    }
}