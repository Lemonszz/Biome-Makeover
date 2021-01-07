package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import party.lemons.biomemakeover.crafting.witch.WitchQuest;
import party.lemons.biomemakeover.crafting.witch.WitchQuestHandler;
import party.lemons.biomemakeover.crafting.witch.WitchQuestList;
import party.lemons.biomemakeover.crafting.witch.screen.WitchScreenHandler;

public class C2S_HandleCompleteQuest implements PacketConsumer
{
	@Override
	public void accept(PacketContext ctx, PacketByteBuf buf)
	{
		int index = buf.readVarInt();
		ctx.getTaskQueue().execute(()->{
			PlayerEntity playerEntity = ctx.getPlayer();

			if(playerEntity.currentScreenHandler instanceof WitchScreenHandler)
			{
				WitchScreenHandler screen = (WitchScreenHandler) playerEntity.currentScreenHandler;
				WitchQuestList quests = screen.getQuests();
				if(index >= 0 && index < quests.size())
				{
					WitchQuest quest = quests.get(index);
					screen.completeQuest(playerEntity, quest);

					WitchQuestHandler.sendQuests(playerEntity, screen.syncId, screen.getQuests());
				}
			}
		});
	}
}
