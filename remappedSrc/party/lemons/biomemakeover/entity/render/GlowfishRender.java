package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.SalmonEntityRenderer;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.render.feature.BlightbatFeatureRenderer;
import party.lemons.biomemakeover.entity.render.feature.GlowfishFeatureRenderer;

public class GlowfishRender extends SalmonEntityRenderer
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/glow_fish.png");


	public GlowfishRender(EntityRenderDispatcher r)
	{
		super(r);
		this.addFeature(new GlowfishFeatureRenderer(this));
	}

	@Override
	public Identifier getTexture(SalmonEntity salmonEntity)
	{
		return TEXTURE;
	}

	@Override
	protected int getBlockLight(SalmonEntity entity, BlockPos blockPos)
	{
		return 15;
	}
}
