package party.lemons.biomemakeover.world.feature.mansion.room;

import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.util.Grid;
import party.lemons.biomemakeover.world.feature.mansion.MansionFeature;
import party.lemons.biomemakeover.world.feature.mansion.MansionLayout;
import party.lemons.biomemakeover.world.feature.mansion.RoomType;

import java.util.List;
import java.util.Random;

public class RoofMansionRoom extends NonRoofedMansionRoom
{
	public RoofMansionRoom(BlockPos position)
	{
		super(position, RoomType.ROOF);
	}

	@Override
	public int getSortValue()
	{
		return -4 + getLayout().doorCount();
	}

	@Override
	public void addWalls(Random random, BlockPos wallPos, StructureManager manager, Grid<MansionRoom> roomGrid, List<StructurePiece> children)
	{
		if(isRoofConnected(Direction.NORTH, roomGrid))
			children.add(new MansionFeature.Piece(manager, getRoofSplit(random), wallPos.offset(Direction.NORTH).add(-2, 0, 0), BlockRotation.NONE, getPosition().getY() == 0, true));
		if(isRoofConnected(Direction.WEST, roomGrid))
			children.add(new MansionFeature.Piece(manager, getRoofSplit(random), wallPos.offset(Direction.WEST).add(0, 0, -2), BlockRotation.CLOCKWISE_90, getPosition().getY() == 0, true));
	}

	private Identifier getRoofSplit(Random random)
	{
		return MansionFeature.ROOF_SPLIT.get(random.nextInt(MansionFeature.ROOF_SPLIT.size()));
	}

	public BlockPos getOffsetForRotation(BlockPos offsetPos, BlockRotation rotation)
	{
		switch(layout.doorCount())
		{
			case 1:
				if(layout.get(Direction.SOUTH)) return offsetPos.add(12, 0, 10);
				else if(layout.get(Direction.NORTH)) return offsetPos.add(-2, 0, 0);
				else if(layout.get(Direction.EAST)) return offsetPos.add(10, 0, -2);
				else if(layout.get(Direction.WEST)) return offsetPos.add(0, 0, 12);
			case 2:
				if(layout.get(Direction.SOUTH) && layout.get(Direction.NORTH))
					return offsetPos.add(-2, 0, 0);
				else if(layout.get(Direction.SOUTH) && layout.get(Direction.EAST)) return offsetPos.add(-2, 0, -2);
				else if(layout.get(Direction.SOUTH) && layout.get(Direction.WEST))
					return offsetPos.add(12, 0, -2);
				else if(layout.get(Direction.EAST) && layout.get(Direction.WEST))
					return offsetPos.add(0, 0, 12);
				else if(layout.get(Direction.NORTH) && layout.get(Direction.EAST)) return offsetPos.add(-2, 0, 12);
				else if(layout.get(Direction.NORTH) && layout.get(Direction.WEST)) return offsetPos.add(12, 0, 12);
			case 3:
				if(layout.get(Direction.NORTH) && layout.get(Direction.SOUTH) && layout.get(Direction.WEST))
					return offsetPos.add(12, 0, 10);
				else if(layout.get(Direction.NORTH) && layout.get(Direction.SOUTH) && layout.get(Direction.EAST))
					return offsetPos.add(-2, 0, 0);
				else if(layout.get(Direction.NORTH) && layout.get(Direction.EAST) && layout.get(Direction.WEST))
					return offsetPos.add(0, 0, 12);
				else if(layout.get(Direction.SOUTH) && layout.get(Direction.EAST) && layout.get(Direction.WEST))
					return offsetPos.add(10, 0, -2);
			case 4:
				return offsetPos.add(0, 0, 0);
			case 0:
				return offsetPos.add(-2, 0, -2);
		}
		return offsetPos;
	}

	public Identifier getTemplate(Random random)
	{
		List<Identifier> ids;
		switch(layout.doorCount())
		{
			case 0:
				ids = MansionFeature.ROOF_0;
				break;
			case 1:
				ids = MansionFeature.ROOF_1;
				break;
			case 2:
				if((layout.get(Direction.NORTH) && layout.get(Direction.SOUTH)) || (layout.get(Direction.EAST) && layout.get(Direction.WEST)))
					ids = MansionFeature.ROOF_2_STRAIGHT;
				else ids = MansionFeature.ROOF_2;
				break;
			case 3:
				ids = MansionFeature.ROOF_3;
				break;
			case 4:
				ids = MansionFeature.ROOF_4;
				break;
			default:
				ids = MansionFeature.INNER_WALL;
				break;
		}
		return ids.get(random.nextInt(ids.size()));
	}

	public BlockRotation getRotation(Random random)
	{
		switch(layout.doorCount())
		{
			case 1:
				if(layout.get(Direction.SOUTH)) return BlockRotation.CLOCKWISE_180;
				else if(layout.get(Direction.NORTH)) return BlockRotation.NONE;
				else if(layout.get(Direction.EAST)) return BlockRotation.CLOCKWISE_90;
				else if(layout.get(Direction.WEST)) return BlockRotation.COUNTERCLOCKWISE_90;
			case 2:
				if(layout.get(Direction.SOUTH) && layout.get(Direction.NORTH)) return BlockRotation.NONE;
				else if(layout.get(Direction.SOUTH) && layout.get(Direction.EAST)) return BlockRotation.NONE; // !
				else if(layout.get(Direction.SOUTH) && layout.get(Direction.WEST))
					return BlockRotation.CLOCKWISE_90; // ~
				else if(layout.get(Direction.EAST) && layout.get(Direction.WEST))
					return BlockRotation.COUNTERCLOCKWISE_90;
				else if(layout.get(Direction.NORTH) && layout.get(Direction.EAST))
					return BlockRotation.COUNTERCLOCKWISE_90;
				else if(layout.get(Direction.NORTH) && layout.get(Direction.WEST))
					return BlockRotation.CLOCKWISE_180; //~ !!
			case 3:
				if(layout.get(Direction.NORTH) && layout.get(Direction.SOUTH) && layout.get(Direction.WEST))
					return BlockRotation.CLOCKWISE_180;
				else if(layout.get(Direction.NORTH) && layout.get(Direction.SOUTH) && layout.get(Direction.EAST))
					return BlockRotation.NONE;
				else if(layout.get(Direction.NORTH) && layout.get(Direction.EAST) && layout.get(Direction.WEST))
					return BlockRotation.COUNTERCLOCKWISE_90;
				else if(layout.get(Direction.SOUTH) && layout.get(Direction.EAST) && layout.get(Direction.WEST))
					return BlockRotation.CLOCKWISE_90;
			case 4:
				return BlockRotation.NONE;
			case 0:
				return BlockRotation.NONE;
		}
		return null;
	}

	@Override
	public void setLayout(MansionLayout layout, Random random)
	{
		Grid<MansionRoom> lo = layout.getLayout();
		for(int i = 0; i < 4; i++)
		{
			Direction dir = Direction.fromHorizontal(i);
			MansionRoom neighbour = lo.get(getPosition().offset(dir));

			if(neighbour != null)
			{
				if(neighbour.getRoomType() == RoomType.ROOF || neighbour.getRoomType().hasWalls())
				{
					this.getLayout().put(dir, true);
				}
			}
		}
	}

	public boolean isRoofConnected(Direction direction, Grid<MansionRoom> roomGrid)
	{
		return layout.get(direction) && roomGrid.get(getPosition().offset(direction)).getRoomType() == RoomType.ROOF;
	}

}
