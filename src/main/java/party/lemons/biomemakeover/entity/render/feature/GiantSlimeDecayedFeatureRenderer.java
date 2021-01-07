package party.lemons.biomemakeover.entity.render.feature;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.StuckArrowsFeatureRenderer;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.mob.SlimeEntity;
import party.lemons.biomemakeover.entity.render.GiantSlimeRender;

public class GiantSlimeDecayedFeatureRenderer<T extends SlimeEntity> extends FeatureRenderer<T, SlimeEntityModel<T>>
{

	public GiantSlimeDecayedFeatureRenderer(FeatureRendererContext<T, SlimeEntityModel<T>> context)
	{
		super(context);
	}



	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		if(GiantSlimeRender.DUMMY_DECAYED != null)
		{
			matrices.push();
			matrices.scale(-1, -1, -1);
			matrices.scale(0.15F, 0.15F, 0.15F);
			GiantSlimeRender.DUMMY_DECAYED.setPose(EntityPose.SWIMMING);
			MinecraftClient.getInstance().getEntityRenderDispatcher().render(GiantSlimeRender.DUMMY_DECAYED, 0,-8.7F,0.65F, headYaw, tickDelta, matrices, vertexConsumers, light);
			matrices.pop();
		}
	}
}
