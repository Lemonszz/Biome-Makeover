package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.MothEntity;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.sound.EntityLoopSoundInstance;

public class MothRender extends MobRenderer<MothEntity, MothModel> {

    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/moth.png");


    public MothRender(EntityRendererProvider.Context context) {
        super(context, new MothModel(context.bakeLayer(MothModel.LAYER_LOCATION)), 0.25F);
    }

    @Override
    protected void setupRotations(MothEntity livingEntity, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(livingEntity, poseStack, f, g, h);
        poseStack.translate(0, 0.25F, 0);
    }

    @Override
    public ResourceLocation getTextureLocation(MothEntity entity) {
        if(!entity.hasPlayedLoop)
        {
            entity.hasPlayedLoop = true;
            Minecraft.getInstance().getSoundManager().play(new EntityLoopSoundInstance(entity, BMEffects.MOTH_FLAP.get()));
        }


        return TEXTURE;
    }
}
