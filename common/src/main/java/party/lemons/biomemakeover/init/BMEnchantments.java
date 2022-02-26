package party.lemons.biomemakeover.init;

import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.Enchantment;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.item.enchantment.*;
import party.lemons.taniwha.registry.RegistryHelper;

public class BMEnchantments {
    //TODO: Some of these don't really need their own class.

    public static final Enchantment DECAY_CURSE = new DecayCurseEnchantment();
    public static final Enchantment INSOMNIA_CURSE = new InsomniaCurseEnchantment();
    public static final Enchantment CONDUCTIVITY_CURSE = new ConductivityCurseEnchantment();
    public static final Enchantment ENFEEBLEMENT_CURSE = new EnfeeblementCurseEnchantment();
    public static final Enchantment DEPTH_CURSE = new DepthsCurseEnchantment();
    public static final Enchantment FLAMMABILITY_CURSE = new FlammabilityCurseEnchantment();
    public static final Enchantment SUFFOCATION_CURSE = new SuffocationCurseEnchantment();
    public static final Enchantment UNWIELDINESS_CURSE = new UnwieldinessCurseEnchantment();
    public static final Enchantment INACCURACY_CURSE = new InaccuracyCurseEnchantment();
    public static final Enchantment BUCKLING_CURSE = new BucklingCurseEnchantment();

    public static void init() {
        RegistryHelper.register(Constants.MOD_ID, Registry.ENCHANTMENT, Enchantment.class, BMEnchantments.class);
    }
}