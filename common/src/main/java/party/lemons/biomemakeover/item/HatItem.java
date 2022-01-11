package party.lemons.biomemakeover.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class HatItem extends ArmorItem
{
    public static final HatArmorMaterial MATERIAL = new HatArmorMaterial();
    private final ResourceLocation hatTexture;

    public HatItem(ResourceLocation hatTexture, Properties properties)
    {
        super(MATERIAL, EquipmentSlot.HEAD, properties);
        this.hatTexture = hatTexture;
    }

    public ResourceLocation getHatTexture()
    {
        return hatTexture;
    }

    public static class HatArmorMaterial implements ArmorMaterial
    {
        @Override
        public int getDurabilityForSlot(EquipmentSlot equipmentSlot) {
            return 300;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot equipmentSlot) {
            return 2;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public SoundEvent getEquipSound()
        {
            return SoundEvents.ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairIngredient()
        {
            return Ingredient.of(Items.LEATHER);
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