package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RootsBlock;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class MushroomRootsBlock extends RootsBlock implements BlockWithItem, BlockWithModifiers<MushroomRootsBlock> {
    public MushroomRootsBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.is(Blocks.MYCELIUM) || super.mayPlaceOn(floor, world, pos);
    }

    @Override
    public MushroomRootsBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);

        return this;
    }
}
