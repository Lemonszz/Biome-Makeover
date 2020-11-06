package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.entity.render.feature.HatFeatureRenderer;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>
{
	public PlayerRendererMixin(EntityRenderDispatcher dispatcher, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius)
	{
		super(dispatcher, model, shadowRadius);
	}

	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Z)V")
	public void onConstruct(EntityRenderDispatcher dispatcher, boolean smallarms, CallbackInfo info)
	{
		this.addFeature(new HatFeatureRenderer<>(this));
	}
}
