package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.CamelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;

public class ChestCamelRender extends CamelRenderer {
    public ChestCamelRender(EntityRendererProvider.Context context) {
        super(context, new ModelLayerLocation(new ResourceLocation("minecraft", "camel"), "main"));

        addLayer(new CamelChestLayer(context, this));
    }

    private static class CamelChestLayer extends RenderLayer<Camel, CamelModel<Camel>>
    {
        private final ChestBlockEntity chestDummy;
        private float openness = 0.0F;

        public CamelChestLayer(EntityRendererProvider.Context context, RenderLayerParent<Camel, CamelModel<Camel>> renderLayerParent) {
            super(renderLayerParent);

            chestDummy = new ChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState()){
                @Override
                public float getOpenNess(float f) {
                    return openness;
                }
            };
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, Camel entity, float f, float g, float h, float delta, float k, float l) {
            poseStack.pushPose();
            getParentModel().root().translateAndRotate(poseStack);
            getParentModel().root().getChild("body").translateAndRotate(poseStack);
            poseStack.scale(0.85F, 0.85F, 0.85F);
            poseStack.translate(0.5, -0.88F, -0.4F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));

            openness = Mth.abs(getParentModel().root().zRot) / 2F;
            Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(chestDummy).render(chestDummy, delta, poseStack, multiBufferSource, light, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();

        }
    }
}
