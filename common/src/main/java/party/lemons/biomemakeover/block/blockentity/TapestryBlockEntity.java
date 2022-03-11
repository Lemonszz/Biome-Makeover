package party.lemons.biomemakeover.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.init.BMBlockEntities;

public class TapestryBlockEntity extends BlockEntity {
    public TapestryBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BMBlockEntities.TAPESTRY.get(), blockPos, blockState);
    }
}
