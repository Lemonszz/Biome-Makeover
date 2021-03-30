package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RavagerEntity.class)
public abstract class RavagerEntityMixin extends RaiderEntity
{
	@Override
	public boolean canTarget(LivingEntity target)
	{
		if(target.getVehicle() == this)
			return false;

		return super.canTarget(target);
	}

	protected RavagerEntityMixin(EntityType<? extends RaiderEntity> entityType, World world)
	{
		super(entityType, world);
	}
}
