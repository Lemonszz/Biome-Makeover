package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class WitchHatModel<T extends Entity> extends EntityModel<T> implements ModelWithHead, ModelWithHat
{
	protected ModelPart hatBrim;
	protected ModelPart head;

	public WitchHatModel()
	{
		textureWidth = 64;
		textureHeight = 128;

		this.head = (new ModelPart(this)).setTextureSize(64, 128);
		this.head.setPivot(0.0F, 0.0F, 0.0F);
		this.head.setTextureOffset(0, 0).addCuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0);
		this.hatBrim = (new ModelPart(this)).setTextureSize(64, 128);
		this.hatBrim.setPivot(-5.0F, -10.03125F, -5.0F);
		this.hatBrim.setTextureOffset(0, 64).addCuboid(0.0F, 2.0F, 0.0F, 10.0F, 2.0F, 10.0F);
		this.head.addChild(this.hatBrim);

		ModelPart modelPart = (new ModelPart(this)).setTextureSize(64, 128);
		modelPart.setPivot(1.75F, -4.0F, 2.0F);
		modelPart.setTextureOffset(0, 76).addCuboid(0.0F, 2.0F, 0.0F, 7.0F, 4.0F, 7.0F);
		modelPart.pitch = -0.05235988F;
		modelPart.roll = 0.02617994F;
		this.hatBrim.addChild(modelPart);
		ModelPart modelPart2 = (new ModelPart(this)).setTextureSize(64, 128);
		modelPart2.setPivot(1.75F, -4.0F, 2.0F);
		modelPart2.setTextureOffset(0, 87).addCuboid(0.0F, 2.0F, 0.0F, 4.0F, 4.0F, 4.0F);
		modelPart2.pitch = -0.10471976F;
		modelPart2.roll = 0.05235988F;
		modelPart.addChild(modelPart2);
		ModelPart modelPart3 = (new ModelPart(this)).setTextureSize(64, 128);
		modelPart3.setPivot(1.75F, -2.0F, 2.0F);
		modelPart3.setTextureOffset(0, 95).addCuboid(0.0F, 2.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.25F);
		modelPart3.pitch = -0.20943952F;
		modelPart3.roll = 0.10471976F;
		modelPart2.addChild(modelPart3);
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		head.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{

	}

	@Override
	public ModelPart getHead()
	{
		return head;
	}

	@Override
	public void setHatVisible(boolean visible)
	{
		head.visible = true;
	}
}