package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BatModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.BlightBatEntity;
import party.lemons.biomemakeover.init.BMBlocks;

public class BlightBatRender extends MobRenderer<BlightBatEntity, BlightBatRender.BlightBatModel> {

    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/glowbat.png");

    public BlightBatRender(EntityRendererProvider.Context context) {
        super(context, new BlightBatModel(context.bakeLayer(BlightBatModel.LAYER_LOCATION)), 0.25F);

        addLayer(new BlightBatLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(BlightBatEntity entity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(BlightBatEntity entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    protected void scale(BlightBatEntity livingEntity, PoseStack poseStack, float f) {
        poseStack.scale(0.35F, 0.35F, 0.35F);
    }

    @Override
    protected void setupRotations(BlightBatEntity bat, PoseStack poseStack, float f, float g, float h) {
        if (bat.isResting()) {
            poseStack.translate(0.0, -0.1f, 0.0);
        } else {
            poseStack.translate(0.0, Mth.cos(f * 0.3f) * 0.1f, 0.0);
        }
        super.setupRotations(bat, poseStack, f, g, h);
    }

    public static class BlightBatModel extends HierarchicalModel<BlightBatEntity>
    {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("blightbat"), "main");

        private final ModelPart root;
        private final ModelPart head;
        private final ModelPart body;
        private final ModelPart rightWing;
        private final ModelPart leftWing;
        private final ModelPart rightWingTip;
        private final ModelPart leftWingTip;

        public BlightBatModel(ModelPart modelPart) {
            this.root = modelPart;
            this.head = modelPart.getChild("head");
            this.body = modelPart.getChild("body");
            this.rightWing = this.body.getChild("right_wing");
            this.rightWingTip = this.rightWing.getChild("right_wing_tip");
            this.leftWing = this.body.getChild("left_wing");
            this.leftWingTip = this.leftWing.getChild("left_wing_tip");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshDefinition = new MeshDefinition();
            PartDefinition partDefinition = meshDefinition.getRoot();
            PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0f, -3.0f, -3.0f, 6.0f, 6.0f, 6.0f), PartPose.ZERO);
            partDefinition2.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(24, 0).addBox(-4.0f, -6.0f, -2.0f, 3.0f, 4.0f, 1.0f), PartPose.ZERO);
            partDefinition2.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(24, 0).mirror().addBox(1.0f, -6.0f, -2.0f, 3.0f, 4.0f, 1.0f), PartPose.ZERO);
            PartDefinition partDefinition3 = partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0f, 4.0f, -3.0f, 6.0f, 12.0f, 6.0f).texOffs(0, 34).addBox(-5.0f, 16.0f, 0.0f, 10.0f, 6.0f, 1.0f), PartPose.ZERO);
            PartDefinition partDefinition4 = partDefinition3.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(42, 0).addBox(-12.0f, 1.0f, 1.5f, 10.0f, 16.0f, 1.0f), PartPose.ZERO);
            partDefinition4.addOrReplaceChild("right_wing_tip", CubeListBuilder.create().texOffs(24, 16).addBox(-8.0f, 1.0f, 0.0f, 8.0f, 12.0f, 1.0f), PartPose.offset(-12.0f, 1.0f, 1.5f));
            PartDefinition partDefinition5 = partDefinition3.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(42, 0).mirror().addBox(2.0f, 1.0f, 1.5f, 10.0f, 16.0f, 1.0f), PartPose.ZERO);
            partDefinition5.addOrReplaceChild("left_wing_tip", CubeListBuilder.create().texOffs(24, 16).mirror().addBox(0.0f, 1.0f, 0.0f, 8.0f, 12.0f, 1.0f), PartPose.offset(12.0f, 1.0f, 1.5f));
            return LayerDefinition.create(meshDefinition, 64, 64);
        }

        @Override
        public ModelPart root() {
            return this.root;
        }

        @Override
        public void setupAnim(BlightBatEntity bat, float f, float g, float h, float i, float j) {
            if (bat.isResting()) {
                this.head.xRot = j * ((float)Math.PI / 180);
                this.head.yRot = (float)Math.PI - i * ((float)Math.PI / 180);
                this.head.zRot = (float)Math.PI;
                this.head.setPos(0.0f, -2.0f, 0.0f);
                this.rightWing.setPos(-3.0f, 0.0f, 3.0f);
                this.leftWing.setPos(3.0f, 0.0f, 3.0f);
                this.body.xRot = (float)Math.PI;
                this.rightWing.xRot = -0.15707964f;
                this.rightWing.yRot = -1.2566371f;
                this.rightWingTip.yRot = -1.7278761f;
                this.leftWing.xRot = this.rightWing.xRot;
                this.leftWing.yRot = -this.rightWing.yRot;
                this.leftWingTip.yRot = -this.rightWingTip.yRot;
            } else {
                this.head.xRot = j * ((float)Math.PI / 180);
                this.head.yRot = i * ((float)Math.PI / 180);
                this.head.zRot = 0.0f;
                this.head.setPos(0.0f, 0.0f, 0.0f);
                this.rightWing.setPos(0.0f, 0.0f, 0.0f);
                this.leftWing.setPos(0.0f, 0.0f, 0.0f);
                this.body.xRot = 0.7853982f + Mth.cos(h * 0.1f) * 0.15f;
                this.body.yRot = 0.0f;
                this.rightWing.yRot = Mth.cos(h * 74.48451f * ((float)Math.PI / 180)) * (float)Math.PI * 0.25f;
                this.leftWing.yRot = -this.rightWing.yRot;
                this.rightWingTip.yRot = this.rightWing.yRot * 0.5f;
                this.leftWingTip.yRot = -this.rightWing.yRot * 0.5f;
            }
        }
    }

    private static class BlightBatLayer extends net.minecraft.client.renderer.entity.layers.RenderLayer<BlightBatEntity, BlightBatModel> {
        public BlightBatLayer(RenderLayerParent<BlightBatEntity, BlightBatModel> renderLayerParent) {
            super(renderLayerParent);
        }

        @Override
        public void render(PoseStack pose, MultiBufferSource multiBufferSource, int i, BlightBatEntity e, float f, float g, float h, float j, float k, float l) {
            if(!e.isBaby() && !e.isInvisible())
            {
                BlockRenderDispatcher blockRenderManager = Minecraft.getInstance().getBlockRenderer();
                BlockState shroom = BMBlocks.PURPLE_GLOWSHROOM.defaultBlockState();
                int m = LivingEntityRenderer.getOverlayCoords(e, 0.0F);

                pose.pushPose();
                getParentModel().root().getChild("head").translateAndRotate(pose);
                pose.translate(0.0D, -0.7, 0);
                pose.mulPose(Vector3f.YP.rotationDegrees(-78.0F));
                pose.scale(-1.0F, -1.0F, 1.0F);
                pose.translate(-0.5D, -0.5D, -0.5D);
                blockRenderManager.renderSingleBlock(shroom, pose, multiBufferSource, i, m);
                pose.popPose();
            }
        }
    }
}
