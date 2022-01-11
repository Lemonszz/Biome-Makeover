package party.lemons.biomemakeover.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.util.extension.Stuntable;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin_Stuntable extends PathfinderMob implements Stuntable
{
    @Shadow @Final private static EntityDataAccessor<Boolean> DATA_BABY_ID;
    @Shadow protected int age;
    private boolean isStunted = false;

    @Inject(at = @At("HEAD"), method = "isBaby", cancellable = true)
    private void isBaby(CallbackInfoReturnable<Boolean> cbi)
    {
        if(isStunted())
            cbi.setReturnValue(true);
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
            getEntityData().set(DATA_BABY_ID, true);
        }
    }

    @Inject(at = @At("HEAD"), method = "setAge", cancellable = true)
    public void setAge(int age, CallbackInfo cbi)
    {
        if(isStunted()) {
            this.age = -6000;
            getEntityData().set(DATA_BABY_ID, true);
            cbi.cancel();
        }
    }

    @Override
    public boolean isStunted() {
        return isStunted;
    }

    @Override
    public void setStunted(boolean stunted)
    {
        this.isStunted = stunted;
        if(isStunted)
            getEntityData().set(DATA_BABY_ID, true);
    }

    protected AgeableMobMixin_Stuntable(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }
}
