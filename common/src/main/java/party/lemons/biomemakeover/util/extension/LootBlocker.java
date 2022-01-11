package party.lemons.biomemakeover.util.extension;

import net.minecraft.world.entity.Entity;

public interface LootBlocker
{
    static boolean isBlocked(Entity entity)
    {
        if(entity instanceof LootBlocker)
        {
            return ((LootBlocker) entity).isLootBlocked();
        }
        return false;
    }

    boolean isLootBlocked();
    void setLootBlocked(boolean block);
}