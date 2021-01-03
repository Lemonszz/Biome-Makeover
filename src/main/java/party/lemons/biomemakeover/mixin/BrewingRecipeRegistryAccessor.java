package party.lemons.biomemakeover.mixin;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BrewingRecipeRegistry.class)
public interface BrewingRecipeRegistryAccessor
{
	@Invoker("registerPotionRecipe")
	static void registerPotionRecipe(Potion input, Item item, Potion output){

	}
}
