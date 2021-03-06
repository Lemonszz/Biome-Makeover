package party.lemons.biomemakeover.block.blockentity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.gui.DirectionalDataScreenHandler;
import party.lemons.biomemakeover.init.BMBlockEntities;

public class DirectionalDataBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory
{
	private String metadata = "";

	public DirectionalDataBlockEntity()
	{
		super(BMBlockEntities.DIRECTIONAL_DATA);
	}

	public String getMetadata() {
		return this.metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putString("metadata", this.metadata);
		return tag;
	}

	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.metadata = tag.getString("metadata");
	}

	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf)
	{
		buf.writeBlockPos(pos);
		buf.writeString(metadata);
	}

	@Override
	public Text getDisplayName()
	{
		return new TranslatableText(getCachedState().getBlock().getTranslationKey());
	}

	@Override
	public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
	{
		return new DirectionalDataScreenHandler(syncId, inv, pos, metadata);
	}
}
