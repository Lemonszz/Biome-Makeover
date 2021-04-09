package party.lemons.biomemakeover.world.feature.mansion.room;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.util.Grid;
import party.lemons.biomemakeover.world.feature.mansion.MansionFeature;
import party.lemons.biomemakeover.world.feature.mansion.RoomType;

import java.util.Random;

public class DungeonRoom extends MansionRoom
{
	public DungeonRoom(BlockPos position, RoomType type)
	{
		super(position, type);
	}

	@Override
	public Identifier getInnerWall(Random random)
	{
		return MansionFeature.DUNGEON_DOOR.get(random.nextInt(MansionFeature.DUNGEON_DOOR.size()));
	}

	@Override
	public Identifier getFlatWall(Random random)
	{
		return MansionFeature.DUNGEON_WALL.get(random.nextInt(MansionFeature.DUNGEON_WALL.size()));
	}

	@Override
	public Identifier getOuterWall(Direction dir, Grid<MansionRoom> roomGrid, Random random)
	{
		return MansionFeature.DUNGEON_WALL.get(random.nextInt(MansionFeature.DUNGEON_WALL.size()));
	}
}
