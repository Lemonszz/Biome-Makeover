package party.lemons.biomemakeover.crafting.witch.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
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
        this.imageHeight = 182;
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
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY)
    {
        g.drawString(this.font, this.title, (49 + this.imageWidth / 2 - this.font.width(this.title) / 2), 6, 4210752, false);
        g.drawString(this.font, this.inventory.getDisplayName(), this.inventoryLabelX, this.inventoryLabelY, 4210752, false);


        int l = this.font.width(QUESTS_TEXT);
        g.drawString(this.font, QUESTS_TEXT, (5 - l / 2 + 48), 6, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics g, float delta, int mouseX, int mouseY)
    {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        g.blit(TEXTURE, k, l, 0, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 512, 256);
    }

    @Override
    public void render(GuiGraphics matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.renderTooltip(matrices, mouseX, mouseY);

        for(QuestButton questButton : questButtons)
        {
            if(questButton != null && questButton.isHoveredOrFocused())
            {
                //TODO:PORT
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
            super(x, y, 104, 26, Component.empty(), action, Button.DEFAULT_NARRATION);
            this.quest = quest;
            this.questRarity = QuestRarity.getRarityFromQuest(quest);
        }

        @Override
        public void renderWidget(GuiGraphics g, int x, int y, float delta)
        {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            int drawTextureIndex = 1;
            if(quest.hasItems(Minecraft.getInstance().player.getInventory()))
            {
                drawTextureIndex++;
                if(isHoveredOrFocused()) drawTextureIndex++;
            }
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            g.blit(TEXTURE, this.getX(), this.getY(), 174, drawTextureIndex * 26, this.width, this.height, 512, 256);

            int rarityY = 7 + (questRarity.ordinal() * 5);
            g.blit(TEXTURE, getX() + 4, getY() + 11, 278, rarityY, 5, 5, 512, 256);

            int itemXX = getX() + 11;
            ItemRenderer itemRenderer = minecraft.getItemRenderer();
            for(ItemStack stack : quest.getRequiredItems())
            {
                g.renderItem(stack, itemXX, getY() + 5);
                g.renderItemDecorations(minecraft.font,stack, itemXX, getY() + 5, String.valueOf(stack.getCount()));
                itemXX += 18;
            }
        }

        //TODO:PORT
        public void renderToolTip(GuiGraphics g, int mouseX, int mouseY) {
            int xx = mouseX - getX();
            int yy = mouseY - getY();

            if(isHoveredOrFocused() && xx > 5 && yy < 19)
            {
                if(xx > 2 && xx < 9) g.renderTooltip(font, questRarity.getTooltipText(), mouseX, mouseY);

                if(xx > 11)
                {
                    g.pose().pushPose();
                    g.pose().translate(0.0, 0.0, 400.0f);

                    xx -= 11;
                    int index = xx / 18;
                    if(index < quest.getRequiredItems().length)
                    {
                        int bgX = getX() + 11 + (index * 18);
                        int bgY = getY() + 5;

                        RenderSystem.colorMask(true, true, true, false);
                        g.fillGradient(bgX, bgY, bgX + 16, bgY + 16, -2130706433, -2130706433);
                        RenderSystem.colorMask(true, true, true, true);

                        g.renderTooltip(font, quest.getRequiredItems()[index], mouseX, mouseY);
                    }
                    g.pose().popPose();
                }
            }
        }

    }
}