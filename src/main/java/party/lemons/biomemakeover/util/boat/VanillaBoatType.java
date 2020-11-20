package party.lemons.biomemakeover.util.boat;

import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class VanillaBoatType extends BoatType
{
	private BoatEntity.Type vanillaType;

	public VanillaBoatType(Identifier id, BoatEntity.Type type, Item boatitem)
	{
		super(id, ()->boatitem);
		this.vanillaType = type;
	}

	public BoatEntity.Type getVanillaType()
	{
		return vanillaType;
	}

	@Override
	public Identifier getTexture()
	{
		return new Identifier("minecraft", "textures/entity/boat/" + vanillaType.getName() + ".png");
	}
}