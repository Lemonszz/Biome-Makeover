package party.lemons.biomemakeover.crafting.witch.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.crafting.witch.QuestRarity;
import party.lemons.biomemakeover.crafting.witch.WitchQuest;
import party.lemons.biomemakeover.crafting.witch.WitchQuestList;
import party.lemons.biomemakeover.init.BMNetwork;

public class WitchScreen extends HandledScreen<WitchScreenHandler>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/gui/witch.png");
	private static final Text QUESTS_TEXT = new TranslatableText("witch.quests");
	private static final Text SEPARATOR = new LiteralText(" - ");
	private static final Text DEPRECATED_TEXT = new TranslatableText("merchant.deprecated");
	private final QuestButton[] questButtons = new QuestButton[3];

	public WitchScreen(WitchScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.backgroundWidth = 174;
		this.backgroundHeight = 181;
		this.playerInventoryTitleX = 110;
		this.playerInventoryTitleY = this.backgroundHeight - 92;
	}

	private void clickQuest(int index, WitchQuest quest)
	{
		if(quest.hasItems(client.player.inventory))
		{
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			buf.writeVarInt(index);
			ClientPlayNetworking.send(BMNetwork.CL_COMPLETE_QUEST, buf);
		}
	}

	protected void init()
	{
		super.init();
		updateQuests();
	}

	protected void updateQuests()
	{
		for(int i  = 0; i < questButtons.length; i++)
		{
			buttons.remove(questButtons[i]);
			children.remove(questButtons[i]);
			questButtons[i] = null;
		}

		int xx = (3 + (this.width - this.backgroundWidth) / 2);
		int j = (this.height - this.backgroundHeight) / 2;
		int yy = j + 16 + 2;

		WitchQuestList quests = getScreenHandler().getQuests();
		for(int i = 0; i < quests.size(); i++)
		{
			final int index = i;
			this.questButtons[i] = addButton(new QuestButton(xx, yy + (i * 26), quests.get(index), (b)->clickQuest(index, quests.get(index))));
		}
	}



	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		this.textRenderer.draw(matrices, this.title, (float)(49 + this.backgroundWidth / 2 - this.textRenderer.getWidth(this.title) / 2), 6.0F, 4210752);

		this.textRenderer.draw(matrices, this.playerInventory.getDisplayName(), (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 4210752);
		int l = this.textRenderer.getWidth(QUESTS_TEXT);
		this.textRenderer.draw(matrices, QUESTS_TEXT, (float)(5 - l / 2 + 48), 6.0F, 4210752);
	}

	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(TEXTURE);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		drawTexture(matrices, i, j, this.getZOffset(), 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 512);
		WitchQuestList tradeOfferList = this.handler.getQuests();
		if (!tradeOfferList.isEmpty())
		{

		}

	}


	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);

		for(QuestButton questButton : questButtons)
		{
			if (questButton != null && questButton.isHovered()) {
				questButton.renderToolTip(matrices, mouseX, mouseY);
			}
		}

	}

	class QuestButton extends ButtonWidget
	{
		private final WitchQuest quest;
		private final QuestRarity questRarity;

		public QuestButton(int x, int y, WitchQuest quest, PressAction action)
		{
			super(x, y, 104, 26, LiteralText.EMPTY, action);
			this.quest = quest;
			this.questRarity = QuestRarity.getRarityFromQuest(quest);
		}


		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta)
		{
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			minecraftClient.getTextureManager().bindTexture(TEXTURE);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			int drawTextureIndex = 1;
			if(quest.hasItems(MinecraftClient.getInstance().player.inventory))
			{
				drawTextureIndex++;
				if(isHovered())
					drawTextureIndex++;
			}
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			this.drawTexture(matrices, this.x, this.y, 174, drawTextureIndex * 26, this.width, this.height, 512, 256);

			int rarityY = 7 + (questRarity.ordinal() * 5);
			this.drawTexture(matrices, x + 4, y + 11, 278, rarityY, 5, 5, 512, 256);

			int itemXX = x + 11;
			ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
			for(ItemStack stack : quest.getRequiredItems())
			{
				RenderSystem.translatef(0.0F, 0.0F, 32.0F);
				this.setZOffset(200);
				itemRenderer.zOffset = 200.0F;
				itemRenderer.renderInGuiWithOverrides(stack, itemXX, y + 5);
				itemRenderer.renderGuiItemOverlay(minecraftClient.textRenderer, stack, itemXX, y + 5, String.valueOf(stack.getCount()));

				itemXX += 18;
			}

			RenderSystem.translatef(0.0F, 0.0F, 32.0F);


			this.setZOffset(0);
			itemRenderer.zOffset = 0.0F;
		}

		public void renderToolTip(MatrixStack matrices, int mouseX, int mouseY) {
			int xx = mouseX - x;
			int yy = mouseY - y;

			if(hovered && xx > 5 && yy < 19)
			{
				if(xx > 2 && xx < 9)
					renderTooltip(matrices, questRarity.getTooltipText(), mouseX, mouseY);

				if(xx > 11)
				{
					xx -= 11;
					int index = xx / 18;
					if(index < quest.getRequiredItems().length)
					{
						int bgX = x + 11 + (index * 18);
						int bgY = y + 5;

						RenderSystem.colorMask(true, true, true, false);
						this.fillGradient(matrices, bgX, bgY, bgX + 16, bgY + 16, -2130706433, -2130706433);
						RenderSystem.colorMask(true, true, true, true);

						renderTooltip(matrices, quest.getRequiredItems()[index], mouseX, mouseY);
					}
				}
			}
		}
	}
}
