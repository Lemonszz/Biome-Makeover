package party.lemons.biomemakeover.statuseffect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import party.lemons.biomemakeover.init.BMCriterion;
import party.lemons.biomemakeover.util.access.StatusEffectAccess;
import party.lemons.biomemakeover.util.extensions.Stuntable;

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
		doEffect(target);
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier)
	{
		doEffect(entity);
		super.onApplied(entity, attributes, amplifier);
	}

	public void doEffect(LivingEntity target)
	{
		target.getStatusEffects().stream().filter((e)->((StatusEffectAccess) e.getEffectType()).bm_getType() == StatusEffectType.HARMFUL).collect(Collectors.toList()).forEach(e->target.removeStatusEffect(e.getEffectType()));

		if(!target.world.isClient())
		{
			if(target instanceof PlayerEntity)
				BMCriterion.ANTIDOTE.trigger((ServerPlayerEntity) target);

			if(target instanceof Stuntable)
				((Stuntable) target).setStunted(false);
		}
	}

}
