package party.lemons.biomemakeover.util.extension;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.boss.EnderDragonPart;
import party.lemons.biomemakeover.entity.mutipart.EntityPart;

public interface MultipartHolder
{
    Int2ObjectMap<EntityPart<?>> getPartMap();
}
