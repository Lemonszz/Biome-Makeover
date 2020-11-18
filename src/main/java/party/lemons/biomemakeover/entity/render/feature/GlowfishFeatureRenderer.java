package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.SalmonEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.passive.SalmonEntity;
import party.lemons.biomemakeover.entity.render.GlowfishRender;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.access.SalmonEntityModelAccessor;

public class GlowfishFeatureRenderer extends FeatureRenderer<SalmonEntity, SalmonEntityModel<SalmonEntity>>
{
	public GlowfishFeatureRenderer(GlowfishRender r)
	{
		super(r);
	}

	@Override
	public void render(MatrixStack ms, VertexConsumerProvider vcp, int i, SalmonEntity e, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		if (!e.isBaby() && !e.isInvisible()) {
			BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
			BlockState shroom = BMBlocks.ORANGE_GLOWSHROOM.getDefaultState();
			int m = LivingEntityRenderer.getOverlay(e, 0.0F);

			ms.push();
			((SalmonEntityModelAccessor)this.getContextModel()).getTail().rotate(ms);
			((SalmonEntityModelAccessor)this.getContextModel()).getTail().visible = false;
			ms.translate(0.0D, 0, 0.5F);
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90));
			ms.scale(-1.0F, -1.0F, 1.0F);
			ms.translate(-0.5D, -0.5D, -0.5D);
			blockRenderManager.renderBlockAsEntity(shroom, ms, vcp, i, m);
			ms.pop();
		}
	}
}
