package party.lemons.biomemakeover.block;

public class NoItemBMBlock extends BMBlock
{
    public NoItemBMBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasItem() {
        return false;
    }
}
