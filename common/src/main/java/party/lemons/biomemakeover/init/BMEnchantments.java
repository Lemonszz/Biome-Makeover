package party.lemons.biomemakeover.init;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.item.enchantment.*;

import java.util.function.Supplier;

public class BMEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTS = DeferredRegister.create(Constants.MOD_ID, Registries.ENCHANTMENT);
    public static final TagKey<Enchantment> ALTAR_CURSE_EXCLUDED = TagKey.create(Registries.ENCHANTMENT, BiomeMakeover.ID("altar_curse_excluded"));
    public static final TagKey<Enchantment> ALTAR_CANT_UPGRADE = TagKey.create(Registries.ENCHANTMENT, BiomeMakeover.ID("altar_cant_upgrade"));

    //TODO: Some of these don't really need their own class.

    public static final Supplier<Enchantment> DECAY_CURSE = ENCHANTS.register(BiomeMakeover.ID("decay_curse"), DecayCurseEnchantment::new);
    public static final Supplier<Enchantment> INSOMNIA_CURSE = ENCHANTS.register(BiomeMakeover.ID("insomnia_curse"), InsomniaCurseEnchantment::new);
    public static final Supplier<Enchantment> CONDUCTIVITY_CURSE = ENCHANTS.register(BiomeMakeover.ID("conductivity_curse"), ConductivityCurseEnchantment::new);
    public static final Supplier<Enchantment> ENFEEBLEMENT_CURSE = ENCHANTS.register(BiomeMakeover.ID("enfeeblement_curse"), EnfeeblementCurseEnchantment::new);
    public static final Supplier<Enchantment> DEPTH_CURSE = ENCHANTS.register(BiomeMakeover.ID("depth_curse"), DepthsCurseEnchantment::new);
    public static final Supplier<Enchantment> FLAMMABILITY_CURSE = ENCHANTS.register(BiomeMakeover.ID("flammability_curse"), FlammabilityCurseEnchantment::new);
    public static final Supplier<Enchantment> SUFFOCATION_CURSE = ENCHANTS.register(BiomeMakeover.ID("suffocation_curse"), SuffocationCurseEnchantment::new);
    public static final Supplier<Enchantment> UNWIELDINESS_CURSE = ENCHANTS.register(BiomeMakeover.ID("unwieldiness_curse"), UnwieldinessCurseEnchantment::new);
    public static final Supplier<Enchantment> INACCURACY_CURSE = ENCHANTS.register(BiomeMakeover.ID("inaccuracy_curse"), InaccuracyCurseEnchantment::new);
    public static final Supplier<Enchantment> BUCKLING_CURSE = ENCHANTS.register(BiomeMakeover.ID("buckling_curse"), BucklingCurseEnchantment::new);

    public static void init() {
        ENCHANTS.register();
    }
}