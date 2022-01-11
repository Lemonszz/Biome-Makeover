package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.ModelUtils;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.MothEntity;
import party.lemons.biomemakeover.util.AnimationHelper;

public class MothModel extends EntityModel<MothEntity> implements HeadedModel
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("moth"), "main");
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart wing_right_joint;
    private final ModelPart wing_left_joint;

    private float bodyPitch;

    public MothModel(ModelPart root) {

        this.body = root.getChild("moth");
        this.head = body.getChild("head");
        this.wing_right_joint = body.getChild("wing_right").getChild("wing_right_joint");
        this.wing_left_joint = body.getChild("wing_left").getChild("wing_left_joint");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition moth = partdefinition.addOrReplaceChild("moth", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, -1.0F));

        PartDefinition cube_r1 = moth.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, -5.5F, -2.5F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 7.5F, 4.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r2 = moth.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.3827F, -0.0761F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r3 = moth.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 30).addBox(-0.5F, -3.5F, -4.5F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(32, 5).addBox(11.5F, -3.5F, -4.5F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(4, 43).addBox(-0.5F, -3.5F, 1.5F, 2.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 43).addBox(9.5F, -3.5F, 1.5F, 2.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 1.5F, -3.5F, 0.3927F, 0.0F, 0.0F));

        PartDefinition head = moth.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 24).addBox(-3.0F, -5.0F, -3.9F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -2.0F));

        PartDefinition HeadDecoration = head.addOrReplaceChild("HeadDecoration", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -19.0F, -11.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 3.0F));

        PartDefinition antenna_2_r1 = HeadDecoration.addOrReplaceChild("antenna_2_r1", CubeListBuilder.create().texOffs(24, 18).addBox(-1.5F, -4.5F, 0.0F, 3.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(3.5F, -4.5F, 0.0F, 3.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -23.3F, -4.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition wing_right = moth.addOrReplaceChild("wing_right", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -5.0F, 1.7F, 0.3927F, 0.0F, 0.0F));

        PartDefinition wing_right_joint = wing_right.addOrReplaceChild("wing_right_joint", CubeListBuilder.create().texOffs(32, 0).addBox(-11.0F, -8.0F, 0.6F, 11.0F, 11.0F, 0.01F, new CubeDeformation(0.0F))
                .texOffs(34, 36).addBox(-8.0F, 3.0F, 0.6F, 8.0F, 7.0F, 0.01F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition wing_left = moth.addOrReplaceChild("wing_left", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, -5.0F, 1.8F, 0.3927F, 0.0F, 0.0F));

        PartDefinition wing_left_joint = wing_left.addOrReplaceChild("wing_left_joint", CubeListBuilder.create().texOffs(42, 18).addBox(0.0F, 3.0F, 0.6F, 8.0F, 7.0F, 0.01F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(0.0F, -8.0F, 0.6F, 11.0F, 11.0F, 0.01F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    @Override
    public void prepareMobModel(MothEntity entity, float f, float g, float h) {
        super.prepareMobModel(entity, f, g, h);
        this.bodyPitch = entity.getSwimAmount(h);

    }

    @Override
    public void setupAnim(MothEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        AnimationHelper.rotateHead(head, headPitch, netHeadYaw);

        this.body.xRot = 0.0F;
        this.body.y = 19.0F;
        float animProgress = ageInTicks * 2.1F;

        this.wing_left_joint.yRot = (-1F + (Mth.cos(animProgress) * 3.1415927F * 0.1F));
        this.wing_right_joint.yRot = -this.wing_left_joint.yRot;

        this.body.xRot = 0.0F;
        this.body.yRot = 0.0F;
        this.body.zRot = 0.0F;

        if (!entity.isTargeting())
        {
            this.body.xRot = 0.0F;
            this.body.yRot = 0.0F;
            this.body.zRot = 0.0F;
            animProgress = Mth.cos(ageInTicks * 0.18F);
            this.body.xRot = 0.1F + animProgress * 3.1415927F * 0.025F;
            this.body.y = 19.0F - Mth.cos(ageInTicks * 0.18F) * 0.9F;
        }

        if (this.bodyPitch > 0.0F) {
            this.body.xRot = ModelUtils.rotlerpRad(this.body.yRot, 3.0915928F, this.bodyPitch);
        }

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public ModelPart getHead() {
        return head;
    }
}