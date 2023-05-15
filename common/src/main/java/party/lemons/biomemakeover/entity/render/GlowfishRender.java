package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SalmonModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.SalmonRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMBlocks;

public class GlowfishRender extends SalmonRenderer {

    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/glow_fish.png");

    public GlowfishRender(EntityRendererProvider.Context context) {
        super(context);
        
        addLayer(new GlowfishLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Salmon salmon) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(Salmon entity, BlockPos blockPos) {
        return 15;
    }

    private static class GlowfishLayer extends RenderLayer<Salmon, SalmonModel<Salmon>> {
        public GlowfishLayer(GlowfishRender r) {
            super(r);
        }

        @Override
        public void render(PoseStack pose, MultiBufferSource multiBufferSource, int i, Salmon e, float f, float g, float h, float j, float k, float l)
        {
            if(!e.isBaby() && !e.isInvisible())
            {
                BlockRenderDispatcher blockRenderManager = Minecraft.getInstance().getBlockRenderer();
                BlockState shroom = BMBlocks.ORANGE_GLOWSHROOM.get().defaultBlockState();
                int m = LivingEntityRenderer.getOverlayCoords(e, 0.0F);

                pose.pushPose();
                getParentModel().root().getChild("body_back").translateAndRotate(pose);
                getParentModel().root().getChild("body_back").getChild("top_back_fin").visible = false;

                pose.translate(0.0D, 0, 0.5F);
                pose.mulPose(Axis.XP.rotationDegrees(-90));
                pose.scale(-0.75F, -0.75F, 0.75F);
                pose.translate(-0.5D, 0.0D, -0.5D);
                blockRenderManager.renderSingleBlock(shroom, pose, multiBufferSource, i, m);
                pose.popPose();
            }
        }
    }
}
