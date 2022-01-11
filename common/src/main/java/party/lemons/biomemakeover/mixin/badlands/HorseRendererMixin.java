package party.lemons.biomemakeover.mixin.badlands;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.entity.render.feature.HorseHatRenderLayer;

@Mixin(HorseRenderer.class)
public abstract class HorseRendererMixin extends AbstractHorseRenderer<Horse, HorseModel<Horse>> {

    private HorseRendererMixin(EntityRendererProvider.Context context, HorseModel<Horse> horseModel, float f) {
        super(context, horseModel, f);
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    public void onConstruct(EntityRendererProvider.Context context, CallbackInfo cbi) {
        addLayer(new HorseHatRenderLayer(this, context.getModelSet()));
    }
}
