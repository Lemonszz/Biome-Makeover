package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.MothEntity;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.sound.EntityLoopSoundInstance;

public class MothEntityRender extends MobEntityRenderer<MothEntity, MothEntityModel>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/moth.png");

	public MothEntityRender(EntityRenderDispatcher rd)
	{
		super(rd, new MothEntityModel(), 0.25F);
	}

	@Override
	public void render(MothEntity moth, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i)
	{
		matrixStack.push();
		matrixStack.translate(0, 0.25F, 0);
		super.render(moth, f, g, matrixStack, vertexConsumerProvider, i);
		matrixStack.pop();
	}


	@Override
	public Identifier getTexture(MothEntity entity)
	{
		if(!entity.hasPlayedLoop)
		{
			entity.hasPlayedLoop = true;
			MinecraftClient.getInstance().getSoundManager().playNextTick(new EntityLoopSoundInstance(entity, BMEffects.MOTH_FLAP));
		}

		return TEXTURE;
	}
}
