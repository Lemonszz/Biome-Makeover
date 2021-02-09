package party.lemons.biomemakeover.world.feature.mansion.room;

import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.util.Grid;
import party.lemons.biomemakeover.world.feature.mansion.*;

import java.util.List;
import java.util.Random;

public class MansionRoom
{
	protected final RoomLayout layout = new RoomLayout();
	protected final BlockPos position;
	protected RoomType type;
	protected LayoutType layoutType;
	public boolean active = true;
	private int sortValue = 0;

	public MansionRoom(BlockPos position, RoomType type)
	{
		this.position = position;
		this.type = type;
		this.layoutType = LayoutType.NORMAL;
	}

	public RoomType getRoomType()
	{
		return type;
	}

	public void setRoomType(RoomType type)
	{
		this.type = type;
	}

	public void setLayout(MansionLayout layout, Random random)
	{
		Grid<MansionRoom> lo = layout.getLayout();
		for(int i = 0; i < 4; i++)
		{
			Direction dir = Direction.fromHorizontal(i);
			MansionRoom neighbour = lo.get(getPosition().offset(dir));

			if(neighbour != null)
			{
				switch(layoutType)
				{
					case REQUIRED:
						this.layout.put(dir, true);
						neighbour.layout.put(dir.getOpposite(), true);
						break;
				}
			}
		}
	}

	public RoomLayout getLayout()
	{
		return layout;
	}

	public void setLayoutType(LayoutType layoutType)
	{
		this.layoutType = layoutType;
	}

	public BlockPos getPosition()
	{
		return position;
	}

	public Identifier getTemplate(Random random)
	{
		if(type != RoomType.CORRIDOR) return type.getRandomTemplate(random);
		else
		{
			List<Identifier> ids;
			switch(layout.doorCount())
			{
				case 1:
					ids = MansionFeature.CORRIDOR_STRAIGHT;
					break;
				case 2:
					if((layout.get(Direction.NORTH) && layout.get(Direction.SOUTH)) || (layout.get(Direction.EAST) && layout.get(Direction.WEST)))
						ids = MansionFeature.CORRIDOR_STRAIGHT;
					else ids = MansionFeature.CORRIDOR_CORNER;
					break;
				case 3:
					ids = MansionFeature.CORRIDOR_T;
					break;
				case 4:
					ids = MansionFeature.CORRIDOR_CROSS;
					break;
				default:
					ids = MansionFeature.STAIR_DOWN;
					break;
			}
			return ids.get(random.nextInt(ids.size()));
		}
	}

	public BlockRotation getRotation(Random random)
	{
		if(type.hasColumnRotation())
		{
			int index = Math.abs((getPosition().getX() + getPosition().getZ()) % 4);
			return BlockRotation.values()[index];
		}else if(type != RoomType.CORRIDOR) return BlockRotation.random(random);
		else if(type == RoomType.GARDEN) return BlockRotation.random(random);
		else
		{
			switch(layout.doorCount())
			{
				case 1:
					if(layout.get(Direction.SOUTH)) return BlockRotation.NONE;
					else if(layout.get(Direction.NORTH)) return BlockRotation.CLOCKWISE_180;
					else if(layout.get(Direction.EAST)) return BlockRotation.CLOCKWISE_90;
					else if(layout.get(Direction.WEST)) return BlockRotation.COUNTERCLOCKWISE_90;
				case 2:
					if(layout.get(Direction.SOUTH) && layout.get(Direction.NORTH)) return BlockRotation.NONE;
					else if(layout.get(Direction.SOUTH) && layout.get(Direction.EAST))
						return BlockRotation.CLOCKWISE_90; // !
					else if(layout.get(Direction.SOUTH) && layout.get(Direction.WEST))
						return BlockRotation.CLOCKWISE_180; // ~
					else if(layout.get(Direction.EAST) && layout.get(Direction.WEST)) return BlockRotation.CLOCKWISE_90;
					else if(layout.get(Direction.NORTH) && layout.get(Direction.EAST)) return BlockRotation.NONE; //~!
					else if(layout.get(Direction.NORTH) && layout.get(Direction.WEST))
						return BlockRotation.COUNTERCLOCKWISE_90; //~ !!
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
					return BlockRotation.values()[random.nextInt(BlockRotation.values().length)];
				case 0:
					return BlockRotation.NONE;
			}
			return null;
		}
	}

	public BlockPos getOffsetForRotation(BlockPos offsetPos, BlockRotation rotation)
	{
		switch(rotation)
		{
			case NONE:
				return offsetPos;
			case CLOCKWISE_90:
				return offsetPos.add(10, 0, 0);
			case CLOCKWISE_180:
				return offsetPos.add(10, 0, 10);
			case COUNTERCLOCKWISE_90:
				return offsetPos.add(0, 0, 10);
		}
		return offsetPos;
	}

	public boolean isConnected(Direction direction)
	{
		return layout.get(direction);
	}

	public boolean canSupportRoof()
	{
		return true;
	}

	public void setSortValue(int sortValue)
	{
		this.sortValue = sortValue;
	}

	public int getSortValue()
	{
		return sortValue;
	}

	public void addWalls(Random random, BlockPos wallPos, StructureManager manager, Grid<MansionRoom> roomGrid, List<StructurePiece> children)
	{
		boolean ground = getPosition().getY() == 0;

		if(getRoomType().hasWalls())
		{
			if(isConnected(Direction.NORTH))
				children.add(new MansionFeature.Piece(manager, getInnerWall(random), wallPos.offset(Direction.NORTH, 2), BlockRotation.NONE, ground, false));
			else if(!roomGrid.contains(getPosition().north()) || !roomGrid.get(getPosition().north()).getRoomType().hasWalls())
				children.add(new MansionFeature.Piece(manager, getOuterWall(Direction.NORTH, roomGrid, random), wallPos.offset(Direction.EAST, 11), BlockRotation.CLOCKWISE_180, ground, true));
			else if(roomGrid.contains(getPosition().north()))
				children.add(new MansionFeature.Piece(manager, getFlatWall(random), wallPos.offset(Direction.NORTH, 2), BlockRotation.NONE, ground, false));

			if(isConnected(Direction.WEST))
				children.add(new MansionFeature.Piece(manager, getInnerWall(random), wallPos, BlockRotation.CLOCKWISE_90, ground, false));
			else if(!roomGrid.contains(getPosition().west()) || !roomGrid.get(getPosition().west()).getRoomType().hasWalls())
				children.add(new MansionFeature.Piece(manager, getOuterWall(Direction.WEST, roomGrid, random), wallPos.north(), BlockRotation.CLOCKWISE_90, ground, true));
			else if(roomGrid.contains(getPosition().west()))
				children.add(new MansionFeature.Piece(manager, getFlatWall(random), wallPos, BlockRotation.CLOCKWISE_90, ground, false));

			if(!roomGrid.contains(getPosition().east()) || !roomGrid.get(getPosition().east()).getRoomType().hasWalls())
				children.add(new MansionFeature.Piece(manager, getOuterWall(Direction.EAST, roomGrid, random), wallPos.offset(Direction.EAST, 11).west().south(11), BlockRotation.COUNTERCLOCKWISE_90, ground, true));
			if(!roomGrid.contains(getPosition().south()) || !roomGrid.get(getPosition().south()).getRoomType().hasWalls())
				children.add(new MansionFeature.Piece(manager, getOuterWall(Direction.SOUTH, roomGrid, random), wallPos.offset(Direction.SOUTH, 10).west(), BlockRotation.NONE, ground, true));

			BlockPos cornerPos1 = getPosition().offset(Direction.NORTH).offset(Direction.WEST);
			if(roomGrid.contains(cornerPos1) && roomGrid.get(cornerPos1).getRoomType().hasWalls())
				children.add(new MansionFeature.Piece(manager, MansionFeature.CORNER_FILLER, wallPos.offset(Direction.WEST).offset(Direction.NORTH).add(0, 0, 0), BlockRotation.NONE, ground, false));
		}
	}

	public Identifier getInnerWall(Random random)
	{
		return MansionFeature.INNER_WALL.get(random.nextInt(MansionFeature.INNER_WALL.size()));
	}

	public Identifier getFlatWall(Random random)
	{
		return MansionFeature.FLAT_WALL.get(random.nextInt(MansionFeature.FLAT_WALL.size()));
	}

	public Identifier getOuterWall(Direction dir, Grid<MansionRoom> roomGrid, Random random)
	{
		if(getPosition().getY() > 0)
		{
			if(getRoomType().hasWindows() && random.nextFloat() < 0.8F && !roomGrid.contains(getPosition().offset(dir)))
				return MansionFeature.OUTER_WINDOW.get(random.nextInt(MansionFeature.OUTER_WINDOW.size()));

			return MansionFeature.OUTER_WALL.get(random.nextInt(MansionFeature.OUTER_WALL.size()));
		}
		else
		{
			return MansionFeature.OUTER_WALL_BASE.get(random.nextInt(MansionFeature.OUTER_WALL_BASE.size()));
		}
	}
}
