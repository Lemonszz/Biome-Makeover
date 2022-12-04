package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherSproutsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import party.lemons.taniwha.block.modifier.BlockModifier;
import party.lemons.taniwha.block.modifier.BlockWithModifiers;
import party.lemons.taniwha.registry.ModifierContainer;

public class MushroomSproutsBlock extends NetherSproutsBlock implements BlockWithModifiers<MushroomSproutsBlock>
{
    private ModifierContainer<Block> modifierContainer;


    public MushroomSproutsBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.is(Blocks.MYCELIUM) || super.mayPlaceOn(floor, world, pos);
    }

    @Override
    public MushroomSproutsBlock modifiers(BlockModifier... modifiers)
    {
        modifierContainer = new ModifierContainer<>(this, modifiers);
        return this;
    }

    @Override
    public @Nullable ModifierContainer<Block> getModifierContainer()
    {
        return modifierContainer;
    }
}