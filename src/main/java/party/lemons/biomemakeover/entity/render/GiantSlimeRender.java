package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.entity.DecayedEntity;
import party.lemons.biomemakeover.entity.render.feature.GiantSlimeDecayedFeatureRenderer;

public class GiantSlimeRender extends SlimeEntityRenderer
{
	public static DecayedEntity DUMMY_DECAYED;
	static{
		ClientTickEvents.END_WORLD_TICK.register((w)->{
			if(GiantSlimeRender.DUMMY_DECAYED != null)
			{
				GiantSlimeRender.DUMMY_DECAYED.age++;
				GiantSlimeRender.DUMMY_DECAYED.tick();
			}
		});
	}

	public GiantSlimeRender(EntityRenderDispatcher entityRenderDispatcher)
	{
		super(entityRenderDispatcher);
		this.features.add(0, new GiantSlimeDecayedFeatureRenderer(this));
	}

	public static void reverseScale(SlimeEntity slimeEntity, MatrixStack matrixStack, float f) {
		float g = 0.999F;
		matrixStack.scale(0.999F, 0.999F, 0.999F);
		matrixStack.translate(0.0D, 0.0010000000474974513D, 0.0D);
		float h = (float)slimeEntity.getSize();
		float i = MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch) / (h * 0.5F + 1.0F);
		float j = 1.0F / (i + 1.0F);
		matrixStack.scale(-(j * h), -(1.0F / j * h), -(j * h));
	}

	public void render(SlimeEntity slimeEntity, float yaw, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

		if(DUMMY_DECAYED == null && MinecraftClient.getInstance().world != null)
		{
			DUMMY_DECAYED = new DecayedEntity(MinecraftClient.getInstance().world);
			DUMMY_DECAYED.setDummy();
		}
		super.render(slimeEntity, yaw, delta, matrixStack, vertexConsumerProvider, i);
	}
}
