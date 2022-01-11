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
import net.minecraft.world.entity.HumanoidArm;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.RootlingEntity;
import party.lemons.biomemakeover.util.AnimationHelper;

public class RootlingModel extends EntityModel<RootlingEntity> implements HeadedModel, ArmedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("rootling"), "main");
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart arm_right;
    private final ModelPart arm_left;
    private final ModelPart leg_right;
    private final ModelPart leg_left;

    public RootlingModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.arm_left = body.getChild("arm_left");
        this.arm_right = body.getChild("arm_right");
        this.leg_left = body.getChild("leg_left");
        this.leg_right = body.getChild("leg_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -2.5F, -3.5F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-2.5F, -3.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(15, 12).addBox(-1.5F, 2.5F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 18.5F, -0.5F));

        PartDefinition arm_right = body.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, 0.0F, -1.5F, 1.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -2.5F, 0.0F));

        PartDefinition arm_left = body.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(8, 18).addBox(0.0F, 0.0F, -1.5F, 1.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -2.5F, 0.0F));

        PartDefinition leg_right = body.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 3.0F, 0.0F));

        PartDefinition leg_left = body.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(3, 3).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 3.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(17, 17).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(21, 0).addBox(-4.5F, -6.0F, -1.6F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 15.0F, -0.5F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 25).addBox(-5.5F, -6.0F, 0.1F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -7.0F, -1.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 25).addBox(-4.0607F, -8.0F, 0.0393F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(RootlingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        AnimationHelper.rotateHead(head, headPitch, netHeadYaw);
        AnimationHelper.swingLimb(arm_left, arm_right, limbSwing, limbSwingAmount, 1.4F);
        AnimationHelper.swingLimb(leg_left, leg_right, limbSwing, limbSwingAmount, 2F);

        body.zRot = (float) (Math.sin(limbSwing) / 4F) * limbSwingAmount;
        head.zRot = (float) (Math.cos(limbSwing) / 8F) * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, buffer, packedLight, packedOverlay);
        head.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public ModelPart getHead() {
        return head;
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        getArm(humanoidArm).translateAndRotate(poseStack);
    }

    protected ModelPart getArm(HumanoidArm arm)
    {
        return arm == HumanoidArm.LEFT ? this.arm_left : this.arm_right;
    }
}