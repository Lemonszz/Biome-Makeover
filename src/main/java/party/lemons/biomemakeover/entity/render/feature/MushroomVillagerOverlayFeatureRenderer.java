package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DecayedEntity;
import party.lemons.biomemakeover.entity.MushroomVillagerEntity;
import party.lemons.biomemakeover.entity.render.DecayedEntityModel;

public class MushroomVillagerOverlayFeatureRenderer extends FeatureRenderer<MushroomVillagerEntity, VillagerResemblingModel<MushroomVillagerEntity>>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/mushrooming_trader_outer.png");
	private final VillagerResemblingModel<MushroomVillagerEntity> model = new VillagerResemblingModel(0);

	public MushroomVillagerOverlayFeatureRenderer(MobEntityRenderer<MushroomVillagerEntity, VillagerResemblingModel<MushroomVillagerEntity>> featureRendererContext) {
		super(featureRendererContext);
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, MushroomVillagerEntity e, float f, float g, float h, float j, float k, float l) {
		BlockPos blockPos = new BlockPos(e.method_31166(f));
		int light = LightmapTextureManager.pack(e.isOnFire() ? 15 : e.world.getLightLevel(LightType.BLOCK, blockPos), e.world.getLightLevel(LightType.SKY, blockPos));

		render(this.getContextModel(), this.model, TEXTURE, matrixStack, vertexConsumerProvider, light, e, f, g, j, k, l, h, 1.0F, 1.0F, 1.0F);
	}
}
