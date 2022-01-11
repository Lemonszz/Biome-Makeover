package party.lemons.biomemakeover.mixin;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemTags.class)
public interface ItemTagsInvoker
{
    @Invoker
    static Tag.Named<Item> callBind(String string) {
        throw new AssertionError();
    }
}
