package party.lemons.biomemakeover.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import party.lemons.biomemakeover.util.registry.RegistryHelper;

import java.util.UUID;
import java.util.function.Supplier;

public class BMArmorItem extends ArmorItem
{
    final LinkedListMultimap<ResourceLocation, AttributeHolder> suppliedAttributes;
    private Multimap<Attribute, AttributeModifier> attributes;
    private final int protection;
    private final float toughness;

    public BMArmorItem(ArmorMaterial material,LinkedListMultimap<ResourceLocation, AttributeHolder> attributes, int protection, float toughness, EquipmentSlot slot, Properties properties)
    {
        super(material, slot, properties);

        this.suppliedAttributes = attributes;
        this.protection = protection;
        this.toughness = toughness;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if(attributes == null)
            initAttributes();

        return equipmentSlot == this.slot ? attributes : ImmutableMultimap.of();
    }

    private void initAttributes()
    {
        attributes = LinkedListMultimap.create();
        suppliedAttributes.forEach((l, holder)->{
            attributes.put(holder.supplier.get(), holder.modifier);
        });
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public int getDefense() {
        return this.protection;
    }

    public static class Builder
    {
        private static final UUID DUMMY_UUID = UUID.randomUUID();
        private static final UUID[] MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

        private final LinkedListMultimap<ResourceLocation, AttributeHolder> suppliedAttributes = LinkedListMultimap.create();
        private final ArmorMaterial material;
        private int protection;
        private boolean overrideProtection = false;
        private float toughness;
        private float knockbackResistance;

        public static Builder create(ArmorMaterial material)
        {
            return new Builder(material);
        }

        public Builder protection(int amount)
        {
            this.protection = amount;
            this.overrideProtection = true;
            return this;
        }

        public Builder toughness(float amount)
        {
            this.toughness = amount;
            return this;
        }

        public Builder knockbackResistance(float amount)
        {
            this.knockbackResistance = amount;
            return this;
        }

        public Builder attribute(String name, RegistrySupplier<Attribute> attribute, double value, AttributeModifier.Operation operation)
        {
            suppliedAttributes.put(attribute.getId(), new AttributeHolder(attribute, new AttributeModifier(DUMMY_UUID, name, value, operation)));
            return this;
        }

        public Builder attribute(String name, Attribute attribute, double value, AttributeModifier.Operation operation)
        {
            suppliedAttributes.put(Registry.ATTRIBUTE.getKey(attribute),new AttributeHolder(()->attribute, new AttributeModifier(DUMMY_UUID, name, value, operation)));
            return this;
        }

        public Supplier<Item> build(EquipmentSlot slot, Properties properties)
        {
            if(!overrideProtection)
                protection = material.getDefenseForSlot(slot);

            suppliedAttributes.removeAll(Registry.ATTRIBUTE.getKey(Attributes.ARMOR));
            suppliedAttributes.removeAll(Registry.ATTRIBUTE.getKey(Attributes.ARMOR_TOUGHNESS));
            suppliedAttributes.removeAll(Registry.ATTRIBUTE.getKey(Attributes.KNOCKBACK_RESISTANCE));

            suppliedAttributes.put(Registry.ATTRIBUTE.getKey(Attributes.ARMOR), new AttributeHolder(()->Attributes.ARMOR, new AttributeModifier(DUMMY_UUID, "Armor modifier", this.protection, AttributeModifier.Operation.ADDITION)));
            suppliedAttributes.put(Registry.ATTRIBUTE.getKey(Attributes.ARMOR_TOUGHNESS), new AttributeHolder(()->Attributes.ARMOR_TOUGHNESS, new AttributeModifier(DUMMY_UUID, "Armor toughness", this.toughness, AttributeModifier.Operation.ADDITION)));
            if (this.knockbackResistance != 0.0F)
            {
                suppliedAttributes.put(Registry.ATTRIBUTE.getKey(Attributes.KNOCKBACK_RESISTANCE), new AttributeHolder(()->Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(DUMMY_UUID, "Armor knockback resistance", this.knockbackResistance, AttributeModifier.Operation.ADDITION)));
            }

            final LinkedListMultimap<ResourceLocation, AttributeHolder> builtAttributes = buildAttributes(slot);
            return ()->new BMArmorItem(material, builtAttributes, protection, toughness, slot, properties);
        }

        private LinkedListMultimap<ResourceLocation, AttributeHolder> buildAttributes(EquipmentSlot slot)
        {
            LinkedListMultimap<ResourceLocation, AttributeHolder> atts = LinkedListMultimap.create();

            suppliedAttributes.forEach(((location, attributeHolder) -> {
                AttributeModifier modifier = attributeHolder.modifier();
                atts.put(location, new AttributeHolder(attributeHolder.supplier(), new AttributeModifier(MODIFIERS[slot.getIndex()], modifier.getName(), modifier.getAmount(), modifier.getOperation())));
            }));
            return atts;
        }

        private Builder(ArmorMaterial material)
        {
            this.material = material;
            this.toughness = material.getToughness();
            this.knockbackResistance = material.getKnockbackResistance();
        }
    }

    private record AttributeHolder(Supplier<Attribute> supplier, AttributeModifier modifier)
    {

    }
}