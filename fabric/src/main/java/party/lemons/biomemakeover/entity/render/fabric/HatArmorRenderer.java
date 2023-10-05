package party.lemons.biomemakeover.entity.render.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import party.lemons.biomemakeover.entity.render.HatModels;
import party.lemons.biomemakeover.item.HatItem;

public class HatArmorRenderer implements ArmorRenderer {
    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel)
    {
        HatItem hat = (HatItem) stack.getItem();

        Model model = HatModels.getHatModel(hat, contextModel.getHead());

        model.renderToBuffer(matrices, vertexConsumers.getBuffer(model.renderType(hat.getHatTexture())), light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
    }
}
