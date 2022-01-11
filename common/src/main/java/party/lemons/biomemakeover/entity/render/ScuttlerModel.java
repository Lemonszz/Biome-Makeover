package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.ScuttlerEntity;

public class ScuttlerModel extends AgeableListModel<ScuttlerEntity>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("scuttler"), "main");

    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart left_front_leg;
    private final ModelPart right_front_leg;
    private final ModelPart right_back_leg;
    private final ModelPart left_back_leg;
    private final ModelPart tail;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart rattler;

    public ScuttlerModel(ModelPart root) {
        super(true, 8F, 2F);

        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.jaw = root.getChild("head").getChild("jaw");
        this.left_front_leg = root.getChild("body").getChild("left_front_leg");
        this.right_front_leg = root.getChild("body").getChild("right_front_leg");
        this.right_back_leg = root.getChild("body").getChild("right_back_leg");
        this.left_back_leg = root.getChild("body").getChild("left_back_leg");
        this.tail = root.getChild("body").getChild("tail");
        this.tail2 = root.getChild("body").getChild("tail").getChild("tail2");
        this.tail3 = root.getChild("body").getChild("tail").getChild("tail2").getChild("tail3");
        this.rattler = root.getChild("body").getChild("tail").getChild("tail2").getChild("tail3").getChild("rattler");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.5F, 0.0F, 6.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 9).addBox(-0.5F, -2.5F, 1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 9).addBox(-0.5F, -2.5F, 5.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 9).addBox(-0.5F, -2.5F, 9.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 22.0F, -4.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition left_front_leg = body.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(24, 5).addBox(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 2.0F, -0.0873F, 1.1345F, 0.0436F));

        PartDefinition right_front_leg = body.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(24, 5).addBox(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 2.0F, -0.0873F, 2.0071F, -0.0436F));

        PartDefinition right_back_leg = body.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(26, 15).addBox(-1.0F, -0.7978F, -1.2235F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.1566F, 9.1895F, -0.2618F, 0.0F, 0.0F));

        PartDefinition left_back_leg = body.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(26, 15).addBox(-1.0F, -0.7978F, -1.2235F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.1566F, 9.1895F, -0.2618F, 0.0F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 12.0F, 0.6981F, 0.0F, 0.0F));

        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5448F, 3.5642F, 0.4363F, 0.0F, 0.0F));

        PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(13, 25).addBox(-1.0F, -1.4605F, -0.3184F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0783F, 1.2687F, 0.7854F, 0.0F, 0.0F));

        PartDefinition rattler = tail3.addOrReplaceChild("rattler", CubeListBuilder.create().texOffs(15, 17).addBox(-1.5F, -1.4463F, -0.0312F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6005F, 1.5858F, 0.1309F, 0.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 15).addBox(-2.5F, -2.0F, -4.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 9).addBox(-0.5F, -3.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 22.0F, -4.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));


        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(ScuttlerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadyRot, float headxRot)
    {
        setRotationAngle(body, 0.2618F, 0.0F, 0.0F);
        setRotationAngle(head, -0.2618F, 0.0F, 0.0F);
        setRotationAngle(jaw, 0.1309F, 0.0F, 0.0F);
        setRotationAngle(left_front_leg, -0.0873F, 1.1345F, 0.0436F);
        setRotationAngle(right_front_leg, -0.0873F, 2.0071F, -0.0436F);
        setRotationAngle(right_back_leg, -0.2618F, 0.0F, 0.0F);
        setRotationAngle(left_back_leg, -0.2618F, 0.0F, 0.0F);
        setRotationAngle(tail, 0.6981F, 0.0F, 0.0F);
        setRotationAngle(tail2, 0.4363F, 0.0F, 0.0F);
        setRotationAngle(tail3, 0.7854F, 0.0F, 0.0F);
        setRotationAngle(rattler, 0.1309F, 0.0F, 0.0F);

        float pi = (float) Math.PI;
        this.head.xRot = -0.2618F + (headxRot * 0.0175F);
        this.head.yRot = netHeadyRot * 0.0175F;
        this.right_back_leg.xRot = Mth.cos(limbSwing * 0.66F) * 1.4F * limbSwingAmount;
        this.left_back_leg.xRot = Mth.cos(limbSwing * 0.66F + pi) * 1.4F * limbSwingAmount;
        this.right_front_leg.yRot = (pi + -(1.1345F + (Mth.cos(limbSwing * 0.66F) * 1.0F * limbSwingAmount)));
        this.left_front_leg.yRot = 1.1345F + (Mth.cos(limbSwing * 0.66F) * 1.0F * limbSwingAmount);

        //this.tail.xRot = 5;
        this.tail.zRot = Mth.cos(limbSwing * 0.66F + pi) * 0.7F * limbSwingAmount;
        this.tail3.zRot = -Mth.cos(limbSwing * 0.66F + pi) * 0.5F * limbSwingAmount;
        this.rattler.zRot = -Mth.cos(limbSwing * 0.66F + pi) * 0.7F * limbSwingAmount;
        this.rattler.xRot = -Mth.cos(limbSwing * 0.66F + pi) * 1F * (limbSwingAmount / 2);

        this.tail.zRot += Mth.sin(entity.rattleTime);
        this.tail3.zRot += -Mth.sin(entity.rattleTime);
        this.rattler.zRot += -Mth.sin(entity.rattleTime);

        if(entity.getEntityData().get(ScuttlerEntity.EATING))
            this.head.xRot = -Mth.cos(entity.eatTime * 0.6F + pi) * 0.3F;
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z)
    {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(body);
    }
}