package party.lemons.biomemakeover.entity.event;

import net.minecraft.world.entity.Entity;
import party.lemons.biomemakeover.network.S2C_EntityEvent;

public interface EntityEventBroadcaster
{
    default void overrideEvent(EntityEvent event){

    }

    default boolean doesOverrideEvent(EntityEvent event){
        return false;
    }

    default void broadcastEvent(Entity entity, EntityEvent event){
        new S2C_EntityEvent(entity, event).sendToChunkListeners(entity.level().getChunkAt(entity.getOnPos()));
    }
}
