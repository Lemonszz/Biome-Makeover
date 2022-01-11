package party.lemons.biomemakeover.mixin;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PotionBrewing.class)
public interface PotionBrewingInvoker {

    @Invoker
    static void callAddMix(Potion potion, Item item, Potion potion2) {
        throw new AssertionError();
    }

    @Invoker
    static void callAddContainerRecipe(Item item, Item item2, Item item3) {
        throw new AssertionError();
    }

    @Invoker
    static void callAddContainer(Item item) {
        throw new AssertionError();
    }
}
