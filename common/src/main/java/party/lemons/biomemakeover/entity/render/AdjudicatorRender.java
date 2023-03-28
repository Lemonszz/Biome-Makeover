package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorStateProvider;

public class AdjudicatorRender extends MobRenderer<AdjudicatorEntity, AdjudicatorModel<AdjudicatorEntity>> {

    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/adjudicator.png");

    public AdjudicatorRender(EntityRendererProvider.Context context) {
        super(context, new AdjudicatorModel<>(context.bakeLayer(AdjudicatorModel.LAYER_LOCATION)), 0.25F);
        addLayer(new AdjudicatorHeldItemRenderer<>(this, context.getItemInHandRenderer()));
        addLayer(new AdjudicatorEyesRenderLayer<>(this));
        addLayer(new InvulnerableFeatureRenderer(this));
    }

    @Override
    protected void setupRotations(AdjudicatorEntity entity, PoseStack poseStack, float animationProgress, float bodyYaw, float tickDelta) {
        super.setupRotations(entity, poseStack, animationProgress, bodyYaw, tickDelta);

        switch (entity.getState()) {
            case WAITING -> {
                float waitAng = Mth.rotLerp(tickDelta, entity.renderRotPrevious, entity.stateTime * 1F);
                poseStack.mulPose(Axis.YP.rotationDegrees(waitAng));
                poseStack.translate(0, Math.sin(animationProgress / 25F) / 4F, 0);
                entity.renderRotPrevious = waitAng;
            }
            case TELEPORT -> {
                float teleAngle = Mth.rotLerp(tickDelta, entity.renderRotPrevious, entity.stateTime * entity.stateTime);
                poseStack.mulPose(Axis.YP.rotationDegrees(teleAngle));
                entity.renderRotPrevious = teleAngle;
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(AdjudicatorEntity entity) {
        return TEXTURE;
    }

    public static class AdjudicatorHeldItemRenderer<T extends Mob & AdjudicatorStateProvider , M extends EntityModel<T> & ArmedModel> extends ItemInHandLayer<T, M> {
        private final ItemInHandRenderer itemInHandRenderer;

        public AdjudicatorHeldItemRenderer(RenderLayerParent renderLayerParent, ItemInHandRenderer itemInHandRenderer) {
            super(renderLayerParent, itemInHandRenderer);

            this.itemInHandRenderer = itemInHandRenderer;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T boss, float f, float g, float h, float j, float k, float l) {
            boolean rightHanded = boss.getMainArm() == HumanoidArm.RIGHT;
            ItemStack leftHand = rightHanded ? boss.getOffhandItem() : boss.getMainHandItem();
            ItemStack rightHand = rightHanded ? boss.getMainHandItem() : boss.getOffhandItem();
            if (!leftHand.isEmpty() || !rightHand.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0, 0.75F, 0);
                this.renderArmWithItem(boss, rightHand, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack, multiBufferSource, i);
                this.renderArmWithItem(boss, leftHand, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, poseStack, multiBufferSource, i);
                poseStack.popPose();
            }
        }

        @Override
        protected void renderArmWithItem(LivingEntity livingEntity, ItemStack stack, ItemDisplayContext transformType, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                this.getParentModel().translateToHand(humanoidArm, poseStack);
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                boolean isLeft = humanoidArm == HumanoidArm.LEFT;
                poseStack.translate((isLeft ? -0.7F : 0.7F) / 16.0F, 0.125D, -0.625D);
                itemInHandRenderer.renderItem(livingEntity, stack, transformType, isLeft, poseStack, multiBufferSource, i);
                poseStack.popPose();
            }
        }
    }

    public static class AdjudicatorEyesRenderLayer<T extends Mob & AdjudicatorStateProvider, M extends EntityModel<T>> extends EyesLayer<T, M>
    {
        private static RenderType renderType = RenderType.eyes(BiomeMakeover.ID("textures/entity/adjudicator_eyes.png"));

        public AdjudicatorEyesRenderLayer(RenderLayerParent<T, M> renderLayerParent) {
            super(renderLayerParent);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float f, float g, float h, float j, float k, float l) {
            if(entity.getState() != AdjudicatorState.WAITING)
                super.render(poseStack, multiBufferSource, i, entity, f, g, h, j, k, l);
        }

        @Override
        public RenderType renderType() {
            return renderType;
        }
    }

    private class InvulnerableFeatureRenderer extends EnergySwirlLayer<AdjudicatorEntity, AdjudicatorModel<AdjudicatorEntity>> {
        private final ResourceLocation SKIN = new ResourceLocation("textures/entity/wither/wither_armor.png");

        public InvulnerableFeatureRenderer(AdjudicatorRender adjudicatorRender) {
            super(adjudicatorRender);
        }

        @Override
        protected float xOffset(float f) {
            return Mth.cos(f * 0.02F) * 3.0F;
        }

        @Override
        protected ResourceLocation getTextureLocation() {
            return SKIN;
        }

        @Override
        protected EntityModel<AdjudicatorEntity> model() {
            return getParentModel();
        }
    }
}
