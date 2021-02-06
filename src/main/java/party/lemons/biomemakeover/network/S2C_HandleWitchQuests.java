package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import party.lemons.biomemakeover.crafting.witch.WitchQuestList;
import party.lemons.biomemakeover.crafting.witch.screen.WitchScreenHandler;

public class S2C_HandleWitchQuests implements ClientPlayNetworking.PlayChannelHandler
{
	@Override
	public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender)
	{
		int syncId = buf.readVarInt();
		WitchQuestList witchQuests = new WitchQuestList(buf);

		client.execute(()->
		{
			ScreenHandler screenHandler = MinecraftClient.getInstance().player.currentScreenHandler;


			if(syncId == screenHandler.syncId && screenHandler instanceof WitchScreenHandler)
			{
				((WitchScreenHandler) screenHandler).setQuests(witchQuests);
			}
		});
	}
}
