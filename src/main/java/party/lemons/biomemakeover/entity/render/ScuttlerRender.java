package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.ScuttlerEntity;

public class ScuttlerRender extends MobEntityRenderer<ScuttlerEntity, ScuttlerModel>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/scuttler.png");

	public ScuttlerRender(EntityRenderDispatcher rd)
	{
		super(rd, new ScuttlerModel(), 0.25F);
	}

	@Override
	public Identifier getTexture(ScuttlerEntity entity)
	{
		return TEXTURE;
	}
}
