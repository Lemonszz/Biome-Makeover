package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.HorseEntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.entity.passive.HorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.entity.render.feature.HorseHatFeatureRenderer;

@Mixin(HorseEntityRenderer.class)
public abstract class HorseEntityRendererMixin extends LivingEntityRenderer<HorseEntity, HorseEntityModel<HorseEntity>>
{
	public HorseEntityRendererMixin(EntityRenderDispatcher entityRenderDispatcher, HorseEntityModel model, float shadowSize)
	{
		super(entityRenderDispatcher, model, shadowSize);
	}

	@Inject(at = @At("RETURN"), method = "<init>")
	public void onConstruct(EntityRenderDispatcher dispatcher, CallbackInfo info)
	{
		this.addFeature(new HorseHatFeatureRenderer<>(this));
	}
}
