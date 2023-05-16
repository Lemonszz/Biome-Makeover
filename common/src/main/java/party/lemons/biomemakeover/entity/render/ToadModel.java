package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.ToadEntity;
import party.lemons.taniwha.util.MathUtils;

public class ToadModel extends EntityModel<ToadEntity>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("toad"), "main");
    private final ModelPart bone;
    private final ModelPart frontlege;
    private final ModelPart backlege;
    private final ModelPart frontlegw;
    private final ModelPart backlegw;
    private final ModelPart lipTop;
    private final ModelPart lipBottom;
    private final ModelPart tounge;

    public ToadModel(ModelPart root) {
        this.bone = root.getChild("bone");

        this.frontlege = bone.getChild("frontlege");
        this.backlege = bone.getChild("backlege");
        this.frontlegw = bone.getChild("frontlegw");
        this.backlegw = bone.getChild("backlegw");
        this.lipTop = bone.getChild("lips").getChild("bone3");
        this.lipBottom= bone.getChild("lips").getChild("bone4");
        this.tounge = bone.getChild("tounge");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -3.7273F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(10, 24).addBox(1.0F, -7.0F, -2.7273F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-3.0F, -7.0F, -2.7273F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, -0.2727F));

        PartDefinition backlege = bone.addOrReplaceChild("backlege", CubeListBuilder.create().texOffs(8, 16).addBox(-2.0F, 2.0F, -4.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 16).addBox(-2.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 3.2727F));

        PartDefinition backlegw = bone.addOrReplaceChild("backlegw", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(0.0F, 2.0F, -4.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 3.2727F));

        PartDefinition eyeballw = bone.addOrReplaceChild("eyeballw", CubeListBuilder.create().texOffs(18, 28).addBox(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -6.5F, -1.2273F));

        PartDefinition eyeballe = bone.addOrReplaceChild("eyeballe", CubeListBuilder.create().texOffs(24, 3).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -6.5F, -1.2273F));

        PartDefinition lips = bone.addOrReplaceChild("lips", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -3.7273F));

        PartDefinition bone3 = lips.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(20, 18).addBox(-3.0F, 1.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone4 = lips.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(20, 16).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition frontlegw = bone.addOrReplaceChild("frontlegw", CubeListBuilder.create(), PartPose.offset(4.0F, -2.0F, -2.7273F));

        PartDefinition cube_r1 = frontlegw.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 22).addBox(0.0F, 0.684F, -1.3794F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition frontlege = bone.addOrReplaceChild("frontlege", CubeListBuilder.create(), PartPose.offset(-4.0F, -2.0F, -2.7273F));

        PartDefinition cube_r2 = frontlege.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, 0.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, -2.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition tounge = bone.addOrReplaceChild("tounge", CubeListBuilder.create().texOffs(0, 30).addBox(-0.9F, -3.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -1.7273F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    static float tongueDistance;

    @Override
    public void setupAnim(ToadEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
    {
        float pi = (float) Math.PI;
        float legAmount = 1.4F;

        this.frontlege.xRot = Mth.cos(limbAngle * 1 + pi) * 1.4F * limbDistance;
        this.backlege.xRot = Mth.cos(limbAngle * 1) * legAmount * limbDistance;

        this.frontlegw.xRot = Mth.cos(limbAngle * 1) * 1.4F * limbDistance;
        this.backlegw.xRot = Mth.cos(limbAngle * 1 + pi) * legAmount * limbDistance;

        if(!entity.onGround())
        {
            this.backlegw.xRot = 2F;
            this.backlege.xRot = 2F;
        }

        if(entity.hasTongueEntity())
        {
            entity.mouthDistance = MathUtils.approachValue(entity.mouthDistance, 1, 0.5F);
            Entity e = entity.level().getEntity(entity.getTongueEntityID());
            if(e != null && entity.isTongueReady())
            {
                tongueDistance = (entity.distanceTo(e) * 16) - ((float) (e.getBoundingBox().maxX - e.getBoundingBox().minX) * 16F);
            }else tongueDistance = 0;
        }else
        {
            entity.mouthDistance = MathUtils.approachValue(entity.mouthDistance, 0, 0.10F);
        }
        lipTop.y = -entity.mouthDistance;
        lipBottom.y = entity.mouthDistance;

        tounge.xRot = -0.2618F + (headPitch * 0.0175F);
        tounge.yRot = headYaw * 0.0175F;
    }

    public ModelPart getTounge()
    {
        return tounge;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(poseStack, buffer, packedLight, packedOverlay);
    }
}