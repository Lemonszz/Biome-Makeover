package party.lemons.biomemakeover.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import party.lemons.biomemakeover.compat.rei.display.grinding.GrindingCategory;
import party.lemons.biomemakeover.compat.rei.display.grinding.GrindingDisplay;
import party.lemons.biomemakeover.crafting.grinding.GrindingRecipe;
import party.lemons.biomemakeover.init.BMCrafting;
import party.lemons.biomemakeover.init.BMItems;

public class BMReiClient extends BMRei implements REIClientPlugin
{
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new GrindingCategory());
        registry.addWorkstations(BMRei.GRINDING_DISPLAY, EntryStacks.of(BMItems.DUST_DEVIL_SPAWN_EGG.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(GrindingRecipe.class, BMCrafting.GRINDING.get(), GrindingDisplay::new);
    }
}
