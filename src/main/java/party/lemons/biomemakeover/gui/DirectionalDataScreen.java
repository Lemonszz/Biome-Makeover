package party.lemons.biomemakeover.gui;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import party.lemons.biomemakeover.block.blockentity.DirectionalDataBlockEntity;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMNetwork;

public class DirectionalDataScreen extends HandledScreen<DirectionalDataScreenHandler>
{
	private TextFieldWidget inputMetadata;
	private String initialMeta;
	private ButtonWidget buttonDone;
	private ButtonWidget buttonCancel;
	private DirectionalDataBlockEntity block;

	public DirectionalDataScreen(DirectionalDataScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		this.client = MinecraftClient.getInstance();
		BlockState state = client.getInstance().world.getBlockState(handler.pos);
		if(!state.isOf(BMBlocks.DIRECTIONAL_DATA))
		{
			this.client.openScreen(null);
			return;
		}
		this.initialMeta = handler.meta;

		if(client.world.getBlockEntity(handler.pos) instanceof DirectionalDataBlockEntity)
			block = (DirectionalDataBlockEntity) client.world.getBlockEntity(handler.pos);
		else
			client.openScreen(null);

		playerInventoryTitleX = -1000;
		titleX = -1000;
	}

	@Override
	protected void init()
	{
		this.client.keyboard.setRepeatEvents(true);
		this.buttonDone = this.addButton(new ButtonWidget(this.width / 2 - 4 - 150, 210, 150, 20, ScreenTexts.DONE, (buttonWidget) -> {
			this.done();
		}));
		this.buttonCancel = this.addButton(new ButtonWidget(this.width / 2 + 4, 210, 150, 20, ScreenTexts.CANCEL, (buttonWidget) -> {
			this.cancel();
		}));

		this.inputMetadata = new TextFieldWidget(this.textRenderer, this.width / 2 - 152, 120, 308, 20, new TranslatableText("structure_block.custom_data"));
		this.inputMetadata.setMaxLength(128);
		this.inputMetadata.setText(initialMeta);
		this.children.add(this.inputMetadata);

	}

	public void resize(MinecraftClient client, int width, int height) {

		String meta = this.inputMetadata.getText();
		this.init(client, width, height);
		this.inputMetadata.setText(meta);
	}

	public void removed() {
		this.client.keyboard.setRepeatEvents(false);
	}

	public void onClose() {
		this.cancel();
	}

	private void cancel()
	{
		this.client.openScreen(null);
	}

	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if (keyCode != 257 && keyCode != 335) {
			return false;
		} else {
			this.done();
			return true;
		}
	}

	@Override
	public void tick()
	{
		super.tick();
		inputMetadata.tick();

	}

	private void done() {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(block.getPos());
		buf.writeString(inputMetadata.getText());

		ClientPlayNetworking.send(BMNetwork.CL_UPDATE_DIR_DATA, buf);
		this.client.openScreen(null);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		super.render(matrices, mouseX, mouseY, delta);
		this.inputMetadata.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		renderBackground(matrices);
	}

	public boolean isPauseScreen() {
		return false;
	}
}
