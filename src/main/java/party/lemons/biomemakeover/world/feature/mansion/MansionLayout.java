package party.lemons.biomemakeover.world.feature.mansion;

import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.util.Grid;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.List;
import java.util.Random;

public class MansionLayout
{
	private Grid<MansionRoom> layout = new Grid<>();

	public MansionLayout()
	{

	}

	public void generateLayout(Random random)
	{
		int floorCorridorTarget = 10 + random.nextInt(10);
		int floors = 2 + random.nextInt(3);
		int floorRoomTarget = floorCorridorTarget - random.nextInt(5);

		BlockPos.Mutable corridorStart = new BlockPos.Mutable(0, 0, 0);
		for(int floor = 0; floor < floors; floor++)
		{
			placeCorridors(floor, floorCorridorTarget, corridorStart, random);
			List<MansionRoom> rooms = placeRooms(floor, floorRoomTarget, random);

			if(floor < floors)
			{
				MansionRoom stairCase = rooms.get(random.nextInt(rooms.size()));
				stairCase.setRoomType(RoomType.STAIRS_UP);
				BlockPos stairPos = stairCase.getPosition().up();

				MansionRoom upStairs = new MansionRoom(stairPos, RoomType.STAIRS_DOWN);
				layout.put(stairPos, upStairs);
				corridorStart.set(stairPos.up());
			}
		}
		layout.getEntries().forEach((rm)->rm.setLayout(this, random));

		/*for(int x = layout.getMinX(); x < layout.getMaxX(); x++)
		{
			String s = "";
			for(int z = layout.getMinZ(); z < layout.getMaxZ(); z++)
			{
				s += layout.contains(x, 0, z) ? layout.get(x, 0, z).getRoomType() == RoomType.ROOM ? "@" : "#" : "-";
			}
			System.out.println(s);
		}*/
	}

	public void placeCorridors(int y, int maxCount, BlockPos.Mutable pos, Random random)
	{
		int placed = 0;
		while(placed < maxCount)
		{
			if(!layout.contains(pos))
			{
				if(y != 0 && !layout.contains(pos.down()))
					continue;

				layout.put(pos, new MansionRoom(pos, RoomType.CORRIDOR));
				placed++;
			}
			pos = pos.move(Direction.fromHorizontal(random.nextInt(4)));
		}
	}

	public List<MansionRoom> placeRooms(int y, int maxCount, Random random)
	{
		int roomsPlaced = 0;
		int roomTarget = maxCount;
		List<MansionRoom> rooms = Lists.newArrayList();

		while(roomsPlaced < roomTarget)
		{
			BlockPos.Mutable randomPos = new BlockPos.Mutable(
					RandomUtil.randomRange(layout.getMinX(), layout.getMaxX()),
					y,
					RandomUtil.randomRange(layout.getMinZ(), layout.getMaxZ()));

			if(layout.contains(randomPos))
			{
				if(y != 0 && !layout.contains(randomPos.down()))
					continue;

				MansionRoom existingRoom = layout.get(randomPos);
				randomPos = randomPos.move(Direction.fromHorizontal(random.nextInt(4)));
				if(!layout.contains(randomPos))
				{
					MansionRoom newRoom = new MansionRoom(randomPos, RoomType.ROOM);
					layout.put(randomPos, newRoom);
					if(existingRoom.getRoomType() == RoomType.ROOM)
						newRoom.setLayoutType(LayoutType.REQUIRED);

					rooms.add(newRoom);
					roomsPlaced++;
				}
			}
		}
		return rooms;
	}

	public Grid<MansionRoom> getLayout()
	{
		return layout;
	}
}
