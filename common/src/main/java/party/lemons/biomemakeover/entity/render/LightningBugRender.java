package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.LightningBugEntity;
import party.lemons.biomemakeover.init.LightningBugModel;
import party.lemons.biomemakeover.util.MathUtils;

public class LightningBugRender extends MobRenderer<LightningBugEntity, LightningBugModel> {

    public static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/lightning_bug.png");

    public LightningBugRender(EntityRendererProvider.Context context) {
        super(context, new LightningBugModel(context.bakeLayer(LightningBugModel.LAYER_LOCATION)), 0.25F);

        addLayer(new LightningBugRenderLayer(this, new LightningBugModel.LightningBugInner(context.bakeLayer(LightningBugModel.LAYER_LOCATION_INNER)), true));
        addLayer(new LightningBugRenderLayer(this, new LightningBugModel.LightningBugOuter(context.bakeLayer(LightningBugModel.LAYER_LOCATION_OUTER)), false));
    }

    @Nullable
    @Override
    protected RenderType getRenderType(LightningBugEntity livingEntity, boolean bl, boolean bl2, boolean bl3) {
        return null;
    }

    @Override
    protected void scale(LightningBugEntity entity, PoseStack poseStack, float amount) {
        super.scale(entity, poseStack, amount);
        entity.scale += amount / 10;
        if(entity.scale > 99999) entity.scale = 0;

        float sc = 0.9F + ((float) Math.sin(entity.scale) / 5F);
        shadowRadius = sc / 10F;
        poseStack.scale(sc, sc, sc);
    }

    @Override
    protected int getBlockLightLevel(LightningBugEntity entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(LightningBugEntity entity) {
        return TEXTURE;
    }

    private static class LightningBugRenderLayer extends RenderLayer<LightningBugEntity, LightningBugModel>
    {
        private final EntityModel<LightningBugEntity> model;
        private final boolean color;

        public LightningBugRenderLayer(LightningBugRender lightningBugRender, EntityModel<LightningBugEntity> model, boolean color)
        {
            super(lightningBugRender);
            this.model = model;
            this.color = color;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LightningBugEntity entity, float f, float g, float delta, float j, float k, float l) {
            if(!entity.isInvisible())
            {
                Vector3f color = this.color ? getColor(entity, delta) : new Vector3f(1, 1, 1);
                VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));

                model.renderToBuffer(poseStack, vertexConsumer, i, LivingEntityRenderer.getOverlayCoords(entity, 0), color.x(), color.y(), color.z(), 1.0F);
            }
        }

        public Vector3f getColor(LightningBugEntity entity, float delta)
        {
            BlockPos pos = entity.getOnPos();
            int redHash = pos.hashCode();
            int greenHash = (pos.getX() + pos.getY() * 31) * 31 + pos.getZ();
            int blueHash = (pos.getZ() + pos.getX() * 31) * 31 + pos.getY();
            float drawRed = 1F, drawGreen = 1F, drawBlue = 1F;

            float rTarget = (redHash % 255) / 255F;
            float bTarget = (greenHash % 255) / 255F;
            float gTarget = (blueHash % 255) / 255F;

            if(entity.prevRed == -1)
            {
                drawRed = rTarget;
                drawGreen = gTarget;
                drawBlue = bTarget;
            }else
            {
                drawRed = MathUtils.approachValue(entity.prevRed, rTarget, 0.025F * delta);
                drawGreen = MathUtils.approachValue(entity.prevGreen, gTarget, 0.025F * delta);
                drawBlue = MathUtils.approachValue(entity.prevBlue, bTarget, 0.025F * delta);
            }
            entity.prevRed = drawRed;
            entity.prevGreen = drawGreen;
            entity.prevBlue = drawBlue;
            return new Vector3f(drawRed, Mth.abs(drawGreen), drawBlue);
        }
    }
}
