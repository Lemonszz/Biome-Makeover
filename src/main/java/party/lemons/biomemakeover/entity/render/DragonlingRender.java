package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DragonlingEntity;

public class DragonlingRender extends MobEntityRenderer<DragonlingEntity, DragonlingModel>
{
	private static final Identifier TEXTURE = new Identifier(BiomeMakeover.MODID, "textures/entity/dragonling.png");

	public DragonlingRender(EntityRenderDispatcher entityRenderDispatcher)
	{
		super(entityRenderDispatcher, new DragonlingModel(), 0.0F);
	}

	@Override
	public Identifier getTexture(DragonlingEntity entity)
	{
		return TEXTURE;
	}
}