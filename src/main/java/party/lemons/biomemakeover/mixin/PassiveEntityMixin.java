package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.util.Stuntable;

@Mixin(PassiveEntity.class)
public class PassiveEntityMixin implements Stuntable
{
	private boolean isStunted = false;

	@Inject(at = @At("HEAD"), method = "isBaby", cancellable = true)
	private void isBaby(CallbackInfoReturnable<Boolean> cbi)
	{
		if(isStunted) cbi.setReturnValue(true);
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

	@Override
	public boolean isStunted()
	{
		return isStunted;
	}

	@Override
	public void setStunted(boolean stunted)
	{
		this.isStunted = stunted;
	}
}
