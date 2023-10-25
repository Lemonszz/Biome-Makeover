package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.HumanoidArm;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DustDevilEntity;

public class DustDevilModel extends HierarchicalModel<DustDevilEntity> implements ArmedModel, HeadedModel
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("dust_devil"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart cloud;
    private final ModelPart right_arm;
    private final ModelPart left_arm;

    public DustDevilModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root.getChild("dust_devil");
        this.body = this.root.getChild("body");
        this.cloud = this.root.getChild("cloud");
        this.head = this.body.getChild("head");

        this.right_arm = this.body.getChild("arm_r");
        this.left_arm = this.body.getChild("arm_l");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition dust_devil = partdefinition.addOrReplaceChild("dust_devil", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = dust_devil.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, -5.0F, -1.0F, 3.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 23).addBox(-1.5F, -7.0F, -1.0F, 3.0F, 8.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition arm_l = body.addOrReplaceChild("arm_l", CubeListBuilder.create().texOffs(21, 0).addBox(0.0F, 0.0F, -0.9F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -5.0F, 0.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition arm_r = body.addOrReplaceChild("arm_r", CubeListBuilder.create().texOffs(10, 23).addBox(-2.0F, 0.0F, -0.9F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -5.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 10).addBox(-2.5F, -3.4F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(-4.5F, -5.5F, -1.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(1.5F, -5.5F, -1.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.5F, 0.0F));

        PartDefinition cloud = dust_devil.addOrReplaceChild("cloud", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 0.0F));

        PartDefinition cloud_top = cloud.addOrReplaceChild("cloud_top", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -1.5F, -3.5F, 7.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, 0.0F));

        PartDefinition cloud_bottom = cloud.addOrReplaceChild("cloud_bottom", CubeListBuilder.create().texOffs(15, 15).addBox(-2.5F, -1.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack pose) {
        this.root.translateAndRotate(pose);
        this.body.translateAndRotate(pose);
        pose.translate(0.0F, 0.0625F, 0.1875F);
        pose.mulPose(Axis.XP.rotation(this.right_arm.xRot));
        pose.scale(0.7F, 0.7F, 0.7F);
        pose.translate(0.0625F, 0.0F, 0.0F);
    }
    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(DustDevilEntity entity, float limbSwing, float swingAmount, float age, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.head.xRot = headPitch * (float) (Math.PI / 180.0);
        this.head.yRot = headYaw * (float) (Math.PI / 180.0);

        this.animate(entity.idleAnimation, DustDevilRenderer.DUST_DEVIL_IDLE, age, 1F);
        this.animate(entity.tornadoStartAnimation, DustDevilRenderer.DUST_DEVIL_CHARGING, age, 1F);
        this.animate(entity.tornadoAnimation, DustDevilRenderer.DUST_DEVIL_TORNADOING, age, 1F);

        float movementRotation = Math.min(swingAmount / 0.3F, 1.0F);

        this.body.xRot = movementRotation * (float) (Math.PI / 8);
        this.cloud.xRot = movementRotation * (float) (Math.PI / 12);

    }

    @Override
    public ModelPart getHead() {
        return head;
    }
}