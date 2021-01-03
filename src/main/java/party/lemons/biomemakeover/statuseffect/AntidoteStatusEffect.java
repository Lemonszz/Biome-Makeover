package party.lemons.biomemakeover.statuseffect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;

import java.util.List;
import java.util.stream.Collectors;

public class AntidoteStatusEffect extends InstantStatusEffect
{
	public AntidoteStatusEffect()
	{
		super(StatusEffectType.BENEFICIAL, 0xFFFFFF);
	}

	@Override
	public void applyInstantEffect(Entity source, Entity attacker, LivingEntity target, int amplifier, double proximity)
	{
		target.getStatusEffects()
				.stream()
				.filter(
						(e)->!e.getEffectType().isBeneficial())
				.collect(Collectors.toList())
					.forEach(
							e->target.removeStatusEffect(e.getEffectType())
					);
	}
}
