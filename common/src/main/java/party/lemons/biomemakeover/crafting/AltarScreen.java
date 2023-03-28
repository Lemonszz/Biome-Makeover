package party.lemons.biomemakeover.crafting;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;

import java.util.Random;

public class AltarScreen extends AbstractContainerScreen<AltarMenu> {
    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/gui/altar.png");
    private static final int[] GYLPH_PROGRESS = new int[]{0, 6, 11, 16, 20, 24, 29, 35, 42, 49, 54, 54, 54};
    private static final ResourceLocation BOOK_TEXTURE = new ResourceLocation("textures/entity/enchanting_table_book.png");
    private BookModel bookModel;
    private final Random random = new Random();

    public int ticks;
    public float nextPageAngle;
    public float pageAngle;
    public float approximatePageAngle;
    public float pageRotationSpeed;
    public float nextPageTurningSpeed;
    public float pageTurningSpeed;
    private ItemStack stack = ItemStack.EMPTY;


    public AltarScreen(AltarMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.bookModel = new BookModel(this.minecraft.getEntityModels().bakeLayer(ModelLayers.BOOK));
    }

    @Override
    protected void renderBg(PoseStack poseStack, float delta, int mouseX, int mouseY) {
        Lighting.setupForFlatItems();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        int progress = this.getMenu().getProgress();
        float progressPerc = (float) progress / AltarBlockEntity.MAX_TIME;
        if (progress > 0) {
            int n = (int) (28.0F * (1.0F - (float) progress / (float) AltarBlockEntity.MAX_TIME));
            if (n > 0) {
                int perc = (int) (progressPerc * 29F);
                this.blit(poseStack, x + 99, y + 55 - perc, 189, 29 - perc, 9, perc);
            }

            n = GYLPH_PROGRESS[progress / 2 % 13];
            if (n > 0) {
                this.blit(poseStack, x + 68, y + 16 + 53 - n, 177, 53 - n, 12, n);
            }
        }

        //Book

        int m = (int) this.minecraft.getWindow().getGuiScale();
        RenderSystem.viewport((this.width - 320) / 2 * m, (this.height - 240) / 2 * m, 320 * m, 240 * m);
        Matrix4f matrix4f = new Matrix4f().translation(-0.34F, 0.23F, 0.0F).perspective((float) (Math.PI / 2), 1.3333334F, 9.0F, 80.0F);

        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix(matrix4f);
        poseStack.pushPose();
        poseStack.setIdentity();

        poseStack.translate(0.0, 2, 1984.0);
        float scale = 5.0f;
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(20.0f));
        float h = Mth.lerp(delta, this.pageTurningSpeed, this.nextPageTurningSpeed);
        poseStack.translate((1.0f - h) * 0.2f, (1.0f - h) * 0.1f, (1.0f - h) * 0.25f);
        float n = -(1.0f - h) * 90.0f - 90.0f;
        poseStack.mulPose(Axis.YP.rotationDegrees(n));
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0f));
        float o = Mth.lerp(delta, this.pageAngle, this.nextPageAngle) + 0.25f;
        float p = Mth.lerp(delta, this.pageAngle, this.nextPageAngle) + 0.75f;
        o = (o - (float) Mth.floor(o)) * 1.6f - 0.3f;
        p = (p - (float) Mth.floor(p)) * 1.6f - 0.3f;
        if (o < 0.0f) {
            o = 0.0f;
        }
        if (p < 0.0f) {
            p = 0.0f;
        }
        if (o > 1.0f) {
            o = 1.0f;
        }
        if (p > 1.0f) {
            p = 1.0f;
        }
        this.bookModel.setupAnim(0.0f, o, p, h);
        MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        VertexConsumer vertexConsumer = bufferSource.getBuffer(this.bookModel.renderType(BOOK_TEXTURE));
        this.bookModel.renderToBuffer(poseStack, vertexConsumer, 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        bufferSource.endBatch();
        poseStack.popPose();
        RenderSystem.viewport(0, 0, this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
        RenderSystem.restoreProjectionMatrix();
        Lighting.setupFor3DItems();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.renderTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        doTick();
    }

    public void doTick()
    {
        ItemStack itemStack = this.getMenu().getSlot(0).getItem();
        if(!ItemStack.isSame(itemStack, this.stack))
        {
            this.stack = itemStack;

            do
            {
                this.approximatePageAngle += (float) (this.random.nextInt(4) - this.random.nextInt(4));
            }while(this.nextPageAngle <= this.approximatePageAngle + 1.0F && this.nextPageAngle >= this.approximatePageAngle - 1.0F);
        }

        ++this.ticks;
        this.pageAngle = this.nextPageAngle;
        this.pageTurningSpeed = this.nextPageTurningSpeed;
        boolean bl = getMenu().getProgress() > 0;

        if(bl)
        {
            this.nextPageTurningSpeed += 0.2F;
        }else
        {
            this.nextPageTurningSpeed -= 0.2F;
        }

        this.nextPageTurningSpeed = Mth.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
        float f = (this.approximatePageAngle - this.nextPageAngle) * 0.4F;
        float range = 0.2F;
        f = Mth.clamp(f, -range, range);
        this.pageRotationSpeed += (f - this.pageRotationSpeed) * 0.9F;
        this.nextPageAngle += this.pageRotationSpeed;
    }

}
