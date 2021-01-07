package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.entity.LightningEntity;
import party.lemons.biomemakeover.entity.LightningBugEntity;

public class LightningBugOuterEntityModel extends CompositeEntityModel<LightningBugEntity>
{
	private final ModelPart body;

	public LightningBugOuterEntityModel()
	{
		this.textureHeight = 16;
		this.textureWidth = 16;

		body = new ModelPart(this);
		body.setPivot(0.0F, 20.0F, 0.0F);
		body.setTextureOffset(0, 0).addCuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
	}

	@Override
	public Iterable<ModelPart> getParts()
	{
		return ImmutableList.of(body);
	}

	@Override
	public void setAngles(LightningBugEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{

	}
}
