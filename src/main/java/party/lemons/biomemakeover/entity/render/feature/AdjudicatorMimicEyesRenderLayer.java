package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorMimicEntity;
import party.lemons.biomemakeover.entity.render.AdjudicatorEntityModel;

public class AdjudicatorMimicEyesRenderLayer extends EyesFeatureRenderer<AdjudicatorMimicEntity, AdjudicatorEntityModel<AdjudicatorMimicEntity>>
{
	private static final RenderLayer TEXTURE = RenderLayer.getEyes(BiomeMakeover.ID("textures/entity/adjudicator_eyes.png"));

	public AdjudicatorMimicEyesRenderLayer(FeatureRendererContext<AdjudicatorMimicEntity, AdjudicatorEntityModel<AdjudicatorMimicEntity>> ctx)
	{
		super(ctx);
	}

	@Override
	public RenderLayer getEyesTexture()
	{
		return TEXTURE;
	}
}
