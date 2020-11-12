package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import party.lemons.biomemakeover.entity.MushroomVillagerEntity;
import party.lemons.biomemakeover.init.BMBlocks;

public class MushroomVillagerFeatureRenderer<T extends MushroomVillagerEntity> extends FeatureRenderer<T, VillagerResemblingModel<T>>
{
	public MushroomVillagerFeatureRenderer(FeatureRendererContext<T, VillagerResemblingModel<T>> ctx) {
		super(ctx);
	}

	public void render(MatrixStack ms, VertexConsumerProvider vcp, int i, T e, float f, float g, float h, float j, float k, float l) {
		if (!e.isBaby() && !e.isInvisible()) {
			BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
			BlockState shroom = BMBlocks.GREEN_GLOWSHROOM.getDefaultState();
			int m = LivingEntityRenderer.getOverlay(e, 0.0F);

			ms.push();
			this.getContextModel().getHead().rotate(ms);
			ms.translate(0.0D, -1, 0);
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-78.0F));
			ms.scale(-1.0F, -1.0F, 1.0F);
			ms.translate(-0.5D, -0.5D, -0.5D);
			blockRenderManager.renderBlockAsEntity(shroom, ms, vcp, i, m);
			ms.pop();

			ms.push();
			this.getContextModel().getHead().rotate(ms);
			ms.translate(-0.4D, -0.5, -0.5);
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(75));
			ms.multiply(Vector3f.NEGATIVE_Z.getDegreesQuaternion(30));
			ms.scale(-1.0F, -1.0F, 1.0F);
			ms.translate(-0.5D, -0.5D, -0.5D);
			blockRenderManager.renderBlockAsEntity(shroom, ms, vcp, i, m);
			ms.pop();
		}
	}
}
