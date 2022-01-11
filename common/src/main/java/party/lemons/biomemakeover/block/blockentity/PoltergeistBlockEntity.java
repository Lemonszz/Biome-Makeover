package party.lemons.biomemakeover.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.block.PoltergeistBlock;
import party.lemons.biomemakeover.init.BMBlockEntities;
import party.lemons.biomemakeover.level.PoltergeistHandler;

public class PoltergeistBlockEntity extends BlockEntity
{
    public PoltergeistBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BMBlockEntities.POLTERGEIST, blockPos, blockState);
    }

    public static <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState) {
        return (level1, blockPos, blockState1, blockEntity) -> {
            if (level1 != null && !level1.isClientSide() && blockState1.getValue(PoltergeistBlock.ENABLED))
                PoltergeistHandler.doPoltergeist(level1, blockPos, 5);
        };
    }
}
