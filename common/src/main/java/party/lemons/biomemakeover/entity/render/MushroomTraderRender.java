package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.MushroomVillagerEntity;
import party.lemons.biomemakeover.init.BMBlocks;

public class MushroomTraderRender extends MobRenderer<MushroomVillagerEntity, VillagerModel<MushroomVillagerEntity>> {

    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/mushrooming_trader_inner.png");


    public MushroomTraderRender(EntityRendererProvider.Context context) {
        super(context, new VillagerModel<>(context.bakeLayer(ModelLayers.VILLAGER)), 0.5f);

        this.addLayer(new MushroomTraderOverlay(this, new VillagerModel<>(context.bakeLayer(ModelLayers.VILLAGER))));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet()));
        this.addLayer(new CrossedArmsItemLayer<>(this));
        this.addLayer(new MushroomTraderFeatures(this));
    }

    @Override
    public ResourceLocation getTextureLocation(MushroomVillagerEntity entity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(MushroomVillagerEntity entity, BlockPos blockPos) {
        return 15;
    }

    protected void scale(MushroomVillagerEntity wanderingTraderEntity, PoseStack matrixStack, float f)
    {
        float g = 0.9375F;
        matrixStack.scale(g, g, g);
    }

    private static class MushroomTraderOverlay extends RenderLayer<MushroomVillagerEntity, VillagerModel<MushroomVillagerEntity>>
    {
        private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/mushrooming_trader_outer.png");
        private final VillagerModel<MushroomVillagerEntity> model;


        public MushroomTraderOverlay(MushroomTraderRender mushroomTraderRender, VillagerModel<MushroomVillagerEntity> model) {
            super(mushroomTraderRender);

            this.model = model;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, MushroomVillagerEntity entity, float f, float g, float h, float j, float k, float l) {
            int light = getPackedLightCoords(entity, l);
            getParentModel().copyPropertiesTo(model);
            model.setupAnim(entity, f, g, j, k, l);

            renderColoredCutoutModel(model, TEXTURE, poseStack, multiBufferSource, light, entity, 1.0F, 1.0F,1.0F);
        }

        public final int getPackedLightCoords(MushroomVillagerEntity entity, float f) {
            BlockPos blockPos = new BlockPos(((Entity)entity).getLightProbePosition(f));
            return LightTexture.pack(this.getBlockLightLevel(entity, blockPos), this.getSkyLightLevel(entity, blockPos));
        }

        protected int getSkyLightLevel(MushroomVillagerEntity entity, BlockPos blockPos) {
            return entity.level.getBrightness(LightLayer.SKY, blockPos);
        }

        protected int getBlockLightLevel(MushroomVillagerEntity entity, BlockPos blockPos) {
            if (entity.isOnFire()) {
                return 15;
            }
            return entity.level.getBrightness(LightLayer.BLOCK, blockPos);
        }


        protected int getOverlayLight(MushroomVillagerEntity entity, BlockPos blockPos) {
            if (entity.isOnFire()) {
                return 15;
            }
            return entity.level.getBrightness(LightLayer.BLOCK, blockPos);
        }
    }

    private static class MushroomTraderFeatures extends RenderLayer<MushroomVillagerEntity, VillagerModel<MushroomVillagerEntity>> {
        public MushroomTraderFeatures(MushroomTraderRender mushroomTraderRender) {
            super(mushroomTraderRender);
        }

        @Override
        public void render(PoseStack pose, MultiBufferSource mbs, int i, MushroomVillagerEntity e, float f, float g, float h, float j, float k, float l) {
            if(!e.isBaby() && !e.isInvisible())
            {
                BlockRenderDispatcher blockRenderManager = Minecraft.getInstance().getBlockRenderer();
                BlockState shroom = BMBlocks.GREEN_GLOWSHROOM.get().defaultBlockState();
                int m = LivingEntityRenderer.getOverlayCoords(e, 0.0F);

                pose.pushPose();
                this.getParentModel().getHead().translateAndRotate(pose);
                pose.translate(0.0D, -1, 0);
                pose.mulPose(Vector3f.YP.rotationDegrees(-78.0F));
                pose.scale(-1.0F, -1.0F, 1.0F);
                pose.translate(-0.5D, -0.5D, -0.5D);
                blockRenderManager.renderSingleBlock(shroom, pose, mbs, i, m);
                pose.popPose();

                pose.pushPose();
                this.getParentModel().getHead().translateAndRotate(pose);
                pose.translate(-0.4D, -0.5, -0.5);
                pose.mulPose(Vector3f.XP.rotationDegrees(75));
                pose.mulPose(Vector3f.ZN.rotationDegrees(30));
                pose.scale(-1.0F, -1.0F, 1.0F);
                pose.translate(-0.5D, -0.5D, -0.5D);
                blockRenderManager.renderSingleBlock(shroom, pose, mbs, i, m);
                pose.popPose();
            }
        }
    }
}
