package party.lemons.biomemakeover.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import party.lemons.biomemakeover.BiomeMakeover;

public class WitchHatModel<T extends Entity> extends EntityModel<T> implements HatModel{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("witch_hat"), "main");
    private final ModelPart head;

    public WitchHatModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition partDefinition2 = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 10.0f, 8.0f), PartPose.ZERO);
        PartDefinition partDefinition3 = partDefinition2.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 64).addBox(0.0f, -2.0f, 0.0f, 10.0f, 2.0f, 10.0f), PartPose.offset(-5.0f, -6.53125f, -5.0f));
        PartDefinition partDefinition4 = partDefinition3.addOrReplaceChild("hat2", CubeListBuilder.create().texOffs(0, 76).addBox(0.0f, -2.0f, 0.0f, 7.0f, 4.0f, 7.0f), PartPose.offsetAndRotation(1.75f, -4.0f, 2.0f, -0.05235988f, 0.0f, 0.02617994f));
        PartDefinition partDefinition5 = partDefinition4.addOrReplaceChild("hat3", CubeListBuilder.create().texOffs(0, 87).addBox(0.0f, -2.0f, 0.0f, 4.0f, 4.0f, 4.0f), PartPose.offsetAndRotation(1.75f, -4.0f, 2.0f, -0.10471976f, 0.0f, 0.05235988f));
        partDefinition5.addOrReplaceChild("hat4", CubeListBuilder.create().texOffs(0, 95).addBox(0.0f, -2.0f, 0.0f, 1.0f, 2.0f, 1.0f, new CubeDeformation(0.25f)), PartPose.offsetAndRotation(1.75f, -2.0f, 2.0f, -0.20943952f, 0.0f, 0.10471976f));

        return LayerDefinition.create(meshdefinition, 64, 128);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {

    }

    @Override
    public void copyHead(ModelPart model) {
        head.copyFrom(model);
    }
}