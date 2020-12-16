package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import party.lemons.biomemakeover.crafting.witch.WitchQuestList;
import party.lemons.biomemakeover.crafting.witch.screen.WitchScreenHandler;

public class S2C_HandleWitchQuests implements PacketConsumer
{
	@Override
	public void accept(PacketContext packetContext, PacketByteBuf buf)
	{
		int syncId = buf.readVarInt();
		WitchQuestList witchQuests = new WitchQuestList(buf);

		packetContext.getTaskQueue().execute(()->{
			ScreenHandler screenHandler = MinecraftClient.getInstance().player.currentScreenHandler;


			if (syncId == screenHandler.syncId && screenHandler instanceof WitchScreenHandler)
			{
				((WitchScreenHandler)screenHandler).setQuests(witchQuests);
			}
		});

	}
}
