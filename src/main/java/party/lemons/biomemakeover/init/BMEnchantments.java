package party.lemons.biomemakeover.init;

import net.minecraft.enchantment.BindingCurseEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.enchantments.ConductivityCurseEnchantment;
import party.lemons.biomemakeover.enchantments.DecayCurseEnchantment;
import party.lemons.biomemakeover.enchantments.InsomniaCurseEnchantment;
import party.lemons.biomemakeover.enchantments.SicknessCurseEnchantment;
import party.lemons.biomemakeover.util.RegistryHelper;

public class BMEnchantments
{
	public static final Enchantment DECAY_CURSE = new DecayCurseEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.values());
	public static final Enchantment INSOMNIA_CURSE = new InsomniaCurseEnchantment();
	public static final Enchantment CONDUCTIVITY_CURSE = new ConductivityCurseEnchantment();
	public static final Enchantment SICKNESS_CURSE = new SicknessCurseEnchantment();

	public static void init()
	{
		RegistryHelper.register(Registry.ENCHANTMENT, Enchantment.class, BMEnchantments.class);
	}
}
