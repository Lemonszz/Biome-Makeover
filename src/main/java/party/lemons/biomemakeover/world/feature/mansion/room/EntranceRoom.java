package party.lemons.biomemakeover.world.feature.mansion.room;

import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.util.Grid;
import party.lemons.biomemakeover.world.feature.mansion.MansionFeature;
import party.lemons.biomemakeover.world.feature.mansion.RoomType;

import java.util.List;
import java.util.Random;

public class EntranceRoom extends MansionRoom
{
	public EntranceRoom(BlockPos position, RoomType type)
	{
		super(position, type);
		setSortValue(1);
	}

	public void addWalls(Random random, BlockPos wallPos, StructureManager manager, Grid<MansionRoom> roomGrid, List<StructurePiece> children)
	{
		boolean ground = getPosition().getY() == 0;

		if(getRoomType().hasWalls())
		{
			if(isConnected(Direction.NORTH))
				children.add(new MansionFeature.Piece(manager, getInnerWall(random), wallPos.offset(Direction.NORTH, 2), BlockRotation.NONE, ground, false));

			if(isConnected(Direction.WEST))
				children.add(new MansionFeature.Piece(manager, getInnerWall(random), wallPos, BlockRotation.CLOCKWISE_90, ground, false));

			BlockPos cornerPos1 = getPosition().offset(Direction.NORTH).offset(Direction.WEST);
			if(roomGrid.contains(cornerPos1) && roomGrid.get(cornerPos1).getRoomType().hasWalls())
				children.add(new MansionFeature.Piece(manager, MansionFeature.CORNER_FILLER, wallPos.offset(Direction.WEST).offset(Direction.NORTH).add(0, 0, 0), BlockRotation.NONE, ground, false));
		}
	}

	public BlockRotation getRotation(Random random)
	{
		switch(layout.doorCount())
		{
			case 1:
				if(layout.get(Direction.NORTH)) return BlockRotation.NONE;
				else if(layout.get(Direction.SOUTH)) return BlockRotation.CLOCKWISE_180;
				else if(layout.get(Direction.EAST)) return BlockRotation.CLOCKWISE_90;
				else if(layout.get(Direction.WEST)) return BlockRotation.COUNTERCLOCKWISE_90;
		}
		return BlockRotation.NONE;
	}

	public BlockPos getOffsetForRotation(BlockPos offsetPos, BlockRotation rotation)
	{
		switch(rotation)
		{
			case CLOCKWISE_180:
				return offsetPos.add(10, 0, 10);
			case CLOCKWISE_90:
				return offsetPos.add(10, 0, 0);
			case NONE:
				return offsetPos.add(0, 0, 0);
			case COUNTERCLOCKWISE_90:
				return offsetPos.add(0, 0, 10);
		}
		return offsetPos;
	}
}
