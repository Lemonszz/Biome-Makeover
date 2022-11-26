package party.lemons.biomemakeover.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.util.extension.Stuntable;

@Mixin(Tadpole.class)
public abstract class TadpoleMixin extends AbstractFish implements Stuntable
{
	@Shadow
	private int age;

	@Shadow public abstract void readAdditionalSaveData(CompoundTag arg);

	private boolean isStunted = false;


	public TadpoleMixin(EntityType<? extends AbstractFish> entityType, Level level)
	{
		super(entityType, level);
	}

	@Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
	private void addAdditionalData(CompoundTag tag, CallbackInfo cbi)
	{
		tag.putBoolean("bm_IsStunted", isStunted());
	}

	@Inject(at = @At("HEAD"), method = "readAdditionalSaveData")
	private void readAdditionalData(CompoundTag tag, CallbackInfo cbi)
	{
		if(tag.contains("bm_IsStunted"))
			setStunted(tag.getBoolean("bm_IsStunted"));
	}

	@Inject(at = @At("HEAD"), method = "getAge", cancellable = true)
	public void getAge(CallbackInfoReturnable<Integer> cbi)
	{
		if(isStunted())
		{
			cbi.setReturnValue(-6000);
		}
	}

	@Inject(at = @At("HEAD"), method = "setAge", cancellable = true)
	public void setAge(int age, CallbackInfo cbi)
	{
		if(isStunted()) {
			this.age = -6000;
			cbi.cancel();
		}
	}

	@Override
	public boolean isAlwaysBaby()
	{
		return true;
	}

	@Override
	public boolean isStunted() {
		return isStunted;
	}

	@Override
	public void setStunted(boolean stunted)
	{
		this.isStunted = stunted;
	}
}
