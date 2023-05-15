package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;
import party.lemons.taniwha.block.TBlockExtension;
import party.lemons.taniwha.block.modifier.BlockModifier;
import party.lemons.taniwha.block.modifier.BlockWithModifiers;
import party.lemons.taniwha.registry.ModifierContainer;

public class FloweredWaterlilyPadBlock extends WaterlilyBlock implements BlockWithModifiers<FloweredWaterlilyPadBlock>, TBlockExtension
{
    public FloweredWaterlilyPadBlock(Properties properties) {
        super(properties);
    }

    private ModifierContainer<Block> modifierContainer;

    @Override
    public FloweredWaterlilyPadBlock modifiers(BlockModifier... modifiers)
    {
        modifierContainer = new ModifierContainer<>(this, modifiers);
        return this;
    }

    @Override
    public @Nullable ModifierContainer<Block> getModifierContainer()
    {
        return modifierContainer;
    }

    @Override
    public BlockPathTypes getNodePathType()
    {
        return BlockPathTypes.DANGER_OTHER;
    }
}
