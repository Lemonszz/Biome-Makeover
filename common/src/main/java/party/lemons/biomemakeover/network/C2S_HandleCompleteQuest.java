package party.lemons.biomemakeover.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import party.lemons.biomemakeover.crafting.witch.WitchQuest;
import party.lemons.biomemakeover.crafting.witch.WitchQuestHandler;
import party.lemons.biomemakeover.crafting.witch.WitchQuestList;
import party.lemons.biomemakeover.crafting.witch.menu.WitchMenu;
import party.lemons.biomemakeover.crafting.witch.menu.WitchScreen;
import party.lemons.biomemakeover.init.BMNetwork;

public class C2S_HandleCompleteQuest extends BaseC2SMessage
{
    private int index;

    public C2S_HandleCompleteQuest(int index)
    {
        this.index = index;
    }

    public C2S_HandleCompleteQuest(FriendlyByteBuf buf)
    {
        this.index = buf.readInt();
    }

    @Override
    public MessageType getType() {
        return BMNetwork.CL_COMPLETE_QUEST;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(index);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(()->{
            if(context.getPlayer().containerMenu instanceof WitchMenu)
            {
                WitchMenu screen = (WitchMenu) context.getPlayer().containerMenu;
                WitchQuestList quests = screen.getQuests();
                if(index >= 0 && index < quests.size())
                {
                    WitchQuest quest = quests.get(index);
                    screen.completeQuest(context.getPlayer(), quest);

                    WitchQuestHandler.sendQuests(context.getPlayer(), screen.containerId, screen.getQuests());
                }
            }
        });
    }
}
