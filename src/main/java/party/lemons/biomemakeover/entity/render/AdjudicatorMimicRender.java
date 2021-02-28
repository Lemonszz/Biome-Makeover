package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorMimicEntity;
import party.lemons.biomemakeover.entity.render.feature.AdjudicatorEyesRenderLayer;
import party.lemons.biomemakeover.entity.render.feature.AdjudicatorMimicEyesRenderLayer;

public class AdjudicatorMimicRender extends MobEntityRenderer<AdjudicatorMimicEntity, AdjudicatorEntityModel<AdjudicatorMimicEntity>>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/adjudicator.png");

	public AdjudicatorMimicRender(EntityRenderDispatcher rd)
	{
		super(rd, new AdjudicatorEntityModel(), 0);
		addFeature(new AdjudicatorMimicEyesRenderLayer(this));
	}

	@Override
	public Identifier getTexture(AdjudicatorMimicEntity entity)
	{
		return TEXTURE;
	}
}
