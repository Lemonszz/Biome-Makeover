package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.VillagerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.entity.MushroomVillagerEntity;
import party.lemons.biomemakeover.entity.render.feature.MushroomVillagerFeatureRenderer;

public class MushroomVillagerRender extends MobEntityRenderer<MushroomVillagerEntity, VillagerResemblingModel<MushroomVillagerEntity>>
{
	private static final Identifier TEXTURE = new Identifier("textures/entity/wandering_trader.png");

	public MushroomVillagerRender(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new VillagerResemblingModel(0.0F), 0.5F);
		this.addFeature(new HeadFeatureRenderer(this));
		this.addFeature(new VillagerHeldItemFeatureRenderer(this));
		this.addFeature(new MushroomVillagerFeatureRenderer(this));
	}

	public Identifier getTexture(MushroomVillagerEntity wanderingTraderEntity) {
		return TEXTURE;
	}

	protected void scale(MushroomVillagerEntity wanderingTraderEntity, MatrixStack matrixStack, float f) {
		float g = 0.9375F;
		matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
	}
}
