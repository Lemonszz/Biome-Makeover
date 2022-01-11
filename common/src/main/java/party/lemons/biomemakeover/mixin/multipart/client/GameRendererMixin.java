package party.lemons.biomemakeover.mixin.multipart.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.entity.mutipart.EntityPart;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/phys/Vec3;distanceToSqr(Lnet/minecraft/world/phys/Vec3;)D", ordinal = 1), method = "pick", locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onPick(float delta, CallbackInfo cbi, Entity entity, double d, Vec3 vec3, boolean bl, int i, double e, Vec3 vec32, Vec3 vec33, float g, AABB aABB, EntityHitResult entityHitResult, Entity entity2, Vec3 vec34, double h)
    {
        if (h < e || this.minecraft.hitResult == null) {
            this.minecraft.hitResult = entityHitResult;
            if (entity2 instanceof EntityPart) {
                this.minecraft.crosshairPickEntity = entity2;
            }
        }
    }
}
