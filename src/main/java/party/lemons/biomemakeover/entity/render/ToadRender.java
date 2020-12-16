package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.ToadEntity;

public class ToadRender extends MobEntityRenderer<ToadEntity, ToadEntityModel>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/toad.png");

	public ToadRender(EntityRenderDispatcher rd)
	{
		super(rd, new ToadEntityModel(), 0.25F);
	}

	@Override
	public Identifier getTexture(ToadEntity entity)
	{
		return TEXTURE;
	}
}
