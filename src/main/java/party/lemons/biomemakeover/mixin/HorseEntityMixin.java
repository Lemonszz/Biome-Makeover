package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.nbt.CompoundTag;
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
	private boolean cowboySpawned = false;

	protected HorseEntityMixin(EntityType<? extends HorseBaseEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Inject(at = @At("TAIL"), method = "initDataTracker")
	private void initDataTacker(CallbackInfo cbi)
	{
		this.dataTracker.startTracking(HAS_HAT, false);
	}

	@Inject(at = @At("RETURN"), method = "writeCustomDataToTag")
	private void writeData(CompoundTag tag, CallbackInfo cbi)
	{
		tag.putBoolean("Hat", hasHat());
		tag.putBoolean("CowboySpawned", cowboySpawned);
	}

	@Inject(at = @At("RETURN"), method = "readCustomDataFromTag")
	private void readData(CompoundTag tag, CallbackInfo cbi)
	{
		if(tag.contains("Hat"))
			this.dataTracker.set(HAS_HAT, tag.getBoolean("Hat"));

		if(tag.contains("CowboySpawned"))
			cowboySpawned = tag.getBoolean("CowboySpawned");
	}

	@Override
	public boolean canImmediatelyDespawn(double distanceSquared)
	{
		if(getPrimaryPassenger() == null)
			return cowboySpawned;
		else
		{
			if(getPrimaryPassenger() instanceof PatrolEntity)
			{
				return ((PatrolEntity) getPrimaryPassenger()).canImmediatelyDespawn(distanceSquared);
			}
		}
		return cowboySpawned;
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

	@Override
	public void setCowboySpawned()
	{
		cowboySpawned = true;
	}
}
