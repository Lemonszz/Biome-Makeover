package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.util.extensions.Stuntable;

@Mixin(PassiveEntity.class)
public abstract class PassiveEntityMixin extends PathAwareEntity implements Stuntable
{
	@Shadow @Final private static TrackedData<Boolean> CHILD;
	@Shadow protected int breedingAge;
	private boolean isStunted = false;

	protected PassiveEntityMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("HEAD"), method = "isBaby", cancellable = true)
	private void isBaby(CallbackInfoReturnable<Boolean> cbi)
	{
		if(isStunted()) cbi.setReturnValue(true);
	}

	@Inject(at = @At("RETURN"), method = "writeCustomDataToTag")
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo cbi)
	{
		tag.putBoolean("IsStunted", isStunted);
	}

	@Inject(at = @At("RETURN"), method = "readCustomDataFromTag")
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo cbi)
	{
		if(tag.contains("IsStunted")) isStunted = tag.getBoolean("IsStunted");
	}

	@Inject(at = @At("HEAD"), method = "getBreedingAge", cancellable = true)
	public void getBreedingAge(CallbackInfoReturnable<Integer> cbi)
	{
		if(isStunted()) {
			cbi.setReturnValue(-6000);
			dataTracker.set(CHILD, true);
		}
	}

	@Inject(at = @At("HEAD"), method = "setBreedingAge", cancellable = true)
	public void setBreedingAge(int age, CallbackInfo cbi)
	{
		if(isStunted()) {
			breedingAge = -6000;
			dataTracker.set(CHILD, true);
			cbi.cancel();
		}
	}

	@Override
	public boolean isStunted()
	{
		return isStunted;
	}

	@Override
	public void setStunted(boolean stunted)
	{
		this.isStunted = stunted;
		if(isStunted)
			dataTracker.set(CHILD, true);
	}
}
