package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.mob.SlimeEntity;
import party.lemons.biomemakeover.entity.DecayedEntity;

public class GiantSlimeRender extends SlimeEntityRenderer
{
	public static DecayedEntity DUMMY_DECAYED;

	public GiantSlimeRender(EntityRenderDispatcher entityRenderDispatcher)
	{
		super(entityRenderDispatcher);
	}

	public void render(SlimeEntity slimeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

		if(DUMMY_DECAYED == null && MinecraftClient.getInstance().world != null)
		{
			DUMMY_DECAYED = new DecayedEntity(MinecraftClient.getInstance().world);
			DUMMY_DECAYED.setDummy();
		}

		if(DUMMY_DECAYED != null)
		{
			DUMMY_DECAYED.setPose(EntityPose.SWIMMING);

			MinecraftClient.getInstance().getEntityRenderDispatcher().render(DUMMY_DECAYED, 0,2,1.245F, f, g, matrixStack, vertexConsumerProvider, i);
		}
		super.render(slimeEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}
}
