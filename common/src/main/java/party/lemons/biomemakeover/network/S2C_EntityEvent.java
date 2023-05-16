package party.lemons.biomemakeover.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import party.lemons.biomemakeover.entity.event.EntityEvent;
import party.lemons.biomemakeover.entity.event.EntityEventBroadcaster;
import party.lemons.biomemakeover.init.BMNetwork;

public class S2C_EntityEvent extends BaseS2CMessage {

    private final int entityID, event;

    public S2C_EntityEvent(Entity entity, EntityEvent event)
    {
        this(entity.getId(), event.ordinal());
    }

    public S2C_EntityEvent(int entityID, int event)
    {
        this.entityID = entityID;
        this.event = event;
    }

    public S2C_EntityEvent(FriendlyByteBuf buf)
    {
        this(buf.readInt(), buf.readInt());
    }

    @Override
    public MessageType getType() {
        return BMNetwork.ENTITY_EVENT;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeInt(event);
    }

    @Override
    public void handle(NetworkManager.PacketContext context)
    {
        context.queue(()->{
            Entity e = context.getPlayer().level().getEntity(entityID);
            EntityEvent ev = EntityEvent.values()[event];
            if(e == null)
                return;

            if(e instanceof EntityEventBroadcaster broadcaster)
            {
                if(broadcaster.doesOverrideEvent(ev))
                {
                    broadcaster.overrideEvent(ev);
                    return;
                }
            }
            ev.execute(e);
        });
    }
}
