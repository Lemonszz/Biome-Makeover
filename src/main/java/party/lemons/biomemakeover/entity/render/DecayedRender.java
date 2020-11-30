package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.DrownedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.client.render.entity.feature.DrownedOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.DrownedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DecayedEntity;
import party.lemons.biomemakeover.entity.render.feature.DecayedOverlayFeatureRenderer;

public class DecayedRender extends ZombieBaseEntityRenderer<DecayedEntity, DrownedEntityModel<DecayedEntity>>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/decayed_inner_layer.png");

	public DecayedRender(EntityRenderDispatcher entityRenderDispatcher)
	{
		super(entityRenderDispatcher, new DrownedEntityModel(0.0F, 0.0F, 64, 64), new DrownedEntityModel(0.5F, true), new DrownedEntityModel(1.0F, true));
		this.addFeature(new DecayedOverlayFeatureRenderer<>(this));
	}

	public Identifier getTexture(ZombieEntity zombieEntity)
	{
		return TEXTURE;
	}

	protected void setupTransforms(DecayedEntity decayed, MatrixStack matrixStack, float f, float g, float h)
	{
		super.setupTransforms(decayed, matrixStack, f, g, h);
		float i = decayed.getLeaningPitch(h);
		if(i > 0.0F)
		{
			matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(MathHelper.lerp(i, decayed.pitch, -10.0F - decayed.pitch)));
		}

	}
}