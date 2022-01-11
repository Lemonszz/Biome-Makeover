package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DragonflyEntity;

public class DragonflyModel extends EntityModel<DragonflyEntity>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("dragonfly"), "main");
    private final ModelPart Body;
    private final ModelPart right_top;
    private final ModelPart left_top;
    private final ModelPart right_bottom;
    private final ModelPart left_bottom;

    public DragonflyModel(ModelPart root) {
        this.Body = root.getChild("Body");

        this.right_top = root.getChild("Body").getChild("right_top");
        this.right_bottom = root.getChild("Body").getChild("right_bottom");
        this.left_top = root.getChild("Body").getChild("left_top");
        this.left_bottom = root.getChild("Body").getChild("left_bottom");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(0.5F, -3.5F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.5F, -3.5F, -1.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.5F, -3.5F, 1.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, -3.5F, -1.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, -3.5F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, -3.5F, 1.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(10, 10).addBox(-2.0F, -5.0F, -4.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-1.5F, -5.5F, -2.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(-1.0F, -5.0F, 1.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition left_top = Body.addOrReplaceChild("left_top", CubeListBuilder.create(), PartPose.offset(0.5F, -5.4F, -1.0F));

        PartDefinition topwing_r1 = left_top.addOrReplaceChild("topwing_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, -1.0F, 6.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition left_bottom = Body.addOrReplaceChild("left_bottom", CubeListBuilder.create(), PartPose.offset(0.5F, -5.4F, -1.0F));

        PartDefinition bottomwing_r1 = left_bottom.addOrReplaceChild("bottomwing_r1", CubeListBuilder.create().texOffs(5, 5).addBox(0.0F, 0.0F, -1.0F, 5.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition right_top = Body.addOrReplaceChild("right_top", CubeListBuilder.create(), PartPose.offset(-1.5F, -5.5F, -1.0F));

        PartDefinition topwing_r2 = right_top.addOrReplaceChild("topwing_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-5.5F, 0.1F, -1.0F, 6.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition right_bottom = Body.addOrReplaceChild("right_bottom", CubeListBuilder.create(), PartPose.offset(-1.5F, -5.0F, -1.0F));

        PartDefinition bottomwing_r2 = right_bottom.addOrReplaceChild("bottomwing_r2", CubeListBuilder.create().texOffs(5, 5).mirror().addBox(-5.0F, -0.4F, -1.0F, 5.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(DragonflyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float change = 0.436332F; // 20 degrees
        if(entity.tickCount % 2 == 0)
        {
            right_top.zRot = change;
            left_top.zRot = change;
            right_bottom.zRot = -change;
            left_bottom.zRot = -change;
        }else
        {
            right_top.zRot = -change;
            left_top.zRot = -change;
            left_bottom.zRot = change;
            right_bottom.zRot = change;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Body.render(poseStack, buffer, packedLight, packedOverlay);
    }
}