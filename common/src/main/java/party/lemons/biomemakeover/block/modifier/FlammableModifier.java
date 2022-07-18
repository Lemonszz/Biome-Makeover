package party.lemons.biomemakeover.block.modifier;

import net.minecraft.world.level.block.Block;
import party.lemons.biomemakeover.util.FlammabilityRegistry;

public record FlammableModifier(int catchOdds, int burnOdds) implements BlockModifier
{
    public static final FlammableModifier WOOD = create(5, 20);
    public static final FlammableModifier LEAVES = create(30, 60);
    public static final FlammableModifier TALL_FLOWER = create(60, 100);
    public static final FlammableModifier IVY = create(60, 50);

    public static FlammableModifier create(int catchOdds, int burnOdds) {
        return new FlammableModifier(catchOdds, burnOdds);
    }

    @Override
    public void accept(Block block) {
        FlammabilityRegistry.setBlockFlammable(block, catchOdds, burnOdds);

        //CATCH_ODDS.put(block, catchOdds);
        //BURN_ODDS.put(block, burnOdds);
    }

    //Used in wiki generation which is broken so may as well remove these lmao
    //Also why didn't i just query the normal way instead of caching it here idiot
    //public static Map<Block, Integer> CATCH_ODDS = Maps.newHashMap();
    //public static Map<Block, Integer> BURN_ODDS = Maps.newHashMap();
}