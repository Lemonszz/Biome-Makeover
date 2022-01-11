package party.lemons.biomemakeover.util.access;

import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public interface CarvedPumpkinAccess {
    Predicate<BlockState> bm_isGolemHeadBlock();
}
