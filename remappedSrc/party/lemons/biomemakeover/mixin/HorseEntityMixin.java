package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.util.HorseHat;

@Mixin(HorseEntity.class)
public class HorseEntityMixin extends HorseBaseEntity implements HorseHat
{
	private static final TrackedData<Boolean> HAS_HAT = DataTracker.registerData(HorseEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	protected HorseEntityMixin(EntityType<? extends HorseBaseEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Inject(at = @At("TAIL"), method = "initDataTracker")
	public void initDataTacker(CallbackInfo cbi)
	{
		this.dataTracker.startTracking(HAS_HAT, false);
	}

	@Override
	public boolean hasHat()
	{
		return this.dataTracker.get(HAS_HAT);
	}

	@Override
	public void setHat()
	{
		this.dataTracker.set(HAS_HAT, true);
	}
}
