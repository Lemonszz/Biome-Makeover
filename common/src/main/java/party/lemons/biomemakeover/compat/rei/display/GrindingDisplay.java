package party.lemons.biomemakeover.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import party.lemons.biomemakeover.compat.rei.BMRei;
import party.lemons.biomemakeover.crafting.grinding.GrindingRecipe;
import party.lemons.taniwha.util.collections.WeightedList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GrindingDisplay extends BasicDisplay {

    private final List<Float> percentChances;

    public GrindingDisplay(GrindingRecipe recipe)
    {
        super(EntryIngredients.ofIngredients(recipe.getIngredients()), recipe.getResults().getEntries().stream().map(e-> EntryIngredients.of(e.object())).toList(), Optional.of(recipe.getId()));
        percentChances = new ArrayList<>();

        for(WeightedList.Entry<ItemStack> entry : recipe.getResults().getEntries())
        {
            float percentage = (float)entry.weight() / (float)recipe.getResults().getTotalWeight();
            percentChances.add(percentage);
        }

    }
    public GrindingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Recipe<?> recipe, List<Float> percentChances)
    {
        super(inputs, outputs, Optional.ofNullable(recipe).map(Recipe::getId));
        this.percentChances = percentChances;
    }

    public GrindingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> id, CompoundTag tag)
    {
        super(inputs, outputs, id);

        percentChances = new ArrayList<>();
        ListTag percentages = tag.getList("Percentages", Tag.TAG_FLOAT);
        for(int i = 0; i < percentages.size(); i++)
        {
            percentChances.add(percentages.getFloat(i));
        }
    }


    public List<Float> getPercentChances() {
        return percentChances;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return BMRei.GRINDING_DISPLAY;
    }
}
