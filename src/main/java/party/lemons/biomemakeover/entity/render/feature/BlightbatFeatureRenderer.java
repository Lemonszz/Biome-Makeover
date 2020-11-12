package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.passive.BatEntity;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.access.BatEntityModelAccessor;

public class BlightbatFeatureRenderer extends FeatureRenderer<BatEntity, EntityModel<BatEntity>>
{
	public BlightbatFeatureRenderer(FeatureRendererContext<BatEntity, EntityModel<BatEntity>> context) {
		super(context);
	}

	public void render(MatrixStack ms, VertexConsumerProvider vcp, int i, BatEntity e, float f, float g, float h, float j, float k, float l) {
		if (!e.isBaby() && !e.isInvisible()) {
			BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
			BlockState shroom = BMBlocks.PURPLE_GLOWSHROOM.getDefaultState();
			int m = LivingEntityRenderer.getOverlay(e, 0.0F);

			ms.push();
			((BatEntityModelAccessor)this.getContextModel()).getHead().rotate(ms);
			ms.translate(0.0D, -0.7, 0);
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-78.0F));
			ms.scale(-1.0F, -1.0F, 1.0F);
			ms.translate(-0.5D, -0.5D, -0.5D);
			blockRenderManager.renderBlockAsEntity(shroom, ms, vcp, i, m);
			ms.pop();
		}
	}
}
