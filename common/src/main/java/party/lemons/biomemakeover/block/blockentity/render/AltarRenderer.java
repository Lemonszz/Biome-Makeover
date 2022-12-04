package party.lemons.biomemakeover.block.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;

public class AltarRenderer implements BlockEntityRenderer<AltarBlockEntity>
{
    public static final Material BOOK_LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/enchanting_table_book"));
    private final BookModel bookModel;

    public AltarRenderer(BlockEntityRendererProvider.Context context)
    {
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    @Override
    public void render(AltarBlockEntity blockEntity, float delta, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        float h;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.75, 0.5);
        float g = (float)blockEntity.ticks + delta;
        poseStack.translate(0.0, 0.1f + Mth.sin(g * 0.1f) * 0.01f, 0.0);
        for (h = blockEntity.currentAngle - blockEntity.lastAngle; h >= (float)Math.PI; h -= (float)Math.PI * 2) {
        }
        while (h < (float)(-Math.PI)) {
            h += (float)Math.PI * 2;
        }
        float k = blockEntity.lastAngle + h * delta;
        poseStack.mulPose(Axis.YP.rotation(-k));
        poseStack.mulPose(Axis.ZP.rotationDegrees(80.0f));
        float l = Mth.lerp(delta, blockEntity.pageAngle, blockEntity.nextPageAngle);
        float m = Mth.frac(l + 0.25f) * 1.6f - 0.3f;
        float n = Mth.frac(l + 0.75f) * 1.6f - 0.3f;
        float o = Mth.lerp(delta, blockEntity.pageTurningSpeed, blockEntity.nextPageTurningSpeed);
        this.bookModel.setupAnim(g, Mth.clamp(m, 0.0f, 1.0f), Mth.clamp(n, 0.0f, 1.0f), o);
        VertexConsumer vertexConsumer = BOOK_LOCATION.buffer(multiBufferSource, RenderType::entitySolid);
        this.bookModel.render(poseStack, vertexConsumer, i, j, 1.0f, 1.0f, 1.0f, 1.0f);
        poseStack.popPose();
    }
}
