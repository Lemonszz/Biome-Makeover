package party.lemons.biomemakeover.block;

import party.lemons.taniwha.block.types.TBlock;

public class NoItemBMBlock extends TBlock
{
    public NoItemBMBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasItem() {
        return false;
    }
}
