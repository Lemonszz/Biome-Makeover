package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;

public class BMFlowerPotBlock extends FlowerPotBlock implements BlockWithModifiers<BMFlowerPotBlock>
{
    public BMFlowerPotBlock(Block block, Properties properties) {
        super(block, properties);
    }
    @Override
    public BMFlowerPotBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);

        return this;
    }
}
