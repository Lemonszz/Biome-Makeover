package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
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
	public void render(ScuttlerEntity scuttler, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i)
	{
		shadowRadius = 0.25F;
		matrixStack.push();
		if(scuttler.isBaby())
		{
			matrixStack.scale(0.5F, 0.5F, 0.5F);
			shadowRadius = 0.1F;
		}

		super.render(scuttler, f, g, matrixStack, vertexConsumerProvider, i);

		matrixStack.pop();
	}


	@Override
	public Identifier getTexture(ScuttlerEntity entity)
	{
		return TEXTURE;
	}
}
