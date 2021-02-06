package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import party.lemons.biomemakeover.crafting.witch.WitchQuest;
import party.lemons.biomemakeover.crafting.witch.WitchQuestHandler;
import party.lemons.biomemakeover.crafting.witch.WitchQuestList;
import party.lemons.biomemakeover.crafting.witch.screen.WitchScreenHandler;

public class C2S_HandleCompleteQuest implements ServerPlayNetworking.PlayChannelHandler
{
	@Override
	public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender)
	{
		int index = buf.readVarInt();
		server.execute(()->
		{

			if(player.currentScreenHandler instanceof WitchScreenHandler)
			{
				WitchScreenHandler screen = (WitchScreenHandler) player.currentScreenHandler;
				WitchQuestList quests = screen.getQuests();
				if(index >= 0 && index < quests.size())
				{
					WitchQuest quest = quests.get(index);
					screen.completeQuest(player, quest);

					WitchQuestHandler.sendQuests(player, screen.syncId, screen.getQuests());
				}
			}
		});
	}
}
