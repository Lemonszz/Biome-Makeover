package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.ItemStack;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.StoneGolemEntity;

import java.util.Map;

public class StoneGolemRender extends MobRenderer<StoneGolemEntity, StoneGolemModel> {

    public static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/stone_golem/stone_golem.png");

    public StoneGolemRender(EntityRendererProvider.Context context) {
        super(context, new StoneGolemModel(context.bakeLayer(StoneGolemModel.LAYER_LOCATION)), 1F);

        addLayer(new StoneGolemItemLayer(this, context.getItemInHandRenderer()));
        addLayer(new StoneGolemCrackLayer(this));
    }

    @Override
    public void render(StoneGolemEntity entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {

        poseStack.pushPose();
        boolean visible = this.isBodyVisible(entity);
        boolean isInvis = !visible && !entity.isInvisibleTo(Minecraft.getInstance().player);
        boolean outline = Minecraft.getInstance().shouldEntityAppearGlowing(entity);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0D, -1.5010000467300415D, 0.0D); //????
        RenderType renderLayer = this.getRenderType(entity, visible, isInvis, outline);
        if(renderLayer != null)
        {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderLayer);
            int overlay = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, g));
            getModel().renderBaseToBuffer(poseStack, vertexConsumer, i, overlay,1F, 1F, 1F, isInvis ? 0.15F : 1.0F);
        }
        poseStack.popPose();

        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(StoneGolemEntity entity) {
        return TEXTURE;
    }

    private static class StoneGolemCrackLayer extends RenderLayer<StoneGolemEntity, StoneGolemModel> {
        private static final Map<IronGolem.Crackiness, ResourceLocation> DAMAGE_TO_TEXTURE;
        static {
            DAMAGE_TO_TEXTURE = ImmutableMap.of(IronGolem.Crackiness.LOW, BiomeMakeover.ID("textures/entity/stone_golem/stone_golem_damaged_light.png"),
                    IronGolem.Crackiness.MEDIUM, BiomeMakeover.ID("textures/entity/stone_golem/stone_golem_damaged_medium.png"),
                    IronGolem.Crackiness.HIGH, BiomeMakeover.ID("textures/entity/stone_golem/stone_golem_damaged_high.png"));
        }

        public StoneGolemCrackLayer(StoneGolemRender stoneGolemRender) {
            super(stoneGolemRender);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, StoneGolemEntity entity, float f, float g, float h, float j, float k, float l) {
            if (!entity.isInvisible()) {
                IronGolem.Crackiness crack = entity.getCrack();
                if (crack != IronGolem.Crackiness.NONE) {
                    ResourceLocation identifier = DAMAGE_TO_TEXTURE.get(crack);
                    renderColoredCutoutModel(this.getParentModel(), identifier, poseStack, multiBufferSource, i, entity, 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }

    private class StoneGolemItemLayer extends RenderLayer<StoneGolemEntity, StoneGolemModel> {
        private final ItemInHandRenderer itemInHandRenderer;

        public StoneGolemItemLayer(StoneGolemRender stoneGolemRender, ItemInHandRenderer itemInHandRenderer) {
            super(stoneGolemRender);

            this.itemInHandRenderer = itemInHandRenderer;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, StoneGolemEntity entity, float f, float g, float h, float j, float k, float l) {
            boolean rightHanded = entity.getMainArm() == HumanoidArm.RIGHT;
            ItemStack leftHand = rightHanded ? entity.getOffhandItem() : entity.getMainHandItem();
            ItemStack rightHand = rightHanded ? entity.getMainHandItem() : entity.getOffhandItem();
            if (!leftHand.isEmpty() || !rightHand.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0, 0.5F, 0);
                this.renderItem(entity, rightHand, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, poseStack, multiBufferSource, i);
                this.renderItem(entity, leftHand, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, poseStack, multiBufferSource, i);
                poseStack.popPose();
            }
        }

        private void renderItem(StoneGolemEntity entity, ItemStack stack, ItemTransforms.TransformType transformationMode, HumanoidArm arm, PoseStack poseStack, MultiBufferSource multiBufferSource, int light)
        {
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                ((ArmedModel)this.getParentModel()).translateToHand(arm, poseStack);
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                boolean isLeft = arm == HumanoidArm.LEFT;
                poseStack.translate((isLeft ? -0.7F : 0.7F) / 16.0F, 0.125D, -1.75);
                itemInHandRenderer.renderItem(entity, stack, transformationMode, isLeft, poseStack, multiBufferSource, light);
                poseStack.popPose();
            }
        }
    }
}
