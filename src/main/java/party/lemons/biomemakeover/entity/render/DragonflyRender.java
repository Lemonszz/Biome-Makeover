package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DragonflyEntity;
import party.lemons.biomemakeover.entity.ToadEntity;

public class DragonflyRender extends MobEntityRenderer<DragonflyEntity, DragonflyEntityModel>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/dragonfly.png");

	public DragonflyRender(EntityRenderDispatcher rd)
	{
		super(rd, new DragonflyEntityModel(), 0.25F);
	}
	
	@Override
	public Identifier getTexture(DragonflyEntity entity)
	{
		return TEXTURE;
	}
}
