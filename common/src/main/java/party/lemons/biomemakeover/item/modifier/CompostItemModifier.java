package party.lemons.biomemakeover.item.modifier;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ComposterBlock;

public record CompostItemModifier(float chance) implements ItemModifier {
    public static CompostItemModifier create(float chance)
    {
        return new CompostItemModifier(chance);
    }

    @Override
    public void accept(Item item) {
        ComposterBlock.COMPOSTABLES.put(item, chance);
    }
}
