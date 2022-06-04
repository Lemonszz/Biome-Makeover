package party.lemons.biomemakeover.init;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.mixin.PotionBrewingInvoker;
import party.lemons.biomemakeover.mobeffect.AntidoteMobEffect;
import party.lemons.biomemakeover.mobeffect.BMMobEffect;
import party.lemons.biomemakeover.mobeffect.NocturnalMobEffect;
import party.lemons.biomemakeover.util.registry.RegistryHelper;

import java.util.function.Supplier;

public class BMPotions
{
    private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Constants.MOD_ID, Registry.MOB_EFFECT_REGISTRY);
    private static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Constants.MOD_ID, Registry.POTION_REGISTRY);


    public static final Supplier<MobEffect> SHOCKED = EFFECTS.register(BiomeMakeover.ID("shocked"), ()->new BMMobEffect(MobEffectCategory.HARMFUL, 0x6effff).addAttributeModifier(Attributes.MAX_HEALTH, "ad5a6d44-4a23-11eb-b378-0242ac130002", -2D,AttributeModifier.Operation.ADDITION));
    public static final Supplier<MobEffect>  ANTIDOTE = EFFECTS.register(BiomeMakeover.ID("antidote"), AntidoteMobEffect::new);
    public static final Supplier<MobEffect>  NOCTURNAL = EFFECTS.register(BiomeMakeover.ID("nocturnal"), NocturnalMobEffect::new);

    public static final Supplier<Potion> ADRENALINE = POTIONS.register(BiomeMakeover.ID("adrenaline"), ()->new Potion("adrenaline", new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2400, 1), new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 1), new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2400)));
    public static final Supplier<Potion> ASSASSIN = POTIONS.register(BiomeMakeover.ID("assassin"), ()->new Potion("assassin", new MobEffectInstance(MobEffects.INVISIBILITY, 2400), new MobEffectInstance(MobEffects.SLOW_FALLING, 2400, 1), new MobEffectInstance(MobEffects.JUMP, 2400, 2)));
    public static final Supplier<Potion> DARKNESS = POTIONS.register(BiomeMakeover.ID("darkness"), ()->new Potion("darkness", new MobEffectInstance(MobEffects.BLINDNESS, 300), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 0), new MobEffectInstance(MobEffects.WEAKNESS, 170, 0)));
    public static final Supplier<Potion> DOLPHIN_MASTER = POTIONS.register(BiomeMakeover.ID("dolphin_master"), ()->new Potion("dolphin_master", new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 1800), new MobEffectInstance(MobEffects.WATER_BREATHING, 1800)));
    public static final Supplier<Potion> LIQUID_BREAD = POTIONS.register(BiomeMakeover.ID("liquid_bread"), ()->new Potion("liquid_bread", new MobEffectInstance(MobEffects.SATURATION, 1800), new MobEffectInstance(MobEffects.ABSORPTION, 1800, 4)));
    public static final Supplier<Potion> PHANTOM_SPIRIT = POTIONS.register(BiomeMakeover.ID("phantom_spirit"), ()->new Potion("phantom_spirit", new MobEffectInstance(MobEffects.NIGHT_VISION, 2400), new MobEffectInstance(MobEffects.LEVITATION, 600, 0), new MobEffectInstance(MobEffects.SLOW_FALLING, 1000)));
    public static final Supplier<Potion> LIGHT_FOOTED = POTIONS.register(BiomeMakeover.ID("light_footed"), ()->new Potion("light_footed", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 1), new MobEffectInstance(MobEffects.JUMP, 1200, 0), new MobEffectInstance(MobEffects.SLOW_FALLING, 1200)));
    public static final Supplier<Potion> MINER = POTIONS.register(BiomeMakeover.ID("miner"), ()->new Potion("miner", new MobEffectInstance(MobEffects.DIG_SPEED, 3600, 1), new MobEffectInstance(MobEffects.NIGHT_VISION, 4250, 0), new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200)));

    public static final Supplier<Potion> ANTIDOTE_POT = POTIONS.register(BiomeMakeover.ID("actidote_pot"), ()->new Potion("antidote", new MobEffectInstance(ANTIDOTE.get(), 1)));
    public static final Supplier<Potion> NOCTURNAL_POT = POTIONS.register(BiomeMakeover.ID("nocturnal_pot"), ()->new Potion("nocturnal", new MobEffectInstance(NOCTURNAL.get(), 72000)));
    public static final Supplier<Potion> LONG_NOCTURNAL_POT = POTIONS.register(BiomeMakeover.ID("long_nocturnal_pot"), ()->new Potion("nocturnal", new MobEffectInstance(NOCTURNAL.get(), 144000)));

    public static void init()
    {
        EFFECTS.register();
        POTIONS.register();

        LifecycleEvent.SETUP.register(()->{
                    PotionBrewingInvoker.callAddMix(Potions.AWKWARD, BMItems.WART.get(), ANTIDOTE_POT.get());
                    PotionBrewingInvoker.callAddMix(Potions.AWKWARD, BMItems.SCUTTLER_TAIL.get(), ANTIDOTE_POT.get());
                    PotionBrewingInvoker.callAddMix(Potions.AWKWARD, BMItems.MOTH_SCALES.get(), NOCTURNAL_POT.get());
                    PotionBrewingInvoker.callAddMix(NOCTURNAL_POT.get(), Items.REDSTONE, LONG_NOCTURNAL_POT.get());
                });

    }
}
