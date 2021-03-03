package party.lemons.biomemakeover.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.enchantments.*;
import party.lemons.biomemakeover.util.RegistryHelper;

public class BMEnchantments
{
	public static final Enchantment DECAY_CURSE = new DecayCurseEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.values());
	public static final Enchantment INSOMNIA_CURSE = new InsomniaCurseEnchantment();
	public static final Enchantment CONDUCTIVITY_CURSE = new ConductivityCurseEnchantment();
	public static final Enchantment SICKNESS_CURSE = new SicknessCurseEnchantment();
	public static final Enchantment SLIDING_CURSE = new SlidingCurseEnchantment();
	public static final Enchantment DEPTH_CURSE = new DepthsCurseEnchantment();
	public static final Enchantment FLAMMABILITY_CURSE = new FlammabilityCurseEnchantment();
	public static final Enchantment SUFFOCATION_CURSE = new SuffocationCurseEnchantment();
	public static final Enchantment HEAVY_CURSE = new HeavyCurseEnchantment();
	public static final Enchantment INACCURACY_CURSE = new InaccuracyCurseEnchantment();
	public static final Enchantment BUCKLING_CURSE = new BucklingCurseEnchantment();

	public static void init()
	{
		RegistryHelper.register(Registry.ENCHANTMENT, Enchantment.class, BMEnchantments.class);
	}
}
