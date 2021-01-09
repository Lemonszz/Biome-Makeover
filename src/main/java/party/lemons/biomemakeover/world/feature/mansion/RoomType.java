package party.lemons.biomemakeover.world.feature.mansion;

import com.google.common.collect.Lists;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import party.lemons.biomemakeover.BiomeMakeover;

import java.util.List;
import java.util.Random;

public enum RoomType
{
	CORRIDOR(true, null),
	ROOM(false, MansionFeature.ROOMS),
	STAIRS_UP(false, MansionFeature.STAIR_UP),
	STAIRS_DOWN(false, MansionFeature.STAIR_DOWN),
	ROOF(false, null);

	public final boolean doorRequired;
	private final List<Identifier> templates;
	private RoomType(boolean doorRequired, List<Identifier> templates)
	{
		this.doorRequired = doorRequired;
		this.templates= templates;
	}

	public Identifier getRandomTemplate(Random random)
	{
		return templates.get(random.nextInt(templates.size()));
	}
}
