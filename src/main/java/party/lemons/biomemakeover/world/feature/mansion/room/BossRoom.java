package party.lemons.biomemakeover.world.feature.mansion.room;

import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.world.feature.mansion.RoomType;

import java.util.Random;

public class BossRoom extends MansionRoom
{
	public BossRoom(BlockPos position)
	{
		super(position, RoomType.BOSS);
	}

	public BlockPos getOffsetForRotation(BlockPos offsetPos, BlockRotation rotation)
	{
		if(layout.get(Direction.SOUTH)) return offsetPos.south(23).west(9);
		else if(layout.get(Direction.NORTH)) return offsetPos.north(14).east(19);
		else if(layout.get(Direction.EAST)) return offsetPos.east(23).south(20);
		else if(layout.get(Direction.WEST)) return offsetPos.west(14).north(9);

		return offsetPos;
	}

	public BlockRotation getRotation(Random random)
	{
		if(layout.get(Direction.SOUTH)) return BlockRotation.COUNTERCLOCKWISE_90;
		else if(layout.get(Direction.NORTH)) return BlockRotation.CLOCKWISE_90;
		else if(layout.get(Direction.EAST)) return BlockRotation.CLOCKWISE_180;
		else if(layout.get(Direction.WEST)) return BlockRotation.NONE;

		return BlockRotation.NONE;
	}
}
