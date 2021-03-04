package party.lemons.biomemakeover.world.feature.mansion.room;

import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.util.Grid;
import party.lemons.biomemakeover.world.feature.mansion.MansionFeature;
import party.lemons.biomemakeover.world.feature.mansion.RoomType;

import java.util.List;
import java.util.Random;

public class BigMansionRoom extends MansionRoom
{
	private boolean isDummy;
	private Direction partnerDirection;
	private BlockPos partnerPos;

	public BigMansionRoom(BlockPos position, Direction partnerDirection, boolean isDummy)
	{
		super(position, isDummy ? RoomType.ROOM_BIG_DUMMY : RoomType.ROOM_BIG);

		this.isDummy = isDummy;
		this.partnerDirection = partnerDirection;
		this.partnerPos = position.offset(partnerDirection);
	}

	@Override
	public BlockRotation getRotation(Random random)
	{
		if(isDummy())
			return BlockRotation.NONE;

		switch(partnerDirection)
		{
			case NORTH:
				return BlockRotation.COUNTERCLOCKWISE_90;
			case SOUTH:
				return BlockRotation.CLOCKWISE_90;
			case WEST:
				return BlockRotation.CLOCKWISE_180;
			case EAST:
				return BlockRotation.NONE;
		}

		return BlockRotation.NONE;
	}

	@Override
	public Identifier getTemplate(Random random)
	{
		if(isDummy())
			return MansionFeature.EMPTY;

		return MansionFeature.ROOM_BIG.get(random.nextInt(MansionFeature.ROOM_BIG.size()));
	}

	public boolean isDummy()
	{
		return isDummy;
	}

	public void addWalls(Random random, BlockPos wallPos, StructureManager manager, Grid<MansionRoom> roomGrid, List<StructurePiece> children)
	{
		boolean ground = getPosition().getY() == 0;

		if(getRoomType().hasWalls())
		{
			if(isConnected(Direction.NORTH))
				addWall(Direction.NORTH, manager, children, getInnerWall(random),  wallPos.offset(Direction.NORTH, 2), BlockRotation.NONE, ground);
			else if(!roomGrid.contains(getPosition().north()) || !roomGrid.get(getPosition().north()).getRoomType().hasWalls())
				children.add(new MansionFeature.Piece(manager, getOuterWall(Direction.NORTH, roomGrid, random), wallPos.offset(Direction.EAST, 11), BlockRotation.CLOCKWISE_180, ground, true));
			else if(roomGrid.contains(getPosition().north()))
				addWall(Direction.NORTH, manager, children, getFlatWall(random),  wallPos.offset(Direction.NORTH, 2), BlockRotation.NONE, ground);

			if(isConnected(Direction.WEST))
				addWall(Direction.WEST, manager, children, getInnerWall(random),  wallPos, BlockRotation.CLOCKWISE_90, ground);
			else if(!roomGrid.contains(getPosition().west()) || !roomGrid.get(getPosition().west()).getRoomType().hasWalls())
				children.add(new MansionFeature.Piece(manager, getOuterWall(Direction.WEST, roomGrid, random), wallPos.north(), BlockRotation.CLOCKWISE_90, ground, true));
			else if(roomGrid.contains(getPosition().west()))
				addWall(Direction.WEST, manager, children, getFlatWall(random),  wallPos, BlockRotation.CLOCKWISE_90, ground);

			if(!roomGrid.contains(getPosition().east()) || !roomGrid.get(getPosition().east()).getRoomType().hasWalls())
				children.add(new MansionFeature.Piece(manager, getOuterWall(Direction.EAST, roomGrid, random), wallPos.offset(Direction.EAST, 11).west().south(11), BlockRotation.COUNTERCLOCKWISE_90, ground, true));
			if(!roomGrid.contains(getPosition().south()) || !roomGrid.get(getPosition().south()).getRoomType().hasWalls())
				children.add(new MansionFeature.Piece(manager, getOuterWall(Direction.SOUTH, roomGrid, random), wallPos.offset(Direction.SOUTH, 10).west(), BlockRotation.NONE, ground, true));

			BlockPos cornerPos1 = getPosition().offset(Direction.NORTH).offset(Direction.WEST);
			if(roomGrid.contains(cornerPos1) && roomGrid.get(cornerPos1).getRoomType().hasWalls())
				children.add(new MansionFeature.Piece(manager, MansionFeature.CORNER_FILLER, wallPos.offset(Direction.WEST).offset(Direction.NORTH).add(0, 0, 0), BlockRotation.NONE, ground, false));
		}
	}

	private void addWall(Direction direction, StructureManager manager, List<StructurePiece> children, Identifier wall, BlockPos pos, BlockRotation rotation, boolean ground)
	{
		if(direction == partnerDirection)
			return;

		else
			children.add(new MansionFeature.Piece(manager, wall, pos, rotation, ground, false));
	}
}
