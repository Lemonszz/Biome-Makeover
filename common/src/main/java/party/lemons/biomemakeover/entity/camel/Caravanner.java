package party.lemons.biomemakeover.entity.camel;

import net.minecraft.world.entity.PathfinderMob;
import org.jetbrains.annotations.Nullable;

public interface Caravanner
{
    boolean isCaravanning();
    boolean canJoinCaravan();
    boolean isCaravanHead();
    @Nullable <E extends PathfinderMob & Caravanner> E getCaravanLeader();
    @Nullable <E extends PathfinderMob & Caravanner> E getCaravanTail();

    default boolean hasCaravanLeader(){
        return getCaravanLeader() != null;
    }

    default boolean hasCaravanTail(){
        return getCaravanTail() != null;
    }

    <E extends PathfinderMob & Caravanner> void followCaravan(E llama);

    void stopFollowingCaravan();
    <E extends PathfinderMob & Caravanner> void setTail(E tail);

    boolean canBeFollowed();

    boolean canBeLeader();
}
