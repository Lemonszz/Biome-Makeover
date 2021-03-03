package party.lemons.biomemakeover.init;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.mixin.BrewingRecipeRegistryAccessor;
import party.lemons.biomemakeover.statuseffect.AntidoteStatusEffect;
import party.lemons.biomemakeover.statuseffect.BMStatusEffect;
import party.lemons.biomemakeover.statuseffect.NocturnalStatusEffect;
import party.lemons.biomemakeover.util.RegistryHelper;

public class BMPotions
{
	public static final StatusEffect SHOCKED = new BMStatusEffect(StatusEffectType.HARMFUL, 0x6effff).addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, "ad5a6d44-4a23-11eb-b378-0242ac130002", -2D, EntityAttributeModifier.Operation.ADDITION);
	public static final StatusEffect ANTIDOTE = new AntidoteStatusEffect();
	public static final StatusEffect NOCTURNAL = new NocturnalStatusEffect(StatusEffectType.BENEFICIAL, 0xba93c7);

	public static final Potion ADRENALINE = new Potion("adrenaline", new StatusEffectInstance(StatusEffects.STRENGTH, 2400, 1), new StatusEffectInstance(StatusEffects.SPEED, 2400, 1), new StatusEffectInstance(StatusEffects.RESISTANCE, 2400));
	public static final Potion ASSASSIN = new Potion("assassin", new StatusEffectInstance(StatusEffects.INVISIBILITY, 2400), new StatusEffectInstance(StatusEffects.SLOW_FALLING, 2400, 1), new StatusEffectInstance(StatusEffects.JUMP_BOOST, 2400, 2));
	public static final Potion DARKNESS = new Potion("darkness", new StatusEffectInstance(StatusEffects.BLINDNESS, 300), new StatusEffectInstance(StatusEffects.SLOWNESS, 300, 0), new StatusEffectInstance(StatusEffects.WEAKNESS, 170, 0));
	public static final Potion DOLPHIN_MASTER = new Potion("dolphin_master", new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 1800), new StatusEffectInstance(StatusEffects.WATER_BREATHING, 1800));
	public static final Potion LIQUID_BREAD = new Potion("liquid_bread", new StatusEffectInstance(StatusEffects.SATURATION, 1800), new StatusEffectInstance(StatusEffects.ABSORPTION, 1800, 4));
	public static final Potion PHANTOM_SPIRIT = new Potion("phantom_spirit", new StatusEffectInstance(StatusEffects.NIGHT_VISION, 2400), new StatusEffectInstance(StatusEffects.LEVITATION, 600, 0), new StatusEffectInstance(StatusEffects.SLOW_FALLING, 1000));
	public static final Potion LIGHT_FOOTED = new Potion("light_footed", new StatusEffectInstance(StatusEffects.SPEED, 1200, 1), new StatusEffectInstance(StatusEffects.JUMP_BOOST, 1200, 0), new StatusEffectInstance(StatusEffects.SLOW_FALLING, 1200));
	public static final Potion MINER = new Potion("miner", new StatusEffectInstance(StatusEffects.HASTE, 3600, 1), new StatusEffectInstance(StatusEffects.NIGHT_VISION, 4250, 0), new StatusEffectInstance(StatusEffects.SPEED, 1200));

	public static final Potion ANTIDOTE_POT = new Potion("antidote", new StatusEffectInstance(ANTIDOTE, 1));
	public static final Potion NOCTURNAL_POT = new Potion("nocturnal", new StatusEffectInstance(NOCTURNAL, 72000));
	public static final Potion LONG_NOCTURNAL_POT = new Potion("nocturnal", new StatusEffectInstance(NOCTURNAL, 144000));

	public static void init()
	{
		RegistryHelper.register(Registry.POTION, Potion.class, BMPotions.class);
		RegistryHelper.register(Registry.STATUS_EFFECT, StatusEffect.class, BMPotions.class);

		BrewingRecipeRegistryAccessor.registerPotionRecipe(Potions.AWKWARD, BMItems.WART, ANTIDOTE_POT);
	}
}
