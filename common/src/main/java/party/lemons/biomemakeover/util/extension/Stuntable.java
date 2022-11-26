package party.lemons.biomemakeover.util.extension;

import net.minecraft.world.entity.AgeableMob;

public interface Stuntable
{
    boolean isStunted();

    void setStunted(boolean stunted);

    static void setStunted(AgeableMob entity, boolean stunted)
    {
        ((Stuntable)entity).setStunted(stunted);
    }

	default boolean isAlwaysBaby()
    {
        return false;
    }
}