package party.lemons.biomemakeover.crafting.grinding;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import party.lemons.biomemakeover.init.BMCrafting;
import party.lemons.taniwha.util.collections.WeightedList;

public class GrindingRecipe implements Recipe<Container>
{
    private static final RandomSource RANDOM = RandomSource.create();
    protected final Ingredient ingredient;
    protected final WeightedList<ItemStack> results;
    private final ItemStack baseResult;
    protected final ResourceLocation id;
    protected final String group;

    public GrindingRecipe(ResourceLocation resourceLocation, String string, Ingredient ingredient, ItemStack baseResult, WeightedList<ItemStack> results)
    {
        this.id = resourceLocation;
        this.group = string;
        this.ingredient = ingredient;
        this.baseResult = baseResult;
        this.results = results;
    }

    @Override
    public RecipeType<?> getType() {
        return BMCrafting.GRINDING.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BMCrafting.GRINDING_SERIALIZER.get();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonNullList = NonNullList.create();
        nonNullList.add(this.ingredient);
        return nonNullList;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return baseResult;
    }

    public WeightedList<ItemStack> getResults() {
        return results;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess)
    {
        ItemStack stack = results.sample(RANDOM);

        return stack.copy();
    }

    public static class Serializer implements RecipeSerializer<GrindingRecipe> {
        final GrindingFactory factory;

        public Serializer(GrindingFactory singleItemMaker) {
            this.factory = singleItemMaker;
        }

        @Override
        public GrindingRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            String group = GsonHelper.getAsString(json, "group", "");

            Ingredient ingredient;
            if (GsonHelper.isArrayNode(json, "ingredient")) {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient"), false);
            } else {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"), false);
            }

            WeightedList<ItemStack> results = new WeightedList<>();
            ItemStack baseResult = null;
            if(json.has("result"))
            {
                String resultID = GsonHelper.getAsString(json, "result");
                int count = GsonHelper.getAsInt(json, "count", 1);
                ItemStack itemStack = new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(resultID)), count);

                baseResult = itemStack;
                results.add(itemStack, 1);
            }
            else {
                JsonArray resultArray = json.getAsJsonArray("results");
                for(int i = 0; i < resultArray.size(); i++)
                {
                    JsonObject resultObj = resultArray.get(i).getAsJsonObject();
                    String resultID = GsonHelper.getAsString(resultObj, "item");
                    int count = GsonHelper.getAsInt(resultObj, "count", 1);
                    int weight = GsonHelper.getAsInt(resultObj, "weight", 1);
                    ItemStack itemStack = new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(resultID)), count);

                    results.add(itemStack, weight);

                    if(i == 0)
                        baseResult = itemStack;
                }
            }

            return factory.create(id, group, ingredient, baseResult, results);
        }

        @Override
        public GrindingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String group = buf.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            int count = buf.readInt();

            WeightedList<ItemStack> results = new WeightedList<>();
            ItemStack baseItem = null;
            for(int i = 0; i < count; i++)
            {
                int weight = buf.readInt();
                ItemStack stack = buf.readItem();

                if(i == 0)
                    baseItem = stack;

                results.add(stack, weight);
            }

            return this.factory.create(id, group, ingredient, baseItem, results);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GrindingRecipe recipe) {
            buf.writeUtf(recipe.group);
            recipe.ingredient.toNetwork(buf);

            buf.writeInt(recipe.results.getEntries().size());
            for(WeightedList.Entry<ItemStack> entry : recipe.results.getEntries())
            {
                buf.writeInt(entry.weight());
                buf.writeItem(entry.object());
            }
        }

        public interface GrindingFactory {
            GrindingRecipe create(ResourceLocation id, String group, Ingredient ingredient, ItemStack baseResult, WeightedList<ItemStack> results);
        }
    }
}