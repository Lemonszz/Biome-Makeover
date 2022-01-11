package party.lemons.biomemakeover.mixin.multipart;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.entity.mutipart.MultiPartEntity;

@Mixin(ServerLevel.EntityCallbacks.class)
public class EntityCallbacksMixin
{
    @Inject(at = @At("HEAD"), method = "onTrackingEnd")
    public void onEndTracking(Entity e, CallbackInfo cbi)
    {
        if(e instanceof MultiPartEntity mpe)
            MultiPartEntity.unload(mpe);
    }

    @Inject(at = @At("TAIL"), method = "onTrackingStart")
    public void onStartTracking(Entity e, CallbackInfo cbi)
    {
        if(e instanceof MultiPartEntity<?> mpe)
        {
            MultiPartEntity.loadParts(mpe);
        }
    }
}
