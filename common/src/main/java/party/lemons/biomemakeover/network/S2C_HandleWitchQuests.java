package party.lemons.biomemakeover.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import party.lemons.biomemakeover.crafting.witch.WitchQuestList;
import party.lemons.biomemakeover.crafting.witch.menu.WitchMenu;
import party.lemons.biomemakeover.init.BMNetwork;

public class S2C_HandleWitchQuests extends BaseS2CMessage {

    private int index;
    private WitchQuestList quests;

    public S2C_HandleWitchQuests(int index, WitchQuestList quests)
    {
        this.index = index;
        this.quests = quests;
    }

    public S2C_HandleWitchQuests(FriendlyByteBuf buf)
    {
        index = buf.readInt();
        quests = new WitchQuestList(buf);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(index);
        quests.toPacket(buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(()->{
            AbstractContainerMenu menu = Minecraft.getInstance().player.containerMenu;

            if(index == menu.containerId && menu instanceof WitchMenu)
                ((WitchMenu)menu).setQuests(quests);
        });
    }

    @Override
    public MessageType getType() {
        return BMNetwork.WITCH_QUESTS;
    }
}
