package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DragonflyEntity;
import party.lemons.biomemakeover.entity.TadpoleEntity;

public class TadpoleRender extends MobEntityRenderer<TadpoleEntity, TadpoleEntityModel>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/tadpole.png");

	public TadpoleRender(EntityRenderDispatcher rd)
	{
		super(rd, new TadpoleEntityModel(), 0.15F);
	}

	protected void setupTransforms(TadpoleEntity tadpole, MatrixStack matrixStack, float f, float g, float h) {
		super.setupTransforms(tadpole, matrixStack, f, g, h);
		float i = 4.3F * MathHelper.sin(0.6F * f);
		matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(i));
		if (!tadpole.isTouchingWater()) {
			matrixStack.translate(0.10000000149011612D, 0.10000000149011612D, -0.10000000149011612D);
			matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
		}
	}

	@Override
	public Identifier getTexture(TadpoleEntity entity)
	{
		return TEXTURE;
	}
}
