package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.OwlEntity;
import party.lemons.biomemakeover.entity.render.OwlEntityModel;

public class OwlEyesRenderLayer extends EyesFeatureRenderer<OwlEntity, OwlEntityModel>
{
	private static final RenderLayer TEXTURE = RenderLayer.getEyes(BiomeMakeover.ID("textures/entity/owl_eyes.png"));

	public OwlEyesRenderLayer(FeatureRendererContext<OwlEntity, OwlEntityModel> ctx)
	{
		super(ctx);
	}

	@Override
	public RenderLayer getEyesTexture()
	{
		return TEXTURE;
	}
}
