package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.item.CrossbowItem;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.StoneGolemEntity;
import party.lemons.biomemakeover.util.AnimationHelper;

public class StoneGolemModel extends EntityModel<StoneGolemEntity> implements HeadedModel, ArmedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("stone_golem"), "main");
    private final ModelPart lower_base;
    private final ModelPart body;
    private final ModelPart arm_left;
    private final ModelPart arm_right;
    private final ModelPart head;

    public StoneGolemModel(ModelPart root) {
        this.lower_base = root.getChild("lower_base");
        this.body = root.getChild("body");
        arm_left = body.getChild("arm_left");
        arm_right = body.getChild("arm_right");
        head = body.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition lower_base = partdefinition.addOrReplaceChild("lower_base", CubeListBuilder.create().texOffs(0, 22).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -16.7F, -5.0F, 18.0F, 12.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(42, 22).addBox(-7.0F, -4.7F, -3.0F, 14.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.7F, 0.0F));

        PartDefinition arm_left = body.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(0, 44).addBox(0.5F, 1.5F, -3.0F, 4.0F, 26.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(40, 62).addBox(0.5F, -2.5F, -4.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(8.5F, -14.2F, 0.0F));

        PartDefinition arm_right = body.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(20, 44).addBox(-4.5F, 1.5F, -3.0F, 4.0F, 26.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(64, 33).addBox(-6.5F, -2.5F, -4.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.5F, -14.2F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(40, 44).addBox(-4.0F, -11.0F, -3.5F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(60, 62).addBox(-5.0F, -6.0F, -5.5F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(6, 22).addBox(-1.0F, -4.0F, -5.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(56, 16).addBox(-5.0F, -2.0F, -4.5F, 10.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.7F, -3.5F));

        PartDefinition horn = head.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(0, 22).addBox(-9.5F, -2.5F, -1.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-0.5F, -2.5F, -1.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -10.5F, -3.5F, 0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(StoneGolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadyRot, float headPitch) {
        AnimationHelper.rotateHead(head, headPitch, netHeadyRot);

        AbstractIllager.IllagerArmPose state = entity.getState();

        if(state == AbstractIllager.IllagerArmPose.NEUTRAL)
        {
            AnimationHelper.setRotation(arm_left, 0, 0,0 );
            AnimationHelper.setRotation(arm_right, 0, 0,0 );
        }
        else if(state == AbstractIllager.IllagerArmPose.ATTACKING)
        {
            if (entity.getMainHandItem().isEmpty()) {
                AnimationUtils.animateZombieArms(this.arm_left, this.arm_right, true, this.attackTime, limbSwingAmount);
            } else {
                AnimationUtils.swingWeaponDown(this.arm_right, this.arm_left, entity, this.attackTime, limbSwingAmount);
            }
        }
        else if (state == AbstractIllager.IllagerArmPose.CROSSBOW_HOLD)
        {
            hold(this.arm_right, this.arm_left, this.head, true);
        }
        else if (state == AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE)
        {
            charge(this.arm_right, this.arm_left, entity, true);
        }
    }

    public static void hold(ModelPart holdingArm, ModelPart otherArm, ModelPart head, boolean rightArmed) {
        ModelPart modelPart = rightArmed ? holdingArm : otherArm;
        ModelPart modelPart2 = rightArmed ? otherArm : holdingArm;
        modelPart.yRot = (rightArmed ? -0.3F : 0.3F) + head.yRot;
        modelPart2.yRot = (rightArmed ? 0.6F : -0.6F) + head.yRot;
        modelPart.xRot = -1.25F + head.xRot + 0.1F;
        modelPart2.xRot = -1.25F + head.xRot;
    }

    public static void charge(ModelPart holdingArm, ModelPart pullingArm, LivingEntity actor, boolean rightArmed) {
        ModelPart modelPart = rightArmed ? holdingArm : pullingArm;
        ModelPart pullArm = rightArmed ? pullingArm : holdingArm;
        modelPart.yRot = rightArmed ? -0.8F : 0.8F;
        modelPart.xRot = -0.97079635F;
        pullArm.xRot = modelPart.xRot;
        float f = (float) CrossbowItem.getChargeDuration(actor.getUseItem());
        float g = Mth.clamp((float)actor.getTicksUsingItem(), 0.0F, f);
        float h = g / f;
        pullArm.yRot = Mth.lerp(h, 0.2F, 0.4F) * (float)(rightArmed ? 1 : -1);
        pullArm.xRot = Mth.lerp(h, pullArm.xRot, -1);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, buffer, packedLight, packedOverlay);
    }

    public void renderBaseToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        lower_base.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
        if(arm == HumanoidArm.LEFT)
            arm_left.translateAndRotate(poseStack);
        else
            arm_right.translateAndRotate(poseStack);
    }

    @Override
    public ModelPart getHead() {
        return head;
    }
}