package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorStandEntityModel;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.entity.render.feature.HatFeatureRenderer;

@Mixin(ArmorStandEntityRenderer.class)
public abstract class ArmorStandRendererMixin extends LivingEntityRenderer<ArmorStandEntity, ArmorStandEntityModel>
{
	public ArmorStandRendererMixin(EntityRenderDispatcher entityRenderDispatcher, ArmorStandEntityModel model, float shadowSize)
	{
		super(entityRenderDispatcher, model, shadowSize);
	}

	@Inject(at = @At("RETURN"), method = "<init>")
	public void onConstruct(EntityRenderDispatcher dispatcher, CallbackInfo info)
	{
		this.addFeature(new HatFeatureRenderer<>(this));
	}
}