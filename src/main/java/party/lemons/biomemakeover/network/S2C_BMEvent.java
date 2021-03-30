package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import party.lemons.biomemakeover.util.effect.BiomeMakeoverEvent;

public class S2C_BMEvent implements ClientPlayNetworking.PlayChannelHandler
{
	@Override
	public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender)
	{
		BiomeMakeoverEvent event = BiomeMakeoverEvent.values()[buf.readVarInt()];
		BlockPos pos = buf.readBlockPos();

		client.execute(()->
		{
			event.execute(client.world, pos);
		});
	}
}
