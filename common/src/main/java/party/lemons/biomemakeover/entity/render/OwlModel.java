package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.OwlEntity;

import java.util.List;

public class OwlModel extends AgeableListModel<OwlEntity> implements HeadedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("owl"), "main");
    private final ModelPart chest;
    private final ModelPart head;

    private final ModelPart wing_left;
    private final ModelPart wing_left_connection;
    private final ModelPart wing_tip_left;
    private final ModelPart wing_right;
    private final ModelPart wing_right_connection;
    private final ModelPart wing_tip_right;
    private final ModelPart leg_right;
    private final ModelPart leg_left;
    private final ModelPart foot_right;
    private final ModelPart foot_left;

    public OwlModel(ModelPart root) {
        super(true, 14, 0, 2.0F, 2.0F, 24.0F);

        this.chest = root.getChild("chest");
        this.head = root.getChild("head_connection");

        wing_left_connection = chest.getChild("wing_left_connection");
        wing_right_connection = chest.getChild("wing_right_connection");

        wing_left = wing_left_connection.getChild("wing_left");
        wing_right = wing_right_connection.getChild("wing_right");

        wing_tip_left = wing_left.getChild("wing_tip_left");
        wing_tip_right = wing_right.getChild("wing_tip_right");

        leg_left = chest.getChild("leg_left");
        leg_right = chest.getChild("leg_right");

        foot_right = leg_right.getChild("foot_right");
        foot_left = leg_left.getChild("foot_left");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition chest = partdefinition.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -5.575F, -3.3125F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.975F, 0.3125F));

        PartDefinition chest_lower_r1 = chest.addOrReplaceChild("chest_lower_r1", CubeListBuilder.create().texOffs(21, 21).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.425F, 0.6875F, 0.2182F, 0.0F, 0.0F));

        PartDefinition wing_left_connection = chest.addOrReplaceChild("wing_left_connection", CubeListBuilder.create(), PartPose.offset(3.0F, -5.575F, 0.6875F));

        PartDefinition wing_left = wing_left_connection.addOrReplaceChild("wing_left", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -3.0F));

        PartDefinition wing_upper_r1 = wing_left.addOrReplaceChild("wing_upper_r1", CubeListBuilder.create().texOffs(0, 28).addBox(0.0F, -1.0F, -2.0F, 2.0F, 11.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition wing_tip_left = wing_left.addOrReplaceChild("wing_tip_left", CubeListBuilder.create(), PartPose.offset(0.5F, 9.0F, 7.0F));

        PartDefinition wing_tip_r1 = wing_tip_left.addOrReplaceChild("wing_tip_r1", CubeListBuilder.create().texOffs(18, 34).addBox(0.0F, -1.0F, -2.0F, 1.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -4.0F, -2.0F, 0.5672F, 0.0F, 0.0F));

        PartDefinition wing_right_connection = chest.addOrReplaceChild("wing_right_connection", CubeListBuilder.create(), PartPose.offset(-3.0F, -5.575F, 0.6875F));

        PartDefinition wing_right = wing_right_connection.addOrReplaceChild("wing_right", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -3.0F));

        PartDefinition wing_upper_r2 = wing_right.addOrReplaceChild("wing_upper_r2", CubeListBuilder.create().texOffs(28, 0).addBox(-2.0F, -1.0F, -2.0F, 2.0F, 11.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition wing_tip_right = wing_right.addOrReplaceChild("wing_tip_right", CubeListBuilder.create(), PartPose.offset(-0.5F, 9.0F, 7.0F));

        PartDefinition wing_tip_r2 = wing_tip_right.addOrReplaceChild("wing_tip_r2", CubeListBuilder.create().texOffs(28, 34).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -4.0F, -2.0F, 0.5672F, 0.0F, 0.0F));

        PartDefinition leg_right = chest.addOrReplaceChild("leg_right", CubeListBuilder.create(), PartPose.offset(-1.5F, 6.925F, 1.6875F));

        PartDefinition thigh_right_r1 = leg_right.addOrReplaceChild("thigh_right_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition foot_right = leg_right.addOrReplaceChild("foot_right", CubeListBuilder.create().texOffs(16, 4).addBox(-1.5F, -0.5F, -3.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 0.0F));

        PartDefinition leg_left = chest.addOrReplaceChild("leg_left", CubeListBuilder.create(), PartPose.offset(1.5F, 6.925F, 1.6875F));

        PartDefinition thigh_left_r1 = leg_left.addOrReplaceChild("thigh_left_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition foot_left = leg_left.addOrReplaceChild("foot_left", CubeListBuilder.create().texOffs(16, 0).addBox(-1.5F, -0.5F, -1.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, -2.0F));

        PartDefinition tail = chest.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 5.925F, 5.1875F, 0.3054F, 0.0F, 0.0F));

        PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(38, 38).addBox(-3.0F, -0.5F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition head_connection = partdefinition.addOrReplaceChild("head_connection", CubeListBuilder.create(), PartPose.offset(0.0F, 6.4F, 1.0F));

        PartDefinition head = head_connection.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -6.0F, -3.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 5).addBox(-0.6F, -2.0F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition beak_lower = head.addOrReplaceChild("beak_lower", CubeListBuilder.create().texOffs(4, 5).addBox(-2.1F, -14.5F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 13.5F, 1.0F));

        PartDefinition brow_left = head.addOrReplaceChild("brow_left", CubeListBuilder.create().texOffs(11, 28).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -4.5F, -3.5F));

        PartDefinition brow_right = head.addOrReplaceChild("brow_right", CubeListBuilder.create().texOffs(11, 30).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -4.5F, -3.5F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(OwlEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float pi = (float) Math.PI;

        if(entity.getStandingState() != OwlEntity.StandingState.FLYING)
        {
            this.head.xRot = -0.2618F + (headPitch * 0.0175F);
            this.head.yRot = netHeadYaw * 0.0175F;

            this.wing_tip_left.zRot = 0;
            this.wing_tip_right.zRot = 0;


            this.leg_right.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.leg_left.xRot = Mth.cos(limbSwing * 0.6662F + pi) * 1.4F * limbSwingAmount;
            this.wing_right_connection.yRot = Mth.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount;
            this.wing_left_connection.yRot = Mth.cos(limbSwing * 0.6662F + pi) * 1F * limbSwingAmount;

            if(entity.isInSittingPose())
            {
                leg_right.xRot = -1.5708F;
                leg_left.xRot = -1.5708F;
            }

        }else
        {
            this.wing_left_connection.yRot = Mth.cos(limbSwing / 2F) / 2F;
            this.wing_tip_left.zRot = -(Mth.sin(limbSwing / 2F) / 4F);

            this.wing_right_connection.yRot = -(Mth.cos(limbSwing / 2F) / 2F);
            this.wing_tip_right.zRot = (Mth.sin(limbSwing / 2F) / 4F);

            this.leg_right.xRot = Mth.cos(limbSwing * 0.6662F) * 1F * limbSwingAmount;
            this.leg_left.xRot = Mth.cos(limbSwing * 0.6662F + pi) * 1F * limbSwingAmount;
        }
    }

    @Override
    public void prepareMobModel(OwlEntity entity, float limbAngle, float limbDistance, float delta) {
        super.prepareMobModel(entity, limbAngle, limbDistance, delta);

        switch(entity.getStandingState())
        {
            case STANDING:

                break;
            case FLYING:

                break;
        }

        float rad90 = 1.5708F;
        float rad155 = 2.70526F;
        float rad70 = 1.22173F;
        float wingAngle = limbAngle;
        float rad40 = 0.698132F;

        float leanProgress = entity.getSwimAmount(delta) / 7F;
        setRotationAngle(wing_right, 0, rad70 * leanProgress, rad90 * leanProgress);
        setRotationAngle(wing_left, 0, -rad70 * leanProgress, -rad90 * leanProgress);
        setRotationAngle(wing_tip_left, -rad155 * leanProgress, 0, 0);
        setRotationAngle(wing_tip_right, -rad155 * leanProgress, 0, 0);
        setRotationAngle(foot_left, rad90 * leanProgress, 0, 0);
        setRotationAngle(foot_right, rad90 * leanProgress, 0, 0);

        setRotationAngle(head, -rad40 * leanProgress, 0, 0);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(chest);
    }

    public void setRotationAngle(ModelPart part, float x, float y, float z)
    {
        part.xRot = x;
        part.yRot = y;
        part.zRot = z;
    }

    @Override
    public ModelPart getHead() {
        return head;
    }
}