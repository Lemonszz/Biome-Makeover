package party.lemons.biomemakeover.init;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.LightningBugEntity;

public class LightningBugModel extends EntityModel<LightningBugEntity>
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION_INNER = new ModelLayerLocation(BiomeMakeover.ID("lightning_bug_inner"), "main");
    public static final ModelLayerLocation LAYER_LOCATION_OUTER = new ModelLayerLocation(BiomeMakeover.ID("lightning_bug_outer"), "main");
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("lightning_bug"), "main");

    public LightningBugModel(ModelPart root) {
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(LightningBugEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
    }

    public static class LightningBugInner extends EntityModel<LightningBugEntity>
    {
        private final ModelPart main;

        public LightningBugInner(ModelPart root) {
            this.main = root.getChild("main");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition bb_main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 8).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

            return LayerDefinition.create(meshdefinition, 16, 16);
        }

        @Override
        public void setupAnim(LightningBugEntity entity, float f, float g, float h, float i, float j) {

        }

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            main.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    public static class LightningBugOuter extends EntityModel<LightningBugEntity>
    {
        private final ModelPart main;

        public LightningBugOuter(ModelPart root) {
            this.main = root.getChild("main");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition bb_main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create() .texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

            return LayerDefinition.create(meshdefinition, 16, 16);
        }

        @Override
        public void setupAnim(LightningBugEntity entity, float f, float g, float h, float i, float j) {

        }

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            main.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}