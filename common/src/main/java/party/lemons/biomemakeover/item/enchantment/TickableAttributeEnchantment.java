package party.lemons.biomemakeover.item.enchantment;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import party.lemons.biomemakeover.BMConfig;
import party.lemons.taniwha.util.MathUtils;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class TickableAttributeEnchantment extends BMEnchantment
{
    private final Map<Attribute, AttributeModifier> attributeModifiers = Maps.newHashMap();

    public TickableAttributeEnchantment(Supplier<BMConfig.EnchantConfig> config, boolean isCurse, Rarity weight, EnchantmentCategory type, EquipmentSlot[] slotTypes)
    {
        super(config, isCurse, weight, type, slotTypes);

        initAttributes();
    }

    public void initAttributes()
    {

    }

    public void onTick(LivingEntity entity, ItemStack stack, int level)
    {

    }

    protected void addAttributeModifier(Attribute attribute, String uuid, double amount, AttributeModifier.Operation operation)
    {
        AttributeModifier entityAttributeModifier = new AttributeModifier(UUID.fromString(uuid), this::getDescriptionId, amount, operation);
        this.attributeModifiers.put(attribute, entityAttributeModifier);
    }

    public boolean addAttributes(LivingEntity entity, ItemStack stack, EquipmentSlot slot, int level)
    {
        if(attributeModifiers.size() <= 0 || stack.isEmpty()) return false;

        for(Map.Entry<Attribute, AttributeModifier> attributeEntry : this.attributeModifiers.entrySet())
        {
            UUID id = MathUtils.uuidFromString(slot.toString());
            AttributeInstance entityAttributeInstance = entity.getAttributes().getInstance(attributeEntry.getKey());
            if(entityAttributeInstance != null)
            {
                AttributeModifier mod = attributeEntry.getValue();
                entityAttributeInstance.removeModifier(mod);
                entityAttributeInstance.addTransientModifier(new AttributeModifier(id, this.getDescriptionId() + " " + level, this.adjustModifierAmount(level, mod), mod.getOperation()));

            }
        }
        return true;
    }

    public double adjustModifierAmount(int amplifier, AttributeModifier modifier)
    {
        return modifier.getAmount() * (double) (amplifier);
    }

    public void removeAttributes(LivingEntity entity, EquipmentSlot slot)
    {
        for(Map.Entry<Attribute, AttributeModifier> attributeEntry : this.attributeModifiers.entrySet())
        {
            UUID slotID = MathUtils.uuidFromString(slot.toString());
            AttributeInstance entityAttributeInstance = entity.getAttributes().getInstance(attributeEntry.getKey());
            if(entityAttributeInstance != null)
            {
                AttributeModifier mod = entityAttributeInstance.getModifier(slotID);
                if(mod != null)
                    entityAttributeInstance.removeModifier(mod);
                else
                    System.out.println("ERROR REMOVING MODIFIER: DOESNT EXIST??? : " + entityAttributeInstance.getAttribute().getDescriptionId());
            }
        }
    }
}