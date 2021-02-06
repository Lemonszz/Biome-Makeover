package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import party.lemons.biomemakeover.util.SlideEntity;

public class S2C_SetSlideTime implements ClientPlayNetworking.PlayChannelHandler
{
	@Override
	public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender)
	{
		int time = buf.readVarInt();

		client.execute(()->
		{
			((SlideEntity) client.player).setSlideTime(time);
		});
	}
}
