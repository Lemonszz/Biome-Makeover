package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DecayedEntity;

public class DecayedRender extends AbstractZombieRenderer<DecayedEntity, DecayedModel>
{
    public static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/decayed_inner_layer.png");

    public DecayedRender(EntityRendererProvider.Context context) {
        super(context, new DecayedModel(context.bakeLayer(DecayedModel.LAYER_LOCATION)),  new DecayedModel(context.bakeLayer(DecayedModel.LAYER_LOCATION_2)), new DecayedModel(context.bakeLayer(DecayedModel.LAYER_LOCATION_3)));

        this.addLayer(new DecayedOverlayFeatureRenderer(this, context.getModelSet()));
        this.layers.removeIf((l)->l instanceof ItemInHandLayer);

        this.addLayer(new DecayedItemInHandLayer(this, context.getItemInHandRenderer()));

    }

    @Override
    protected void setupRotations(DecayedEntity decayed, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(decayed, poseStack, f, g, h);

        float i = decayed.getSwimAmount(h);
        if(i > 0.0F)
        {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(Mth.lerp(i, decayed.getXRot(), -10.0F - decayed.getXRot())));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(DecayedEntity mob) {
        return TEXTURE;
    }

    private static class DecayedOverlayFeatureRenderer extends RenderLayer<DecayedEntity, DecayedModel>
    {
        private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/decayed_outer_layer.png");
        private final DecayedModel model;

        public DecayedOverlayFeatureRenderer(RenderLayerParent<DecayedEntity, DecayedModel> renderLayerParent, EntityModelSet entityModelSet) {
            super(renderLayerParent);
            this.model = new DecayedModel(entityModelSet.bakeLayer(DecayedModel.LAYER_LOCATION_2));
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, DecayedEntity drowned, float f, float g, float h, float j, float k, float l) {
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, TEXTURE, poseStack, multiBufferSource, i, drowned, f, g, j, k, l, h, 1.0f, 1.0f, 1.0f);
        }
    }

    private static class DecayedItemInHandLayer extends ItemInHandLayer<DecayedEntity, DecayedModel> {
        private final ItemInHandRenderer itemInHandRenderer;

        public DecayedItemInHandLayer(RenderLayerParent<DecayedEntity, DecayedModel> renderLayerParent, ItemInHandRenderer itemInHandRenderer) {
            super(renderLayerParent, itemInHandRenderer);

            this.itemInHandRenderer = itemInHandRenderer;
        }

        private boolean usingShield(LivingEntity entity)
        {
            return entity.getUseItem().getItem() == Items.SHIELD;
        }

        @Override
        protected void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, ItemTransforms.TransformType transformType, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
            if (itemStack.isEmpty()) {
                return;
            }
            if(usingShield(livingEntity))
            {
                poseStack.pushPose();

                this.getParentModel().translateToHand(humanoidArm, poseStack);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                boolean isLeft = humanoidArm == HumanoidArm.LEFT;
                poseStack.translate(2F * (isLeft ? 1F : -1F) / 16.0F, 0.3D, -0.625D);
                Vector3f dirY = isLeft ? Vector3f.YN : Vector3f.YP;
                Vector3f dirZ = isLeft ? Vector3f.ZP : Vector3f.ZN;
                Vector3f dirX = Vector3f.XN;

                poseStack.mulPose(dirY.rotationDegrees(60));
                poseStack.mulPose(dirZ.rotationDegrees(0));
                poseStack.mulPose(dirX.rotationDegrees(25));

                itemInHandRenderer.renderItem(livingEntity, itemStack, transformType, isLeft, poseStack, multiBufferSource, i);
                poseStack.popPose();
            }
            else {
                poseStack.pushPose();
                ((ArmedModel) this.getParentModel()).translateToHand(humanoidArm, poseStack);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(-90.0f));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0f));
                boolean isLeft = humanoidArm == HumanoidArm.LEFT;
                poseStack.translate((float) (isLeft ? -1 : 1) / 16.0f, 0.125, -0.625);
                itemInHandRenderer.renderItem(livingEntity, itemStack, transformType, isLeft, poseStack, multiBufferSource, i);
                poseStack.popPose();
            }
        }
    }
}
