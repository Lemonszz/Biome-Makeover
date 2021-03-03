package party.lemons.biomemakeover.statuseffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;

public class NocturnalStatusEffect extends BMStatusEffect
{
	public NocturnalStatusEffect(StatusEffectType type, int color)
	{
		super(type, color);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier)
	{
		super.applyUpdateEffect(entity, amplifier);

		if(!entity.world.isClient() && entity instanceof ServerPlayerEntity)
		{
			((PlayerEntity)entity).resetStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST));
		}
	}
}
