package party.lemons.biomemakeover.util.registry.boat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class BoatType
{
    public final ResourceLocation id;
    public final Supplier<ItemLike> item;
    public final Supplier<ItemLike> chestItem;

    public BoatType(ResourceLocation id, Supplier<ItemLike> boatItem, Supplier<ItemLike> chestBoatItem)
    {
        this.id = id;
        this.item = boatItem;
        this.chestItem = chestBoatItem;

        BoatTypes.TYPES.add(this);
    }

    public ResourceLocation getTexture(boolean hasChest)
    {
        if(hasChest)
            return new ResourceLocation(id.getNamespace(), "textures/entity/boat/" + id.getPath() + "_chest.png");
        return new ResourceLocation(id.getNamespace(), "textures/entity/boat/" + id.getPath() + ".png");
    }

    public String getModelLocation()
    {
        return "boat/" + id.getPath();
    }

    public String getChestModelLocation()
    {
        return "chestboat/" + id.getPath();
    }
}