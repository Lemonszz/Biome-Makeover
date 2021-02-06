package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.CowboyEntity;
import party.lemons.biomemakeover.entity.render.feature.HatFeatureRenderer;

public class CowboyRender extends IllagerEntityRenderer<CowboyEntity>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/cowboy.png");

	public CowboyRender(EntityRenderDispatcher entityRenderDispatcher)
	{
		super(entityRenderDispatcher, new IllagerEntityModel(0.0F, 0.0F, 64, 64), 0.5F);
		this.addFeature(new HeldItemFeatureRenderer(this));
		this.addFeature(new HatFeatureRenderer<>(this));
	}

	public Identifier getTexture(CowboyEntity pillagerEntity)
	{
		return TEXTURE;
	}
}
