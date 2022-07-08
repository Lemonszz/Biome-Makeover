package party.lemons.biomemakeover.init;

import com.google.common.collect.Maps;
import net.minecraft.world.level.block.state.properties.WoodType;
import party.lemons.biomemakeover.util.registry.sign.WoodTypeHelper;

import java.util.Collection;
import java.util.Map;

public class BMSigns
{
	private static final Map<String, WoodType> TYPES = Maps.newHashMap();

	public static final WoodType BLIGHTED_BALSA = createSignType("bm_blighted_balsa");
	public static final WoodType WILLOW = createSignType("bm_willow");
	public static final WoodType CYPRESS = createSignType("bm_swamp_cypress");
	public static final WoodType ANCIENT_OAK = createSignType("bm_ancient_oak");

	private static WoodType createSignType(String name)
	{
		WoodType type = WoodTypeHelper.register(name);
		TYPES.put(name, type);

		return type;
	}

	public static WoodType getSignType(String name)
	{
		return TYPES.get(name);
	}

	public static Collection<WoodType> getTypes()
	{
		return TYPES.values();
	}
}