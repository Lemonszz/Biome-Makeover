package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.TadpoleEntity;

public class TadpoleRender extends MobRenderer<TadpoleEntity, TadpoleModel>
{
    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/tadpole.png");

    public TadpoleRender(EntityRendererProvider.Context context) {
        super(context, new TadpoleModel(context.bakeLayer(TadpoleModel.LAYER_LOCATION)), 0.15F);
    }

    @Override
    protected void setupRotations(TadpoleEntity tadpole, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(tadpole, poseStack, f, g, h);

        float i = 4.3F * Mth.sin(0.6F * f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(i));
        if(!tadpole.isInWater())
        {
            poseStack.translate(0.10000000149011612D, 0.10000000149011612D, -0.10000000149011612D);
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(TadpoleEntity entity) {
        return TEXTURE;
    }
}
