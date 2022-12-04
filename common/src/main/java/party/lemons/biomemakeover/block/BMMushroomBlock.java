package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HugeMushroomBlock;
import party.lemons.taniwha.block.modifier.BlockModifier;
import party.lemons.taniwha.block.modifier.BlockWithModifiers;
import party.lemons.taniwha.registry.ModifierContainer;

public class BMMushroomBlock extends HugeMushroomBlock implements BlockWithModifiers<BMMushroomBlock>
{
    private ModifierContainer<Block> modifierContainer;
    public BMMushroomBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public BMMushroomBlock modifiers(BlockModifier... modifiers) {
        modifierContainer = new ModifierContainer<>(this, modifiers);
        return this;
    }

    @Override
    public ModifierContainer<Block> getModifierContainer() {
        return modifierContainer;
    }
}