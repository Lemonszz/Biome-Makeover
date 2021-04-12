package party.lemons.biomemakeover.util.access;

import net.minecraft.block.BlockState;

import java.util.function.Predicate;

public interface CarvedPumpkinBlockAccess
{
	Predicate<BlockState> bm_isGolemHeadBlock();
}
