package party.lemons.biomemakeover.util.registry.boat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;

public class VanillaBoatType extends BoatType
{
    private final Boat.Type vanillaType;

    public VanillaBoatType(ResourceLocation id, Boat.Type type, Item boatitem, Item chestItem)
    {
        super(id, ()->boatitem, ()->chestItem);
        this.vanillaType = type;
    }

    public Boat.Type getVanillaType()
    {
        return vanillaType;
    }

    @Override
    public ResourceLocation getTexture(boolean chest)
    {
        if(chest)
            return new ResourceLocation("minecraft", "textures/entity/chest_boat/" + vanillaType.getName() + ".png");

        return new ResourceLocation("minecraft", "textures/entity/boat/" + vanillaType.getName() + ".png");
    }
}