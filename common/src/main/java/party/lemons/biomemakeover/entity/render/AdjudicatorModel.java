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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorStateProvider;
import party.lemons.biomemakeover.util.AnimationHelper;

public class AdjudicatorModel<T extends Monster & AdjudicatorStateProvider> extends EntityModel<T> implements HeadedModel, ArmedModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("adjudicator"), "main");

    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart arm_left;
    private final ModelPart arm_right;
    private final ModelPart leg_left;
    private final ModelPart leg_right;

    public AdjudicatorModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = root.getChild("head");

        arm_left = body.getChild("arm_left");
        arm_right = body.getChild("arm_right");
        leg_left = body.getChild("leg_left");
        leg_right = body.getChild("leg_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -11.25F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.25F, 0.0F));

        PartDefinition robe = body.addOrReplaceChild("robe", CubeListBuilder.create().texOffs(28, 28).addBox(-4.0F, -0.5F, -3.0F, 8.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.25F, 0.0F));

        PartDefinition arm_left = body.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(14, 38).addBox(-1.2F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(32, 16).addBox(-1.5F, -3.0F, -2.5F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, -9.25F, 0.5F));

        PartDefinition arm_right = body.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(26, 43).addBox(-1.8F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(32, 16).addBox(-2.5F, -3.0F, -2.5F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, -9.25F, 0.5F));

        PartDefinition leg_left = body.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(32, 0).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.75F, 0.0F));

        PartDefinition leg_right = body.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(0, 38).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 1.75F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(38, 44).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, -4.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadyRot, float headxRot) {
        AnimationHelper.setRotation(leg_left, 0, 0, 0);
        AnimationHelper.setRotation(leg_right, 0, 0, 0);
        AnimationHelper.rotateHead(head, headxRot, netHeadyRot);

        this.body.yRot = 0;

        AnimationHelper.swingLimb(leg_left, leg_right, limbSwing, limbSwingAmount, 1.25F);

        this.arm_right.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 2.0F * limbSwingAmount * 0.5F;
        this.arm_right.yRot= 0.0F;
        this.arm_right.zRot = 0.0F;
        this.arm_left.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.arm_left.yRot = 0.0F;
        this.arm_left.zRot = 0.0F;

        if (this.riding)
        {
            arm_right.xRot += -0.63F;
            arm_left.xRot += -0.63F;
            this.leg_right.xRot = -1.4F;
            this.leg_right.yRot = 0.31F;
            this.leg_right.zRot = 0.078F;
            this.leg_left.xRot = -1.41F;
            this.leg_left.yRot = -0.31F;
            this.leg_left.zRot = -0.078F;
        }

        switch(entity.getState())
        {
            case WAITING:
                AnimationHelper.setRotation(head, -0.567232F, 0, 0);
                AnimationHelper.setRotation(arm_left, -0.349066F, 0, -2.53073F);
                AnimationHelper.setRotation(arm_right, -0.349066F, 0, 2.53073F);
                AnimationHelper.setRotation(leg_left, -1.701696F, 0.4799655F, 0);
                AnimationHelper.setRotation(leg_right, -1.309F, -0.0872665F, 0);
                break;
            case SUMMONING:
            case TELEPORT:
                this.arm_right.xRot = Mth.cos(limbSwingAmount * 0.6662F) * 0.25F;
                this.arm_left.xRot = Mth.cos(limbSwingAmount * 0.6662F) * 0.25F;
                this.arm_right.zRot = 2.3561945F;
                this.arm_left.zRot = -2.3561945F;
                this.arm_right.yRot = 0.0F;
                this.arm_left.yRot = 0.0F;
                break;
            case FIGHTING:
                if(!entity.getMainHandItem().isEmpty())
                {
                    Item item = entity.getMainHandItem().getItem();
                    if(item instanceof BowItem)
                    {
                        this.arm_right.yRot = -0.1F + this.head.yRot;
                        this.arm_right.xRot = -1.5707964F + this.head.xRot;
                        this.arm_left.xRot = -0.9424779F + this.head.xRot;
                        this.arm_left.yRot = this.head.yRot - 0.4F;
                        this.arm_left.zRot = 1.5707964F;
                    }
                    else if(item instanceof AxeItem || item instanceof SwordItem)
                    {
                        AnimationUtils.swingWeaponDown(this.arm_right, this.arm_left, entity, this.attackTime, limbSwingAmount);
                    }
                }
                else
                {
                    AnimationUtils.animateZombieArms(this.arm_left, this.arm_right, true, this.attackTime, limbSwingAmount);
                }

                break;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, buffer, packedLight, packedOverlay);
        head.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        this.getAttackingArm(humanoidArm).translateAndRotate(poseStack);
    }

    private ModelPart getAttackingArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.arm_left : this.arm_right;
    }

    @Override
    public ModelPart getHead() {
        return head;
    }
}