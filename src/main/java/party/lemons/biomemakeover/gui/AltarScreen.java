package party.lemons.biomemakeover.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;

import java.util.Random;

public class AltarScreen extends HandledScreen<AltarScreenHandler>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/gui/altar.png");
	private static final int[] GYLPH_PROGRESS = new int[]{0, 6, 11, 16, 20, 24, 29, 35, 42, 49, 54, 54, 54};
	private static final Identifier BOOK_TEXTURE = new Identifier("textures/entity/enchanting_table_book.png");
	private static final BookModel BOOK_MODEL = new BookModel();
	private final Random random = new Random();

	public int ticks;
	public float nextPageAngle;
	public float pageAngle;
	public float approximatePageAngle;
	public float pageRotationSpeed;
	public float nextPageTurningSpeed;
	public float pageTurningSpeed;
	private ItemStack stack = ItemStack.EMPTY;


	public AltarScreen(AltarScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		DiffuseLighting.disableGuiDepthLighting();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		client.getTextureManager().bindTexture(TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

		int progress = this.handler.getProgress();
		float progressPerc = (float)progress / AltarBlockEntity.MAX_TIME;
		if (progress > 0) {
			int n = (int)(28.0F * (1.0F - (float)progress / (float)AltarBlockEntity.MAX_TIME));
			if (n > 0) {
				int perc = (int)(progressPerc * 29F);
				this.drawTexture(matrices, x + 99, y + 55 - perc, 189, 29 - perc, 9, perc);
			}

			n = GYLPH_PROGRESS[progress / 2 % 13];
			if (n > 0) {
				this.drawTexture(matrices, x + 68, y + 16 + 53 - n, 177, 53 - n, 12, n);
			}
		}

		//Book

		RenderSystem.matrixMode(5889);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		int k = (int)this.client.getWindow().getScaleFactor();
		RenderSystem.viewport((this.width - 320) / 2 * k, (this.height - 240) / 2 * k, 320 * k, 240 * k);
		RenderSystem.translatef(-0.34F, 0.23F, 0.0F);
		RenderSystem.multMatrix(Matrix4f.viewboxMatrix(90.0D, 1.3333334F, 9.0F, 80.0F));
		RenderSystem.matrixMode(5888);
		matrices.push();
		MatrixStack.Entry entry = matrices.peek();
		entry.getModel().loadIdentity();
		entry.getNormal().loadIdentity();
		matrices.translate(0.0D, 2, 1984.0D);
		float f = 5.0F;
		matrices.scale(5.0F, 5.0F, 5.0F);
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
		matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(20.0F));
		float g = MathHelper.lerp(delta, this.pageTurningSpeed, this.nextPageTurningSpeed);
		matrices.translate((1.0F - g) * 0.2F, (1.0F - g) * 0.1F, (double)((1.0F - g) * 0.25F));
		float h = -(1.0F - g) * 90.0F - 90.0F;
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(h));
		matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));
		float l = MathHelper.lerp(delta, this.pageAngle, this.nextPageAngle) + 0.25F;
		float m = MathHelper.lerp(delta, this.pageAngle, this.nextPageAngle) + 0.75F;
		l = (l - (float)MathHelper.fastFloor(l)) * 1.6F - 0.3F;
		m = (m - (float)MathHelper.fastFloor(m)) * 1.6F - 0.3F;
		if (l < 0.0F) {
			l = 0.0F;
		}

		if (m < 0.0F) {
			m = 0.0F;
		}

		if (l > 1.0F) {
			l = 1.0F;
		}

		if (m > 1.0F) {
			m = 1.0F;
		}

		RenderSystem.enableRescaleNormal();
		BOOK_MODEL.setPageAngles(0.0F, l, m, g);
		VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
		VertexConsumer vertexConsumer = immediate.getBuffer(BOOK_MODEL.getLayer(BOOK_TEXTURE));
		BOOK_MODEL.render(matrices, vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		immediate.draw();
		matrices.pop();
		RenderSystem.matrixMode(5889);
		RenderSystem.viewport(0, 0, this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight());
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(5888);
		DiffuseLighting.enableGuiDepthLighting();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	protected void init() {
		super.init();
		//titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
	}

	@Override
	public void tick()
	{
		super.tick();
		doTick();
	}

	public void doTick() {
		ItemStack itemStack = this.handler.getSlot(0).getStack();
		if (!ItemStack.areEqual(itemStack, this.stack)) {
			this.stack = itemStack;

			do {
				this.approximatePageAngle += (float)(this.random.nextInt(4) - this.random.nextInt(4));
			} while(this.nextPageAngle <= this.approximatePageAngle + 1.0F && this.nextPageAngle >= this.approximatePageAngle - 1.0F);
		}

		++this.ticks;
		this.pageAngle = this.nextPageAngle;
		this.pageTurningSpeed = this.nextPageTurningSpeed;
		boolean bl = handler.getProgress() > 0;

		if (bl) {
			this.nextPageTurningSpeed += 0.2F;
		} else {
			this.nextPageTurningSpeed -= 0.2F;
		}

		this.nextPageTurningSpeed = MathHelper.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
		float f = (this.approximatePageAngle - this.nextPageAngle) * 0.4F;
		float g = 0.2F;
		f = MathHelper.clamp(f, -0.2F, 0.2F);
		this.pageRotationSpeed += (f - this.pageRotationSpeed) * 0.9F;
		this.nextPageAngle += this.pageRotationSpeed;
	}
}
