package party.lemons.biomemakeover.block.modifier;

import net.minecraft.world.level.block.Block;
import party.lemons.biomemakeover.init.BMBlocks;

public record CompostModifier(float chance) implements BlockModifier
{
    public static CompostModifier create(float chance) {
        return new CompostModifier(chance);
    }

    @Override
    public void accept(Block block) {
        BMBlocks.COMPOSTABLES.add(new CompostValue(block, chance));
    }


    /*
        Using a record to hold this info to register later because the block doesn't yet have it's item to run, and the
        composter only accepts items.
        This was the easiest way. Think you got a better way? Try it nerd.
     */
    public record CompostValue(Block block, float chance)
    {

    }
}
