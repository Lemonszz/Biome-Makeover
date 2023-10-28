package party.lemons.biomemakeover.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.crafting.grinding.GrindingRecipe;

public class BMCrafting
{
    private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Constants.MOD_ID, Registries.RECIPE_TYPE);
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Constants.MOD_ID, Registries.RECIPE_SERIALIZER);
    public static final RegistrySupplier<RecipeType<GrindingRecipe>> GRINDING = TYPES.register(BiomeMakeover.ID("grinding"), ()->new RecipeType<GrindingRecipe>() {
        public String toString() {
            return Constants.MOD_ID + "_grinding";
        }
    });
    public static final RegistrySupplier<RecipeSerializer<GrindingRecipe>> GRINDING_SERIALIZER = SERIALIZERS.register(BiomeMakeover.ID("grinding"), ()-> new GrindingRecipe.Serializer(GrindingRecipe::new));

    public static void init()
    {
        TYPES.register();
        SERIALIZERS.register();
    }
}