package party.lemons.biomemakeover.world.feature.mansion.room;

import net.minecraft.util.math.BlockPos;
import party.lemons.biomemakeover.world.feature.mansion.RoomType;

public class NonRoofedMansionRoom extends MansionRoom
{
	public NonRoofedMansionRoom(BlockPos position, RoomType type)
	{
		super(position, type);
	}

	@Override
	public boolean canSupportRoof()
	{
		return false;
	}
}
