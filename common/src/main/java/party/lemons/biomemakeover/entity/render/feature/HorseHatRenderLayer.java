package party.lemons.biomemakeover.entity.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.animal.horse.Horse;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.item.HatItem;
import party.lemons.biomemakeover.util.extension.HorseHat;

public class HorseHatRenderLayer extends HatLayer<Horse, HorseModel<Horse>> {

    public HorseHatRenderLayer(RenderLayerParent<Horse, HorseModel<Horse>> par, EntityModelSet modelSet)
    {
        super(par, modelSet);
    }

    @Override
    public HatItem getHatItem(Horse entity) {
        if(((HorseHat)entity).hasHat())
            return (HatItem) BMItems.COWBOY_HAT;
        return null;
    }

    @Override
    public void setupHat(PoseStack poseStack) {
        poseStack.scale(1.05F, 1.05F, 1.05F);

        getParentModel().headParts().iterator().next().translateAndRotate(poseStack);
        poseStack.translate(0F, -0.4F, 0);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-25F));
    }
}
