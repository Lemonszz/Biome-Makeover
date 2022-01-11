package party.lemons.biomemakeover.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import party.lemons.biomemakeover.init.BMNetwork;
import party.lemons.biomemakeover.util.extension.Slideable;

public class S2C_SyncSlide extends BaseS2CMessage {
    private int slideTime;

    public S2C_SyncSlide(int slideTime)
    {
        this.slideTime = slideTime;
    }

    public S2C_SyncSlide(FriendlyByteBuf buf)
    {
        this(buf.readInt());
    }

    @Override
    public MessageType getType() {
        return BMNetwork.SET_SLIDE_TIME;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(slideTime);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(()->{
            if(context.getPlayer() != null)
                ((Slideable)context.getPlayer()).setSlideTime(slideTime);
        });
    }
}
