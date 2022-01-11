package party.lemons.biomemakeover.mixin;

import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityTypeTags.class)
public interface EntityTypeTagsInvoker
{
    @Invoker
    static Tag.Named<EntityType<?>> callBind(String string) {
        throw new AssertionError();
    }
}
