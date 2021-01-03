package party.lemons.biomemakeover.entity.render.feature;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.SlimeEntity;

public class GiantSlimeDecayedFeatureRenderer<T extends SlimeEntity> extends FeatureRenderer<T, SlimeEntityModel<T>>
{

	public GiantSlimeDecayedFeatureRenderer(FeatureRendererContext<T, SlimeEntityModel<T>> context)
	{
		super(context);
	}



	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		RenderSystem.pushMatrix();

		RenderSystem.popMatrix();
	}
}
