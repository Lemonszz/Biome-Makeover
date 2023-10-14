package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.CamelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.camel.Camel;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;

public class ChestCamelRender extends CamelRenderer {
    public ChestCamelRender(EntityRendererProvider.Context context) {
        super(context, new ModelLayerLocation(new ResourceLocation("minecraft", "camel"), "main"));

        addLayer(new CamelChestLayer(context, this));
    }

    private class CamelChestLayer extends RenderLayer<Camel, CamelModel<Camel>>
    {
        private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/camel/camel_chest.png");

        private final ModelPart model;
        public CamelChestLayer(EntityRendererProvider.Context context, RenderLayerParent<Camel, CamelModel<Camel>> renderLayerParent) {
            super(renderLayerParent);

            model = context.bakeLayer(CamelChestModel.LAYER_LOCATION);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Camel entity, float f, float g, float h, float j, float k, float l)
        {
            model.getChild("root").setRotation(getParentModel().root().xRot/2.5F, getParentModel().root().yRot, getParentModel().root().zRot/2.5F);
            model.getChild("root").getChild("body").copyFrom(getParentModel().root().getChild("body"));
            int p = getOverlayCoords(entity, 0);
            boolean visible = isBodyVisible(entity);
            boolean isInvis = !visible && !entity.isInvisibleTo(Minecraft.getInstance().player);
            boolean outline = Minecraft.getInstance().shouldEntityAppearGlowing(entity);
            poseStack.pushPose();
            poseStack.translate(0, -1.5, 0);
            model.render(poseStack, multiBufferSource.getBuffer(this.getRenderType(visible, isInvis, outline)), i, p, 1.0F, 1.0F, 1.0F, 1.0F);

            poseStack.popPose();

        }

        @Nullable
        protected RenderType getRenderType(boolean bl, boolean bl2, boolean bl3) {
            ResourceLocation resourceLocation = TEXTURE;
            if (bl2) {
                return RenderType.itemEntityTranslucentCull(resourceLocation);
            } else if (bl) {
                return RenderType.entityCutoutNoCull(resourceLocation);
            } else {
                return bl3 ? RenderType.outline(resourceLocation) : null;
            }
        }
    }

    public static class CamelChestModel extends EntityModel<Camel> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("camel_chest"), "main");
        private final ModelPart root;

        public CamelChestModel(ModelPart root) {
            this.root = root.getChild("root");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

            PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.5F, -20.0F, 9.5F));

            PartDefinition Chest = body.addOrReplaceChild("Chest", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -41.0F, 5.0F, 12.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 20.0F, -9.5F));

            PartDefinition Lid = Chest.addOrReplaceChild("Lid", CubeListBuilder.create().texOffs(0, 21).addBox(-6.0F, -34.5F, 10.5F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 0).addBox(-1.0F, -33.0F, 22.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.5F, -5.5F));

            return LayerDefinition.create(meshdefinition, 128, 128);
        }

        @Override
        public void setupAnim(Camel entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        }

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}
