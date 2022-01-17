package party.lemons.biomemakeover.util.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import party.lemons.biomemakeover.network.S2C_BMEffect;

public class EffectHelper
{
    public static void doEffect(Level world, BiomeMakeoverEffect effect, BlockPos pos)
    {
        if(world.isClientSide())
            return;

        new S2C_BMEffect(effect.ordinal(), pos).sendToChunkListeners(world.getChunkAt(pos));
    }
}
