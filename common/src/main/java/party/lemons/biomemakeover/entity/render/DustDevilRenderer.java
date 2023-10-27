package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DustDevilEntity;
import party.lemons.biomemakeover.util.sound.DustDevilIdleWindSoundInstance;
import party.lemons.biomemakeover.util.sound.LoopingAlternatingEntitySound;

public class DustDevilRenderer extends MobRenderer<DustDevilEntity, DustDevilModel>
{
    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/dust_devil/dust_devil.png");

    public DustDevilRenderer(EntityRendererProvider.Context context) {
        super(context, new DustDevilModel(context.bakeLayer(DustDevilModel.LAYER_LOCATION)), 0.3F);
        this.addLayer(new DustDevilTornadoLayer(this, new DustDevilTornadoModel(context.bakeLayer(DustDevilTornadoModel.LAYER_LOCATION))));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(DustDevilEntity entity) {
        if(!entity.hasPlayedLoop)
        {
            entity.hasPlayedLoop = true;
            Minecraft.getInstance().getSoundManager().play(new DustDevilIdleWindSoundInstance(entity));
        }

        return TEXTURE;
    }

    private static class DustDevilTornadoLayer extends RenderLayer<DustDevilEntity, DustDevilModel>
    {
        private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/dust_devil/tornado.png");
        private final DustDevilTornadoModel model;


        public DustDevilTornadoLayer(DustDevilRenderer renderer, DustDevilTornadoModel model)
        {
            super(renderer);
            this.model = model;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, DustDevilEntity entity, float f, float g, float h, float j, float k, float l)
        {
            if(entity.isTornado())
            {
                this.model.setupAnim(entity, f, g, j, k, l);
                VertexConsumer renderType;
                boolean isGlowing = Minecraft.getInstance().shouldEntityAppearGlowing(entity) && entity.isInvisible();

                if (isGlowing) {
                    renderType = multiBufferSource.getBuffer(RenderType.outline(TEXTURE));
                } else {
                    renderType = multiBufferSource.getBuffer(model.renderType(TEXTURE));
                }

                this.model.renderToBuffer(poseStack, renderType, i, getOverlayCoords(entity, 0.0F), 1F, 1F, 1F, 1F);
            }
        }
    }

    public static final AnimationDefinition DUST_DEVIL_IDLE = AnimationDefinition.Builder.withLength(3.84f).looping()
            .addAnimation("root",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 1f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.posVec(0f, -1f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("arm_r",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 0f, 6f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.44f, KeyframeAnimations.degreeVec(0f, 0f, -6f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.4f, KeyframeAnimations.degreeVec(0f, 0f, 6f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.36f, KeyframeAnimations.degreeVec(0f, 0f, -6f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("arm_l",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 0f, -6f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.44f, KeyframeAnimations.degreeVec(0f, 0f, 6f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.4f, KeyframeAnimations.degreeVec(0f, 0f, -6f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.36f, KeyframeAnimations.degreeVec(0f, 0f, 6f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("cloud_top",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 0f, 1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.posVec(0f, 0f, -1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("cloud_top",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 45f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.degreeVec(0f, 135f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.24f, KeyframeAnimations.degreeVec(0f, 225f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.36f, KeyframeAnimations.degreeVec(0f, 315f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 405f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.6f, KeyframeAnimations.degreeVec(0f, 495f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.72f, KeyframeAnimations.degreeVec(0f, 585f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.84f, KeyframeAnimations.degreeVec(0f, 675f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.degreeVec(0f, 765f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.08f, KeyframeAnimations.degreeVec(0f, 855f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.2f, KeyframeAnimations.degreeVec(0f, 945f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.32f, KeyframeAnimations.degreeVec(0f, 1035f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.44f, KeyframeAnimations.degreeVec(0f, 1125f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.56f, KeyframeAnimations.degreeVec(0f, 1215f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.68f, KeyframeAnimations.degreeVec(0f, 1305f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.8f, KeyframeAnimations.degreeVec(0f, 1395f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.degreeVec(0f, 1485f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.04f, KeyframeAnimations.degreeVec(0f, 1575f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.16f, KeyframeAnimations.degreeVec(0f, 1665f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.28f, KeyframeAnimations.degreeVec(0f, 1755f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.4f, KeyframeAnimations.degreeVec(0f, 1845f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.52f, KeyframeAnimations.degreeVec(0f, 1935f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.64f, KeyframeAnimations.degreeVec(0f, 2025f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.76f, KeyframeAnimations.degreeVec(0f, 2115f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.degreeVec(0f, 2205f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 2295f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.12f, KeyframeAnimations.degreeVec(0f, 2385f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.24f, KeyframeAnimations.degreeVec(0f, 2475f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.36f, KeyframeAnimations.degreeVec(0f, 2565f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.48f, KeyframeAnimations.degreeVec(0f, 2655f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.6f, KeyframeAnimations.degreeVec(0f, 2745f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.72f, KeyframeAnimations.degreeVec(0f, 2835f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(0f, 2915f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("cloud_bottom",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 0f, -1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.64f, KeyframeAnimations.posVec(0f, 0f, 1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("cloud_bottom",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.degreeVec(0f, 90f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.24f, KeyframeAnimations.degreeVec(0f, 180f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.36f, KeyframeAnimations.degreeVec(0f, 270f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 360f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.6f, KeyframeAnimations.degreeVec(0f, 450f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.72f, KeyframeAnimations.degreeVec(0f, 540f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.84f, KeyframeAnimations.degreeVec(0f, 630f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.degreeVec(0f, 720f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.08f, KeyframeAnimations.degreeVec(0f, 810f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.2f, KeyframeAnimations.degreeVec(0f, 900f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.32f, KeyframeAnimations.degreeVec(0f, 990f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.44f, KeyframeAnimations.degreeVec(0f, 1080f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.56f, KeyframeAnimations.degreeVec(0f, 1170f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.68f, KeyframeAnimations.degreeVec(0f, 1260f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.8f, KeyframeAnimations.degreeVec(0f, 1350f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.degreeVec(0f, 1440f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.04f, KeyframeAnimations.degreeVec(0f, 1530f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.16f, KeyframeAnimations.degreeVec(0f, 1620f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.28f, KeyframeAnimations.degreeVec(0f, 1710f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.4f, KeyframeAnimations.degreeVec(0f, 1800f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.52f, KeyframeAnimations.degreeVec(0f, 1890f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.64f, KeyframeAnimations.degreeVec(0f, 1980f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.76f, KeyframeAnimations.degreeVec(0f, 2070f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.degreeVec(0f, 2160f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 2250f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.12f, KeyframeAnimations.degreeVec(0f, 2340f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.24f, KeyframeAnimations.degreeVec(0f, 2430f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.36f, KeyframeAnimations.degreeVec(0f, 2520f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.48f, KeyframeAnimations.degreeVec(0f, 2610f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.6f, KeyframeAnimations.degreeVec(0f, 2700f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.72f, KeyframeAnimations.degreeVec(0f, 2790f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(0f, 2870f, 0f), AnimationChannel.Interpolations.CATMULLROM))).build();
    public static final AnimationDefinition DUST_DEVIL_CHARGING = AnimationDefinition.Builder.withLength(3.84f)
            .addAnimation("dust_devil",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 1f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.posVec(0f, -1f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("arm_r",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(-180f, 45f, -90f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("arm_l",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(-180f, -45f, 90f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("cloud_top",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 0f, 1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.posVec(0f, 0f, -1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("cloud_top",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 45f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.degreeVec(0f, 135f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.24f, KeyframeAnimations.degreeVec(0f, 225f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.36f, KeyframeAnimations.degreeVec(0f, 315f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 405f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.6f, KeyframeAnimations.degreeVec(0f, 495f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.72f, KeyframeAnimations.degreeVec(0f, 585f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.84f, KeyframeAnimations.degreeVec(0f, 675f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.degreeVec(0f, 765f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.08f, KeyframeAnimations.degreeVec(0f, 855f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.2f, KeyframeAnimations.degreeVec(0f, 945f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.32f, KeyframeAnimations.degreeVec(0f, 1035f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.44f, KeyframeAnimations.degreeVec(0f, 1125f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.56f, KeyframeAnimations.degreeVec(0f, 1215f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.68f, KeyframeAnimations.degreeVec(0f, 1305f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.8f, KeyframeAnimations.degreeVec(0f, 1395f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.degreeVec(0f, 1485f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.04f, KeyframeAnimations.degreeVec(0f, 1575f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.16f, KeyframeAnimations.degreeVec(0f, 1665f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.28f, KeyframeAnimations.degreeVec(0f, 1755f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.4f, KeyframeAnimations.degreeVec(0f, 1845f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.52f, KeyframeAnimations.degreeVec(0f, 1935f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.64f, KeyframeAnimations.degreeVec(0f, 2025f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.76f, KeyframeAnimations.degreeVec(0f, 2115f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.degreeVec(0f, 2205f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 2295f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.12f, KeyframeAnimations.degreeVec(0f, 2385f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.24f, KeyframeAnimations.degreeVec(0f, 2475f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.36f, KeyframeAnimations.degreeVec(0f, 2565f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.48f, KeyframeAnimations.degreeVec(0f, 2655f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.6f, KeyframeAnimations.degreeVec(0f, 2745f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.72f, KeyframeAnimations.degreeVec(0f, 2835f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(0f, 2915f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("cloud_bottom",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 0f, -1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.64f, KeyframeAnimations.posVec(0f, 0f, 1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("cloud_bottom",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.degreeVec(0f, 90f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.24f, KeyframeAnimations.degreeVec(0f, 180f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.36f, KeyframeAnimations.degreeVec(0f, 270f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 360f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.6f, KeyframeAnimations.degreeVec(0f, 450f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.72f, KeyframeAnimations.degreeVec(0f, 540f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.84f, KeyframeAnimations.degreeVec(0f, 630f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.degreeVec(0f, 720f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.08f, KeyframeAnimations.degreeVec(0f, 810f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.2f, KeyframeAnimations.degreeVec(0f, 900f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.32f, KeyframeAnimations.degreeVec(0f, 990f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.44f, KeyframeAnimations.degreeVec(0f, 1080f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.56f, KeyframeAnimations.degreeVec(0f, 1170f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.68f, KeyframeAnimations.degreeVec(0f, 1260f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.8f, KeyframeAnimations.degreeVec(0f, 1350f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.degreeVec(0f, 1440f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.04f, KeyframeAnimations.degreeVec(0f, 1530f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.16f, KeyframeAnimations.degreeVec(0f, 1620f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.28f, KeyframeAnimations.degreeVec(0f, 1710f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.4f, KeyframeAnimations.degreeVec(0f, 1800f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.52f, KeyframeAnimations.degreeVec(0f, 1890f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.64f, KeyframeAnimations.degreeVec(0f, 1980f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.76f, KeyframeAnimations.degreeVec(0f, 2070f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.degreeVec(0f, 2160f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 2250f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.12f, KeyframeAnimations.degreeVec(0f, 2340f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.24f, KeyframeAnimations.degreeVec(0f, 2430f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.36f, KeyframeAnimations.degreeVec(0f, 2520f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.48f, KeyframeAnimations.degreeVec(0f, 2610f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.6f, KeyframeAnimations.degreeVec(0f, 2700f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.72f, KeyframeAnimations.degreeVec(0f, 2790f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(0f, 2870f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("head",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(-50f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))).build();
    public static final AnimationDefinition DUST_DEVIL_TORNADOING = AnimationDefinition.Builder.withLength(3.84f).looping()
            .addAnimation("dust_devil",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 1f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.posVec(0f, -1f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("arm_r",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-180f, 45f, -90f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(-180f, 45f, -90f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("arm_l",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-180f, -45f, 90f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(-180f, -45f, 90f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("cloud_top",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 0f, 1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.posVec(0f, 0f, -1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("cloud_top",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 45f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.degreeVec(0f, 135f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.24f, KeyframeAnimations.degreeVec(0f, 225f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.36f, KeyframeAnimations.degreeVec(0f, 315f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 405f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.6f, KeyframeAnimations.degreeVec(0f, 495f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.72f, KeyframeAnimations.degreeVec(0f, 585f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.84f, KeyframeAnimations.degreeVec(0f, 675f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.degreeVec(0f, 765f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.08f, KeyframeAnimations.degreeVec(0f, 855f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.2f, KeyframeAnimations.degreeVec(0f, 945f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.32f, KeyframeAnimations.degreeVec(0f, 1035f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.44f, KeyframeAnimations.degreeVec(0f, 1125f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.56f, KeyframeAnimations.degreeVec(0f, 1215f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.68f, KeyframeAnimations.degreeVec(0f, 1305f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.8f, KeyframeAnimations.degreeVec(0f, 1395f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.degreeVec(0f, 1485f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.04f, KeyframeAnimations.degreeVec(0f, 1575f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.16f, KeyframeAnimations.degreeVec(0f, 1665f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.28f, KeyframeAnimations.degreeVec(0f, 1755f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.4f, KeyframeAnimations.degreeVec(0f, 1845f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.52f, KeyframeAnimations.degreeVec(0f, 1935f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.64f, KeyframeAnimations.degreeVec(0f, 2025f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.76f, KeyframeAnimations.degreeVec(0f, 2115f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.degreeVec(0f, 2205f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 2295f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.12f, KeyframeAnimations.degreeVec(0f, 2385f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.24f, KeyframeAnimations.degreeVec(0f, 2475f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.36f, KeyframeAnimations.degreeVec(0f, 2565f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.48f, KeyframeAnimations.degreeVec(0f, 2655f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.6f, KeyframeAnimations.degreeVec(0f, 2745f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.72f, KeyframeAnimations.degreeVec(0f, 2835f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(0f, 2915f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("cloud_bottom",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 0f, -1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.64f, KeyframeAnimations.posVec(0f, 0f, 1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("cloud_bottom",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.degreeVec(0f, 90f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.24f, KeyframeAnimations.degreeVec(0f, 180f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.36f, KeyframeAnimations.degreeVec(0f, 270f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 360f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.6f, KeyframeAnimations.degreeVec(0f, 450f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.72f, KeyframeAnimations.degreeVec(0f, 540f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.84f, KeyframeAnimations.degreeVec(0f, 630f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.degreeVec(0f, 720f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.08f, KeyframeAnimations.degreeVec(0f, 810f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.2f, KeyframeAnimations.degreeVec(0f, 900f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.32f, KeyframeAnimations.degreeVec(0f, 990f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.44f, KeyframeAnimations.degreeVec(0f, 1080f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.56f, KeyframeAnimations.degreeVec(0f, 1170f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.68f, KeyframeAnimations.degreeVec(0f, 1260f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.8f, KeyframeAnimations.degreeVec(0f, 1350f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.degreeVec(0f, 1440f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.04f, KeyframeAnimations.degreeVec(0f, 1530f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.16f, KeyframeAnimations.degreeVec(0f, 1620f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.28f, KeyframeAnimations.degreeVec(0f, 1710f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.4f, KeyframeAnimations.degreeVec(0f, 1800f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.52f, KeyframeAnimations.degreeVec(0f, 1890f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.64f, KeyframeAnimations.degreeVec(0f, 1980f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.76f, KeyframeAnimations.degreeVec(0f, 2070f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.degreeVec(0f, 2160f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 2250f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.12f, KeyframeAnimations.degreeVec(0f, 2340f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.24f, KeyframeAnimations.degreeVec(0f, 2430f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.36f, KeyframeAnimations.degreeVec(0f, 2520f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.48f, KeyframeAnimations.degreeVec(0f, 2610f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.6f, KeyframeAnimations.degreeVec(0f, 2700f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.72f, KeyframeAnimations.degreeVec(0f, 2790f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(0f, 2870f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("head",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(-50f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.degreeVec(-50f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM))).build();


}
