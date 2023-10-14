package party.lemons.biomemakeover.init;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import party.lemons.biomemakeover.BMConfig;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.item.enchantment.*;

import java.util.function.Supplier;

public class BMEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTS = DeferredRegister.create(Constants.MOD_ID, Registries.ENCHANTMENT);
    public static final TagKey<Enchantment> ALTAR_CURSE_EXCLUDED = TagKey.create(Registries.ENCHANTMENT, BiomeMakeover.ID("altar_curse_excluded"));
    public static final TagKey<Enchantment> ALTAR_CANT_UPGRADE = TagKey.create(Registries.ENCHANTMENT, BiomeMakeover.ID("altar_cant_upgrade"));

    public static final Supplier<Enchantment> DECAY_CURSE = ENCHANTS.register(BiomeMakeover.ID("decay_curse"), ()->new DecayCurseEnchantment(()->BMConfig.INSTANCE.enchantmentConfig.DECAY));
    public static final Supplier<Enchantment> INSOMNIA_CURSE = ENCHANTS.register(BiomeMakeover.ID("insomnia_curse"), ()->new InsomniaCurseEnchantment(()->BMConfig.INSTANCE.enchantmentConfig.INSOMNIA));
    public static final Supplier<Enchantment> CONDUCTIVITY_CURSE = ENCHANTS.register(BiomeMakeover.ID("conductivity_curse"), ()->new ConductivityCurseEnchantment(()->BMConfig.INSTANCE.enchantmentConfig.CONDUCTIVITY));
    public static final Supplier<Enchantment> ENFEEBLEMENT_CURSE = ENCHANTS.register(BiomeMakeover.ID("enfeeblement_curse"), ()->new EnfeeblementCurseEnchantment(()->BMConfig.INSTANCE.enchantmentConfig.ENFEEBLEMENT));
    public static final Supplier<Enchantment> DEPTH_CURSE = ENCHANTS.register(BiomeMakeover.ID("depth_curse"), ()->new DepthsCurseEnchantment(()->BMConfig.INSTANCE.enchantmentConfig.DEPTHS));
    public static final Supplier<Enchantment> FLAMMABILITY_CURSE = ENCHANTS.register(BiomeMakeover.ID("flammability_curse"), ()->new BMEnchantment(()->BMConfig.INSTANCE.enchantmentConfig.FLAMMABILITY, true, Enchantment.Rarity.UNCOMMON, EnchantmentCategory.ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}));
    public static final Supplier<Enchantment> SUFFOCATION_CURSE = ENCHANTS.register(BiomeMakeover.ID("suffocation_curse"), ()->new BMEnchantment(()->BMConfig.INSTANCE.enchantmentConfig.SUFFOCATION, true, Enchantment.Rarity.UNCOMMON, EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD}));
    public static final Supplier<Enchantment> UNWIELDINESS_CURSE = ENCHANTS.register(BiomeMakeover.ID("unwieldiness_curse"), ()->new UnwieldinessCurseEnchantment(()->BMConfig.INSTANCE.enchantmentConfig.UNWIELDINESS));
    public static final Supplier<Enchantment> INACCURACY_CURSE = ENCHANTS.register(BiomeMakeover.ID("inaccuracy_curse"), ()->new BMEnchantment(()->BMConfig.INSTANCE.enchantmentConfig.INACCURACY, true, Enchantment.Rarity.UNCOMMON, EnchantmentCategory.BOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
    public static final Supplier<Enchantment> BUCKLING_CURSE = ENCHANTS.register(BiomeMakeover.ID("buckling_curse"), ()->new BMEnchantment(()->BMConfig.INSTANCE.enchantmentConfig.BUCKLING, true, Enchantment.Rarity.UNCOMMON, EnchantmentCategory.ARMOR_LEGS, new EquipmentSlot[]{EquipmentSlot.LEGS}));

    public static void init() {
        ENCHANTS.register();
    }
}