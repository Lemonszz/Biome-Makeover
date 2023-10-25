package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DustDevilEntity;

public class DustDevilTornadoModel extends HierarchicalModel<DustDevilEntity>
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("dust_devil_tornado"), "main");
    private final ModelPart tornado;

    public DustDevilTornadoModel(ModelPart root) {
        super(RenderType::entityTranslucent);

        this.tornado = root.getChild("tornado");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition tornado = partdefinition.addOrReplaceChild("tornado", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bottom = tornado.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 20).addBox(-4.5F, -3.5F, -4.5F, 9.0F, 7.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.5F, 0.0F));

        PartDefinition top = tornado.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -4.5F, -5.5F, 11.0F, 9.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return tornado;
    }

    @Override
    public void setupAnim(DustDevilEntity entity, float limbSwing, float swingAmount, float age, float headYaw, float headPitch)
    {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.tornadoStartAnimation, SUMMON, age, 1F);
        this.animate(entity.tornadoAnimation, IDLE, age, 1F);

    }

    public static final AnimationDefinition IDLE = AnimationDefinition.Builder.withLength(3.84f).looping()
            .addAnimation("root",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 1f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.posVec(0f, -1f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("top",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 0f, 1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.posVec(0f, 0f, -1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("top",
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
            .addAnimation("bottom",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 0f, -1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.64f, KeyframeAnimations.posVec(0f, 0f, 1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("bottom",
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
    public static final AnimationDefinition SUMMON = AnimationDefinition.Builder.withLength(3.84f)
            .addAnimation("root",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 3f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.48f, KeyframeAnimations.posVec(0f, 3f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(0f, -1f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("root",
                    new AnimationChannel(AnimationChannel.Targets.SCALE,
                            new Keyframe(0f, KeyframeAnimations.scaleVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.48f, KeyframeAnimations.scaleVec(0.2f, 0.2f, 0.2f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.scaleVec(1f, 1f, 1f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("top",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 0f, 1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.88f, KeyframeAnimations.posVec(0f, 0f, -1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("top",
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
            .addAnimation("bottom",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.posVec(0f, 0f, -1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.92f, KeyframeAnimations.posVec(-1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.64f, KeyframeAnimations.posVec(0f, 0f, 1f), AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.84f, KeyframeAnimations.posVec(1f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("bottom",
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
}