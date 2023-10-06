package party.lemons.biomemakeover.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.item.HatItem;

public abstract class CowboyHatRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    CowboyHatModel<T> model;

    public CowboyHatRenderLayer(RenderLayerParent<T, M> renderLayerParent, EntityModelSet modelSet) {
        super(renderLayerParent);
        model = new CowboyHatModel<>(modelSet.bakeLayer(CowboyHatModel.LAYER_LOCATION));

    }

    protected boolean hasHat(T entity)
    {
        return true;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float f, float g, float h, float j, float k, float l) {
        if(!hasHat(entity))
            return;

        poseStack.pushPose();
        setup(poseStack);

        model.renderToBuffer(poseStack, multiBufferSource.getBuffer(model.renderType(((HatItem)BMItems.COWBOY_HAT.get()).getHatTexture())), i, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

        poseStack.popPose();
    }

    protected abstract void setup(PoseStack poseStack);
}
