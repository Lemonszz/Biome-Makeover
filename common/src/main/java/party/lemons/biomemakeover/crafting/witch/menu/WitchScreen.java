package party.lemons.biomemakeover.crafting.witch.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.crafting.witch.QuestRarity;
import party.lemons.biomemakeover.crafting.witch.WitchQuest;
import party.lemons.biomemakeover.crafting.witch.WitchQuestList;
import party.lemons.biomemakeover.network.C2S_HandleCompleteQuest;

public class WitchScreen extends AbstractContainerScreen<WitchMenu>
{
    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/gui/witch.png");
    private static final Component QUESTS_TEXT = Component.translatable("witch.quests");
    private static final Component SEPARATOR = Component.literal(" - ");
    private static final Component DEPRECATED_TEXT = Component.translatable("merchant.deprecated");
    private final QuestButton[] questButtons = new QuestButton[3];
    private final Inventory inventory;

    public WitchScreen(WitchMenu handler, Inventory inventory, Component title)
    {
        super(handler, inventory, title);
        this.imageWidth = 174;
        this.imageHeight = 181;
        this.inventoryLabelX = 110;
        this.inventoryLabelY = this.imageHeight - 92;
        this.inventory = inventory;
    }

    private void clickQuest(int index, WitchQuest quest)
    {
        if(quest.hasItems(minecraft.player.getInventory()))
        {
            new C2S_HandleCompleteQuest(index).sendToServer();
        }
    }

    protected void init()
    {
        super.init();
        updateQuests();
    }

    protected void updateQuests()
    {
        for(int i = 0; i < questButtons.length; i++)
        {
            removeWidget(questButtons[i]);
            questButtons[i] = null;
        }

        int xx = (3 + (this.width - this.imageWidth) / 2);
        int j = (this.height - this.imageHeight) / 2;
        int yy = j + 16 + 2;

        WitchQuestList quests = getMenu().getQuests();
        for(int i = 0; i < quests.size(); i++)
        {
            final int index = i;
            this.questButtons[i] = addRenderableWidget(new QuestButton(xx, yy + (i * 26), quests.get(index), (b)->clickQuest(index, quests.get(index))));
        }
    }

    @Override
    protected void renderLabels(PoseStack matrices, int mouseX, int mouseY)
    {
        this.font.draw(matrices, this.title, (float) (49 + this.imageWidth / 2 - this.font.width(this.title) / 2), 6.0F, 4210752);

        this.font.draw(matrices, this.inventory.getDisplayName(), (float) this.inventoryLabelX, (float) this.inventoryLabelY, 4210752);
        int l = this.font.width(QUESTS_TEXT);
        this.font.draw(matrices, QUESTS_TEXT, (float) (5 - l / 2 + 48), 6.0F, 4210752);
    }

    @Override
    protected void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        MerchantScreen.blit(matrices, k, l, this.getBlitOffset(), 0.0f, 0.0f, this.imageWidth, this.imageHeight, 512, 256);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.renderTooltip(matrices, mouseX, mouseY);

        for(QuestButton questButton : questButtons)
        {
            if(questButton != null && questButton.isHoveredOrFocused())
            {
                questButton.renderToolTip(matrices, mouseX, mouseY);
            }
        }
    }

    class QuestButton extends Button
    {
        private final WitchQuest quest;
        private final QuestRarity questRarity;

        public QuestButton(int x, int y, WitchQuest quest, OnPress action)
        {
            super(x, y, 104, 26, Component.empty(), action);
            this.quest = quest;
            this.questRarity = QuestRarity.getRarityFromQuest(quest);
        }


        @Override
        public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta)
        {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderTexture(0, TEXTURE);
            int drawTextureIndex = 1;
            if(quest.hasItems(Minecraft.getInstance().player.getInventory()))
            {
                drawTextureIndex++;
                if(isHoveredOrFocused()) drawTextureIndex++;
            }
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            blit(matrices, this.x, this.y, 174, drawTextureIndex * 26, this.width, this.height, 512, 256);

            int rarityY = 7 + (questRarity.ordinal() * 5);
            blit(matrices, x + 4, y + 11, 278, rarityY, 5, 5, 512, 256);

            int itemXX = x + 11;
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            for(ItemStack stack : quest.getRequiredItems())
            {
                this.setBlitOffset(200);
                itemRenderer.blitOffset = 200.0F;
                itemRenderer.renderAndDecorateItem(stack, itemXX, y + 5);
                itemRenderer.renderGuiItemDecorations(minecraft.font, stack, itemXX, y + 5, String.valueOf(stack.getCount()));

                itemXX += 18;
            }

            this.setBlitOffset(0);
            itemRenderer.blitOffset = 0.0F;
        }

        @Override
        public void renderToolTip(PoseStack matrices, int mouseX, int mouseY) {
            int xx = mouseX - x;
            int yy = mouseY - y;

            if(isHoveredOrFocused() && xx > 5 && yy < 19)
            {
                if(xx > 2 && xx < 9) renderTooltip(matrices, questRarity.getTooltipText(), mouseX, mouseY);

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