package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.access.StatusEffectAccess;

@Mixin(StatusEffect.class)
public class StatusEffectMixin implements StatusEffectAccess
{

	@Shadow
	@Final
	private StatusEffectType type;

	@Override
	public StatusEffectType bm_getType()
	{
		return type;
	}
}
