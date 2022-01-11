package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.GhostEntity;

public class GhostModel extends EntityModel<GhostEntity> implements HeadedModel, ArmedModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("ghost"), "main");
    private final ModelPart body;
    private final ModelPart head;

    private final ModelPart leftArm, rightArm, bodylower, bodylower2, bodylower3;

    public GhostModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = root.getChild("head");

        this.leftArm = root.getChild("body").getChild("leftarm");
        this.rightArm = root.getChild("body").getChild("rightarm");
        this.bodylower = root.getChild("body").getChild("bodylower");
        this.bodylower2 = root.getChild("body").getChild("bodylower").getChild("bodylower2");
        this.bodylower3 = root.getChild("body").getChild("bodylower").getChild("bodylower2").getChild("bodylower3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19).mirror().addBox(-4.0F, 0.0F, -3.0F, 8.0F, 15.0F, 6.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition rightarm = body.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(28, 28).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, -0.6545F, 0.0F, 0.0F));

        PartDefinition leftarm = body.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, -0.829F, 0.0F, 0.0F));

        PartDefinition bodylower = body.addOrReplaceChild("bodylower", CubeListBuilder.create().texOffs(25, 11).addBox(-3.5F, -1.1194F, -2.3649F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.1194F, -0.6351F, 0.3927F, 0.0F, 0.0F));

        PartDefinition bodylower2 = bodylower.addOrReplaceChild("bodylower2", CubeListBuilder.create().texOffs(32, 0).addBox(-2.5F, 2.6806F, -2.8649F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition bodylower3 = bodylower2.addOrReplaceChild("bodylower3", CubeListBuilder.create().texOffs(16, 40).addBox(-0.5F, 2.3745F, -2.8739F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.0061F, -0.491F, 0.3927F, 0.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -10.0F, -6.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.0F, -2.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -2.0F, -2.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(GhostEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        setRotationAngle(body, 0.1745F, 0.0F, 0.0F);
        setRotationAngle(rightArm, -0.829F, 0.0F, 0.0F);
        setRotationAngle(leftArm, -0.829F, 0.0F, 0.0F);
        setRotationAngle(bodylower, 0.3927F, 0.0F, 0.0F);
        setRotationAngle(bodylower2, 0.3927F, 0.0F, 0.0F);
        setRotationAngle(bodylower3, 0.3927F, 0.0F, 0.0F);

        float pi = (float) Math.PI;
        this.head.xRot = -0.2618F + (headPitch * 0.0175F);
        this.head.yRot = netHeadYaw * 0.0175F;

        this.rightArm.xRot = -0.85F + (Mth.cos(limbSwing * 0.6662F + pi) * 0.5F * limbSwingAmount * 0.5F);
        this.leftArm.xRot = -0.85F + (Mth.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount * 0.5F);

        this.bodylower3.xRot = 0.3927F + Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.bodylower2.zRot = 0.3927F + Mth.cos(limbSwing * 0.6662F) * 0.25F * limbSwingAmount * 0.5F;
        this.bodylower.xRot = 0.3927F + Mth.cos(limbSwing * 0.6662F + pi) * 0.1F * limbSwingAmount * 0.5F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, buffer, packedLight, packedOverlay);
        head.render(poseStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart part, float x, float y, float z)
    {
        part.xRot = x;
        part.yRot = y;
        part.zRot = z;
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
        if(arm == HumanoidArm.RIGHT) rightArm.translateAndRotate(poseStack);
        else leftArm.translateAndRotate(poseStack);
    }

    @Override
    public ModelPart getHead() {
        return head;
    }
}