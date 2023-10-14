package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CamelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import party.lemons.biomemakeover.entity.camel.EquipmentCamelEntity;

public class BannerCamelRender extends CamelRenderer {
    public BannerCamelRender(EntityRendererProvider.Context context) {
        super(context, new ModelLayerLocation(new ResourceLocation("minecraft", "camel"), "main"));

        addLayer(new BannerLayer(this));
    }

    private static class BannerLayer extends RenderLayer<Camel, CamelModel<Camel>> {
        private final BannerBlockEntity bannerRenderer = new BannerBlockEntity(BlockPos.ZERO, Blocks.WHITE_BANNER.defaultBlockState());

        public BannerLayer(RenderLayerParent<Camel, CamelModel<Camel>> renderLayerParent) {
            super(renderLayerParent);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Camel entity, float f, float g, float h, float j, float k, float l) {
            ItemStack stack = ((EquipmentCamelEntity) entity).getEquipmentItem();
            if (!stack.isEmpty() && stack.getItem() instanceof BannerItem) {
                this.bannerRenderer.fromItem(stack, ((AbstractBannerBlock) ((BannerItem) stack.getItem()).getBlock()).getColor());

                getParentModel().root().translateAndRotate(poseStack);
                getParentModel().root().getChild("body").translateAndRotate(poseStack);
                poseStack.translate(0.525F, -2.75F, -0.6F);
                poseStack.rotateAround(Axis.ZP.rotationDegrees(180), 0, 1, 0);

                Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(bannerRenderer).render(bannerRenderer, j, poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY);
            }
        }
    }
}
