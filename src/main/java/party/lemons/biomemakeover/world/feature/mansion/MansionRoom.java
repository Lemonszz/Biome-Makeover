package party.lemons.biomemakeover.world.feature.mansion;

import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.util.Grid;

import java.util.List;
import java.util.Random;

public class MansionRoom
{
	private final RoomLayout layout = new RoomLayout();
	private final BlockPos position;
	private RoomType type;
	private LayoutType layoutType;

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
					case NORMAL:
						if(neighbour.getRoomType().doorRequired || random.nextFloat() < 0.125F)
						{
							this.layout.put(dir, true);
							neighbour.layout.put(dir.getOpposite(), true);
						}
						break;
					case REQUIRED:
						this.layout.put(dir, true);
						neighbour.layout.put(dir.getOpposite(), true);
						break;
				}
			}
		}
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
		if(type != RoomType.CORRIDOR)
			return type.getRandomTemplate(random);
		else
		{
			List<Identifier> ids;
			switch(layout.doorCount())
			{
				case 1:
					ids = MansionFeature.CORRIDOR_STRAIGHT;
					break;
				case 2:
					if( (layout.get(Direction.NORTH) && layout.get(Direction.SOUTH)) ||
						(layout.get(Direction.EAST) && layout.get(Direction.WEST)))
						ids = MansionFeature.CORRIDOR_STRAIGHT;
					else
						ids = MansionFeature.CORRIDOR_CORNER;
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
		if(type == RoomType.STAIRS_DOWN || type == RoomType.STAIRS_UP)
		{
			int index = Math.abs((getPosition().getX() + getPosition().getZ()) % 4);
			System.out.println(index);
			return BlockRotation.values()[index];
		}
		else if(type != RoomType.CORRIDOR)
			return BlockRotation.random(random);
		else
		{
			switch(layout.doorCount())
			{
				case 1:
					if(layout.get(Direction.SOUTH))
						return BlockRotation.NONE;
					else if(layout.get(Direction.NORTH))
						return BlockRotation.CLOCKWISE_180;
					else if(layout.get(Direction.EAST))
						return BlockRotation.CLOCKWISE_90;
					else if(layout.get(Direction.WEST))
						return BlockRotation.COUNTERCLOCKWISE_90;
				case 2:
					if(layout.get(Direction.SOUTH) && layout.get(Direction.NORTH))
						return BlockRotation.NONE;
					else if(layout.get(Direction.SOUTH) && layout.get(Direction.EAST))
						return BlockRotation.CLOCKWISE_90; // !
					else if(layout.get(Direction.SOUTH) && layout.get(Direction.WEST))
						return BlockRotation.CLOCKWISE_180; // ~
					else if(layout.get(Direction.EAST) && layout.get(Direction.WEST))
						return BlockRotation.CLOCKWISE_90;
					else if(layout.get(Direction.NORTH) && layout.get(Direction.EAST))
						return BlockRotation.NONE; //~!
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

			System.out.println("NO LAYOUT!!");
			System.out.println(layout.get(Direction.NORTH));
			System.out.println(layout.get(Direction.EAST));
			System.out.println(layout.get(Direction.SOUTH));
			System.out.println(layout.get(Direction.WEST));
			return null;
		}
	}

	public boolean isConnected(Direction direction)
	{
		return layout.get(direction);
	}
}
