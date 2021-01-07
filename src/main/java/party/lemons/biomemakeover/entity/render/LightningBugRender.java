package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DragonflyEntity;
import party.lemons.biomemakeover.entity.LightningBugEntity;
import party.lemons.biomemakeover.entity.MushroomVillagerEntity;
import party.lemons.biomemakeover.entity.render.feature.LightningBugInnerFeatureRenderer;
import party.lemons.biomemakeover.entity.render.feature.LightningBugOuterFeatureRenderer;

public class LightningBugRender extends MobEntityRenderer<LightningBugEntity, LightningBugEntityModel>
{
	public static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/lightning_bug.png");

	public LightningBugRender(EntityRenderDispatcher r)
	{
		super(r, new LightningBugEntityModel(), 0.25F);
		addFeature(new LightningBugInnerFeatureRenderer(this));
		addFeature(new LightningBugOuterFeatureRenderer(this));
	}

	@Override
	protected RenderLayer getRenderLayer(LightningBugEntity entity, boolean showBody, boolean translucent, boolean showOutline)
	{
		return null;
	}

	@Override
	protected void scale(LightningBugEntity entity, MatrixStack matrices, float amount)
	{
		super.scale(entity, matrices, amount);
		entity.scale += amount / 10;
		if(entity.scale > 99999)
			entity.scale =  0;

		float sc = 0.9F + ((float)Math.sin(entity.scale) / 5F);
		shadowRadius = sc / 10F;
		matrices.scale(sc, sc, sc);
	}

	@Override
	protected int getBlockLight(LightningBugEntity entity, BlockPos blockPos)
	{
		return 15;
	}

	@Override
	public Identifier getTexture(LightningBugEntity entity)
	{
		return TEXTURE;
	}
}
