package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.TadpoleEntity;

public class TadpoleModel extends EntityModel<TadpoleEntity>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("tadpole"), "main");
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart finw;
    private final ModelPart fine;

    public TadpoleModel(ModelPart root) {
        this.body = root.getChild("body");

        tail = body.getChild("tail");
        finw = body.getChild("finw");
        fine = body.getChild("fine");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -1.0F, -1.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-4.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 9).addBox(-2.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 22.0F, -3.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.6F, -1.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(4, 9).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 0.0F, 2.0F));

        PartDefinition finw = body.addOrReplaceChild("finw", CubeListBuilder.create().texOffs(0, 9).addBox(0.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.0F));

        PartDefinition fine = body.addOrReplaceChild("fine", CubeListBuilder.create(), PartPose.offset(-4.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = fine.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(7, 0).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0436F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(TadpoleEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float amount = 1.0F;
        if(!entity.isInWater())
        {
            amount = 1.5F;
        }

        this.tail.yRot = -amount * 0.45F * Mth.sin(0.6F * animationProgress);
        this.finw.xRot = amount * 0.10F * Mth.sin(0.8F * animationProgress);
        this.fine.xRot = amount * 0.10F * Mth.sin(0.8F * animationProgress);
    }
}