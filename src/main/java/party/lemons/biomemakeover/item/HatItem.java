package party.lemons.biomemakeover.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class HatItem extends ArmorItem
{
	public static final HatArmorMaterial MATERIAL = new HatArmorMaterial();
	private final Identifier hatTexture;

	public HatItem(Identifier hatTexture, Settings settings)
	{
		super(MATERIAL, EquipmentSlot.HEAD, settings);
		this.hatTexture = hatTexture;
	}

	public Identifier getHatTexture()
	{
		return hatTexture;
	}

	public static class HatArmorMaterial implements ArmorMaterial
	{
		@Override
		public int getDurability(EquipmentSlot slot)
		{
			return 300;
		}

		@Override
		public int getProtectionAmount(EquipmentSlot slot)
		{
			return 2;
		}

		@Override
		public int getEnchantability()
		{
			return 0;
		}

		@Override
		public SoundEvent getEquipSound()
		{
			return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
		}

		@Override
		public Ingredient getRepairIngredient()
		{
			return Ingredient.ofItems(Items.LEATHER);
		}

		@Override
		public String getName()
		{
			return "hat";
		}

		@Override
		public float getToughness()
		{
			return 0;
		}

		@Override
		public float getKnockbackResistance()
		{
			return 0;
		}
	}
}
