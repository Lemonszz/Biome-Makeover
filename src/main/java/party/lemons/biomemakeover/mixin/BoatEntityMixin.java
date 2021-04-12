package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.access.BoatAccessor;

@Mixin(BoatEntity.class)
public class BoatEntityMixin implements BoatAccessor
{
	@Shadow
	private float yawVelocity;

	@Override
	public float bm_getYawVelocity()
	{
		return yawVelocity;
	}

}
