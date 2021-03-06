package party.lemons.biomemakeover.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import party.lemons.biomemakeover.init.BMScreens;

public class DirectionalDataScreenHandler extends ScreenHandler
{
	public BlockPos pos;
	public String meta;

	public DirectionalDataScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf)
	{
		this(syncId, playerInventory, buf.readBlockPos(), buf.readString());
	}

	public DirectionalDataScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos, String data)
	{
		super(BMScreens.DIRECTIONAL_DATA, syncId);
		this.pos = pos;
		this.meta = data;
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return player.isCreative();
	}
}
