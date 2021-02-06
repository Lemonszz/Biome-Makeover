package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BatEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.render.feature.BlightbatFeatureRenderer;

public class BlightBatRender extends MobEntityRenderer<BatEntity, EntityModel<BatEntity>>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/glowbat.png");

	public BlightBatRender(EntityRenderDispatcher entityRenderDispatcher)
	{
		super(entityRenderDispatcher, new BatEntityModel(), 0.25F);
		this.addFeature(new BlightbatFeatureRenderer(this));
	}

	@Override
	protected int getBlockLight(BatEntity entity, BlockPos blockPos)
	{
		return 15;
	}

	public Identifier getTexture(BatEntity batEntity)
	{
		return TEXTURE;
	}

	protected void scale(BatEntity batEntity, MatrixStack matrixStack, float f)
	{
		matrixStack.scale(0.35F, 0.35F, 0.35F);
	}

	protected void setupTransforms(BatEntity batEntity, MatrixStack matrixStack, float f, float g, float h)
	{
		if(batEntity.isRoosting())
		{
			matrixStack.translate(0.0D, -0.10000000149011612D, 0.0D);
		}else
		{
			matrixStack.translate(0.0D, MathHelper.cos(f * 0.3F) * 0.1F, 0.0D);
		}

		super.setupTransforms(batEntity, matrixStack, f, g, h);
	}
}
