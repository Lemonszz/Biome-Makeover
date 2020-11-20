package party.lemons.biomemakeover.util.boat;

import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class BoatType
{
	public final Identifier id;
	public final Supplier<ItemConvertible> item;

	public BoatType(Identifier id, Supplier<ItemConvertible> item)
	{
		this.id = id;
		this.item = item;
	}

	public Identifier getTexture()
	{
		return new Identifier(id.getNamespace(), "textures/entity/boat/" + id.getPath() + ".png");
	}
}