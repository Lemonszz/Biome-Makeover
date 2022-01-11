package party.lemons.biomemakeover.util.registry.boat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;

public class VanillaBoatType extends BoatType
{
    private final Boat.Type vanillaType;

    public VanillaBoatType(ResourceLocation id, Boat.Type type, Item boatitem)
    {
        super(id, ()->boatitem);
        this.vanillaType = type;
    }

    public Boat.Type getVanillaType()
    {
        return vanillaType;
    }

    @Override
    public ResourceLocation getTexture()
    {
        return new ResourceLocation("minecraft", "textures/entity/boat/" + vanillaType.getName() + ".png");
    }
}