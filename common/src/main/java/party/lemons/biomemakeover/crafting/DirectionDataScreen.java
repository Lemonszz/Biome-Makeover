package party.lemons.biomemakeover.crafting;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.block.blockentity.DirectionalDataBlockEntity;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.network.C2S_UpdateDirectionalData;

public class DirectionDataScreen extends AbstractContainerScreen<DirectionalDataMenu> {

    private EditBox inputMetadata;
    private String initialMeta;
    private Button buttonDone;
    private Button buttonCancel;
    private DirectionalDataBlockEntity block;

    public DirectionDataScreen(DirectionalDataMenu handler, Inventory inventory, Component component) {
        super(handler, inventory, component);

        this.minecraft = Minecraft.getInstance();
        BlockState state = minecraft.getInstance().level.getBlockState(handler.pos);
        if(!state.is(BMBlocks.DIRECTIONAL_DATA.get()))
        {
            this.minecraft.setScreen(null);
            return;
        }
        this.initialMeta = handler.meta;

        if(minecraft.level.getBlockEntity(handler.pos) instanceof DirectionalDataBlockEntity)
            block = (DirectionalDataBlockEntity) minecraft.level.getBlockEntity(handler.pos);
        else
            minecraft.setScreen(null);

        inventoryLabelX = -1000;
        titleLabelX = -1000;
    }

    @Override
    protected void init() {
        super.init();

        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.buttonDone = this.addRenderableWidget(new Button(this.width / 2 - 4 - 150, 210, 150, 20, CommonComponents.GUI_DONE, (buttonWidget) -> {
            this.done();
        }));
        this.buttonCancel = this.addRenderableWidget(new Button(this.width / 2 + 4, 210, 150, 20, CommonComponents.GUI_CANCEL, (buttonWidget) -> {
            this.cancel();
        }));

        this.inputMetadata = new EditBox(font, this.width / 2 - 152, 120, 308, 20, Component.translatable("structure_block.custom_data"));
        this.inputMetadata.setMaxLength(128);
        this.inputMetadata.setValue(initialMeta);

        addRenderableWidget(inputMetadata);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f, int i, int j) {
        renderBackground(poseStack);

    }

    @Override
    public void resize(Minecraft minecraft, int i, int j) {
        String meta = this.inputMetadata.getValue();
        this.init(minecraft, width, height);
        this.inputMetadata.setValue(meta);
    }

    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
    }

    @Override
    public void onClose() {
        this.cancel();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && this.shouldCloseOnEsc()) {
            this.onClose();
            return true;
        }else {
            return this.getFocused() != null && this.getFocused().keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        inputMetadata.tick();
    }

    private void cancel()
    {
        this.minecraft.setScreen(null);
    }

    private void done() {
        new C2S_UpdateDirectionalData(block.getBlockPos(), inputMetadata.getValue()).sendToServer();
        this.minecraft.setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
