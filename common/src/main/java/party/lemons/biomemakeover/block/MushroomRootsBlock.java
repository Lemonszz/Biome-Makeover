package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RootsBlock;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.taniwha.block.modifier.BlockModifier;
import party.lemons.taniwha.block.modifier.BlockWithModifiers;
import party.lemons.taniwha.registry.ModifierContainer;

public class MushroomRootsBlock extends RootsBlock implements BlockWithModifiers<MushroomRootsBlock>
{
    private ModifierContainer<Block> modifierContainer;

    public MushroomRootsBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.is(Blocks.MYCELIUM) || super.mayPlaceOn(floor, world, pos);
    }

    @Override
    public MushroomRootsBlock modifiers(BlockModifier... modifiers) {
        modifierContainer = new ModifierContainer<>(this, modifiers);
        return this;
    }

    @Override
    public ModifierContainer<Block> getModifierContainer() {
        return modifierContainer;
    }
}
