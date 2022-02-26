package party.lemons.biomemakeover.init;

import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.mixin.PotionBrewingInvoker;
import party.lemons.biomemakeover.mobeffect.AntidoteMobEffect;
import party.lemons.biomemakeover.mobeffect.BMMobEffect;
import party.lemons.biomemakeover.mobeffect.NocturnalMobEffect;
import party.lemons.taniwha.registry.RegistryHelper;

public class BMPotions
{
    public static final MobEffect SHOCKED = new BMMobEffect(MobEffectCategory.HARMFUL, 0x6effff).addAttributeModifier(Attributes.MAX_HEALTH, "ad5a6d44-4a23-11eb-b378-0242ac130002", -2D, AttributeModifier.Operation.ADDITION);
    public static final MobEffect ANTIDOTE = new AntidoteMobEffect();
    public static final MobEffect NOCTURNAL = new NocturnalMobEffect();

    public static final Potion ADRENALINE = new Potion("adrenaline", new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2400, 1), new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 1), new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2400));
    public static final Potion ASSASSIN = new Potion("assassin", new MobEffectInstance(MobEffects.INVISIBILITY, 2400), new MobEffectInstance(MobEffects.SLOW_FALLING, 2400, 1), new MobEffectInstance(MobEffects.JUMP, 2400, 2));
    public static final Potion DARKNESS = new Potion("darkness", new MobEffectInstance(MobEffects.BLINDNESS, 300), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 0), new MobEffectInstance(MobEffects.WEAKNESS, 170, 0));
    public static final Potion DOLPHIN_MASTER = new Potion("dolphin_master", new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 1800), new MobEffectInstance(MobEffects.WATER_BREATHING, 1800));
    public static final Potion LIQUID_BREAD = new Potion("liquid_bread", new MobEffectInstance(MobEffects.SATURATION, 1800), new MobEffectInstance(MobEffects.ABSORPTION, 1800, 4));
    public static final Potion PHANTOM_SPIRIT = new Potion("phantom_spirit", new MobEffectInstance(MobEffects.NIGHT_VISION, 2400), new MobEffectInstance(MobEffects.LEVITATION, 600, 0), new MobEffectInstance(MobEffects.SLOW_FALLING, 1000));
    public static final Potion LIGHT_FOOTED = new Potion("light_footed", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 1), new MobEffectInstance(MobEffects.JUMP, 1200, 0), new MobEffectInstance(MobEffects.SLOW_FALLING, 1200));
    public static final Potion MINER = new Potion("miner", new MobEffectInstance(MobEffects.DIG_SPEED, 3600, 1), new MobEffectInstance(MobEffects.NIGHT_VISION, 4250, 0), new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200));

    public static final Potion ANTIDOTE_POT = new Potion("antidote", new MobEffectInstance(ANTIDOTE, 1));
    public static final Potion NOCTURNAL_POT = new Potion("nocturnal", new MobEffectInstance(NOCTURNAL, 72000));
    public static final Potion LONG_NOCTURNAL_POT = new Potion("nocturnal", new MobEffectInstance(NOCTURNAL, 144000));

    public static void init()
    {
        RegistryHelper.register(Constants.MOD_ID, Registry.MOB_EFFECT, MobEffect.class, BMPotions.class);
        RegistryHelper.register(Constants.MOD_ID, Registry.POTION, Potion.class, BMPotions.class);

        PotionBrewingInvoker.callAddMix(Potions.AWKWARD, BMItems.WART, ANTIDOTE_POT);
        PotionBrewingInvoker.callAddMix(Potions.AWKWARD, BMItems.MOTH_SCALES, NOCTURNAL_POT);
        PotionBrewingInvoker.callAddMix(NOCTURNAL_POT, Items.REDSTONE, LONG_NOCTURNAL_POT);
    }
}
