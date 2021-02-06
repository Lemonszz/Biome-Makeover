package party.lemons.biomemakeover.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.crafting.itemgroup.ItemTab;

public class ItemGroupTabWidget extends ButtonWidget
{
	private final ItemTab tab;
	public boolean isSelected = false;

	public ItemGroupTabWidget(int x, int y, ItemTab tab, PressAction onPress)
	{
		super(x, y, 22, 22, new TranslatableText(tab.getTranslationKey()), onPress);

		this.tab = tab;
	}

	protected int getYImage(boolean isHovered)
	{
		return isHovered || isSelected ? 1 : 0;
	}

	public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float delta)
	{
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		minecraftClient.getTextureManager().bindTexture(TEXTURE);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
		int i = this.getYImage(this.isHovered());

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);

		this.drawTexture(matrixStack, this.x, this.y, 0, i * height, this.width, this.height);
		this.renderBg(matrixStack, minecraftClient, mouseX, mouseY);

		minecraftClient.getItemRenderer().renderInGui(tab.getIcon(), this.x + 3, this.y + 3);
	}

	public static final Identifier TEXTURE = BiomeMakeover.ID("textures/gui/tabs.png");
}