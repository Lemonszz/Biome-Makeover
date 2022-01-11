package party.lemons.biomemakeover.util.access;

import net.minecraft.world.level.block.Block;

/**
 * Used to allow modded blocks to use vanilla blockentitytypes
 */
public interface BlockEntityTypeAccess
{
    void bm_addBlockTypes(Block... toAdd);
}
