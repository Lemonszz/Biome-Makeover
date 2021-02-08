package party.lemons.biomemakeover.world.feature.mansion.room;

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
}
