package party.lemons.biomemakeover.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;

import java.util.UUID;

public class BMArmorItem extends ArmorItem
{
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributes;
	private final int protection;
	private final float toughness;

	public BMArmorItem(ArmorMaterial material, Multimap<EntityAttribute, EntityAttributeModifier> attributes, int protection, float toughness, EquipmentSlot slot, Settings settings)
	{
		super(material, slot, settings);

		this.attributes = attributes;
		this.protection = protection;
		this.toughness = toughness;
	}

	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == this.slot ? attributes : ImmutableMultimap.of();
	}

	public int getProtection() {
		return this.protection;
	}

	//getToughness();
	public float method_26353() {
		return this.toughness;
	}

	public static class Builder
	{
		private static final UUID DUMMY_UUID = UUID.randomUUID();
		private static final UUID[] MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

		private final LinkedListMultimap<EntityAttribute, EntityAttributeModifier> attributes = LinkedListMultimap.create();
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

		public Builder attribute(String name, EntityAttribute attribute, double value, EntityAttributeModifier.Operation operation)
		{
			attributes.put(attribute, new EntityAttributeModifier(DUMMY_UUID, name, value, operation));
			return this;
		}

		public BMArmorItem build(EquipmentSlot slot, Item.Settings settings)
		{
			if(!overrideProtection)
				protection = material.getProtectionAmount(slot);

			attributes.removeAll(EntityAttributes.GENERIC_ARMOR);
			attributes.removeAll(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
			attributes.removeAll(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);

			attributes.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(DUMMY_UUID, "Armor modifier", this.protection, EntityAttributeModifier.Operation.ADDITION));
			attributes.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(DUMMY_UUID, "Armor toughness", this.toughness, EntityAttributeModifier.Operation.ADDITION));
			if (this.knockbackResistance != 0.0F)
			{
				attributes.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(DUMMY_UUID, "Armor knockback resistance", (double)this.knockbackResistance, EntityAttributeModifier.Operation.ADDITION));
			}

			return new BMArmorItem(material, buildAttributes(slot), protection, toughness, slot, settings);
		}

		private LinkedListMultimap<EntityAttribute, EntityAttributeModifier> buildAttributes(EquipmentSlot slot)
		{
			LinkedListMultimap<EntityAttribute, EntityAttributeModifier> atts = LinkedListMultimap.create();
			for(EntityAttribute attribute : attributes.keys())
			{
				for(EntityAttributeModifier modifier : attributes.get(attribute))
				{
					atts.put(attribute, new EntityAttributeModifier(MODIFIERS[slot.getEntitySlotId()], modifier.getName(), modifier.getValue(), modifier.getOperation()));
				}
			}
			return atts;
		}

		private Builder(ArmorMaterial material)
		{
			this.material = material;
			this.toughness = material.getToughness();
			this.knockbackResistance = material.getKnockbackResistance();
		}
	}
}
