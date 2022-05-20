package party.lemons.biomemakeover.util.registry.boat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class BoatType
{
    public final ResourceLocation id;
    public final Supplier<ItemLike> item;

    public BoatType(ResourceLocation id, Supplier<ItemLike> item)
    {
        this.id = id;
        this.item = item;

        BoatTypes.TYPES.add(this);
    }

    public ResourceLocation getTexture()
    {
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