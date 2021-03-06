package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import party.lemons.biomemakeover.block.blockentity.DirectionalDataBlockEntity;
import party.lemons.biomemakeover.crafting.witch.WitchQuest;
import party.lemons.biomemakeover.crafting.witch.WitchQuestHandler;
import party.lemons.biomemakeover.crafting.witch.WitchQuestList;
import party.lemons.biomemakeover.crafting.witch.screen.WitchScreenHandler;

public class C2S_UpdateDirectionalData implements ServerPlayNetworking.PlayChannelHandler
{
	@Override
	public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender)
	{
		BlockPos pos = buf.readBlockPos();
		String data = buf.readString(32767);

		if(!player.isCreative())
			return;

		server.execute(()->
		{
			World world = player.world;
			if(world.getBlockEntity(pos) instanceof DirectionalDataBlockEntity)
			{
				DirectionalDataBlockEntity be = (DirectionalDataBlockEntity) world.getBlockEntity(pos);
				be.setMetadata(data);

				BlockState state = world.getBlockState(pos);
				be.markDirty();
				world.updateListeners(pos, state, state, 3);
			}
		});
	}
}
