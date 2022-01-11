package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.entity.BMBoatEntity;
import party.lemons.biomemakeover.util.registry.boat.BoatType;
import party.lemons.biomemakeover.util.registry.boat.BoatTypes;

import java.util.Map;

public class BMBoatRender extends EntityRenderer<BMBoatEntity>
{
    private final Map<BoatType, Pair<ResourceLocation, BoatModel>> boatResources = Maps.newHashMap();


    private final Map<BoatType, ResourceLocation> textures = Maps.newHashMap();

    public BMBoatRender(EntityRendererProvider.Context context)
    {
        super(context);
        this.shadowRadius = 0.8F;

        for(BoatType boatType : BoatTypes.TYPES)
        {
            boatResources.put(boatType, Pair.of(boatType.getTexture(), new BoatModel(context.bakeLayer(new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, boatType.getModelLocation()), "main")))));
        }
    }

    @Override
    public void render(BMBoatEntity boat, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i)
    {

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.375D, 0.0D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - f));
        float h = (float) boat.getHurtTime() - g;
        float j = boat.getDamage() - g;
        if(j < 0.0F)
        {
            j = 0.0F;
        }

        if(h > 0.0F)
        {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(h) * h * j / 10.0F * (float) boat.getHurtDir()));
        }

        float k = boat.getBubbleAngle(g);
        if(!Mth.equal(k, 0.0F))
        {
            poseStack.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), boat.getBubbleAngle(g), true));
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        Pair<ResourceLocation, BoatModel> data = boatResources.get(boat.getNewBoatType());
        BoatModel model = data.getSecond();
        ResourceLocation texture = data.getFirst();

        model.setupAnim(boat, g, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(model.renderType(texture));
        model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        if (!boat.isUnderWater()) {
            VertexConsumer vertexConsumer2 = multiBufferSource.getBuffer(RenderType.waterMask());
            model.waterPatch().render(poseStack, vertexConsumer2, i, OverlayTexture.NO_OVERLAY);
        }
        poseStack.popPose();
        super.render(boat, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(BMBoatEntity entity) {
        BoatType type = entity.getNewBoatType();

        if(textures.containsKey(type)) return textures.get(type);
        else
        {
            ResourceLocation texture = type.getTexture();
            textures.put(type, texture);

            return texture;
        }
    }
}