package party.lemons.biomemakeover.mixin.badlands;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.entity.render.feature.CowboyHatRenderLayer;

@Mixin(HorseRenderer.class)
public abstract class HorseRendererMixin extends AbstractHorseRenderer<Horse, HorseModel<Horse>> {

    private HorseRendererMixin(EntityRendererProvider.Context context, HorseModel<Horse> horseModel, float f) {
        super(context, horseModel, f);
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    public void onConstruct(EntityRendererProvider.Context context, CallbackInfo cbi) {
        addLayer(new CowboyHatRenderLayer<Horse, HorseModel<Horse>>(this, context.getModelSet()) {
            @Override
            protected void setup(PoseStack poseStack) {
                poseStack.scale(1.05F, 1.05F, 1.05F);

                getParentModel().headParts().iterator().next().translateAndRotate(poseStack);
                poseStack.translate(0F, -0.4F, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(-25F));
            }
        });
    }
}
