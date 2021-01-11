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

		List<BlockPos.Mutable> corridorStarts = Lists.newArrayList(new BlockPos.Mutable(0, 0, 0));
		for(int floor = 0; floor < floors; floor++)
		{
			int corridorCount = placeCorridors(floor, floorCorridorTarget, corridorStarts, random);
			List<MansionRoom> rooms = null;
			if(corridorCount > 0)
				rooms = placeRooms(floor, floorRoomTarget, random);

			if(rooms != null && floor < floors - 1 && rooms.size() > 0)
			{
				corridorStarts.clear();
				for(int i = 0; i < 1 + random.nextInt(3); i++)
				{
					MansionRoom stairCase = rooms.get(random.nextInt(rooms.size()));
					stairCase.setRoomType(RoomType.STAIRS_UP);
					BlockPos stairPos = stairCase.getPosition().up();

					MansionRoom upStairs = new MansionRoom(stairPos, RoomType.STAIRS_DOWN);
					upStairs.setLayoutType(LayoutType.REQUIRED);
					layout.put(stairPos, upStairs);
					corridorStarts.add(new BlockPos.Mutable(stairPos.getX(), stairPos.getY(), stairPos.getZ()));
				}
			}
			floorCorridorTarget /= 2F;
			floorRoomTarget /= 2F;
		}
		layout.getEntries().forEach((rm)->rm.setLayout(this, random));
		layout.getEntries().forEach((rm)->{
			if(rm.getRoomType() == RoomType.CORRIDOR && random.nextFloat() < 0.3F)
				rm.setRoomType(RoomType.ROOM);
		});

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

	public int placeCorridors(int y, int maxCount, List<BlockPos.Mutable> positions, Random random)
	{
		int attempts = 500;
		int placed = 0;
		BlockPos lastSuccess = new BlockPos(positions.get(0));
		BlockPos.Mutable pos = positions.get(random.nextInt(positions.size()));
		while(placed < maxCount && attempts > 0)
		{
			if(!layout.contains(pos))
			{
				if(y != 0 && !layout.contains(pos.down()))
				{
					attempts--;
					continue;
				}
				layout.put(new BlockPos(pos), new MansionRoom(new BlockPos(pos), RoomType.CORRIDOR));
				lastSuccess = new BlockPos(pos);
				placed++;
			}
			if(random.nextInt(5) == 0)
				positions.add(new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ()));

			pos = pos.move(Direction.fromHorizontal(random.nextInt(4)));
			pos = positions.get(random.nextInt(positions.size()));

		/*	if(random.nextFloat() < 0.1)
			{
				if(random.nextBoolean())
				{
					pos.set(start.getX(), start.getY(), start.getZ());
				}
				else
				{
					pos.set(lastSuccess.getX(), lastSuccess.getY(), lastSuccess.getZ());
				}
			}*/
		}

		return placed;
	}

	public List<MansionRoom> placeRooms(int y, int maxCount, Random random)
	{
		int roomsPlaced = 0;
		int roomTarget = maxCount;
		int attempts = 500;
		List<MansionRoom> rooms = Lists.newArrayList();
		BlockPos lastSuccess = null;

		while(attempts > 0 && roomsPlaced < roomTarget)
		{
			BlockPos.Mutable randomPos = new BlockPos.Mutable(
					RandomUtil.randomRange(layout.getMinX(), layout.getMaxX()),
					y,
					RandomUtil.randomRange(layout.getMinZ(), layout.getMaxZ()));

			if(lastSuccess != null && random.nextFloat() < 0.10F)
			{
				randomPos = new BlockPos.Mutable(lastSuccess.getX(), lastSuccess.getY(), lastSuccess.getZ());
			}

			if(layout.contains(randomPos))
			{
				if(y != 0 && !layout.contains(randomPos.down()))
					continue;

				MansionRoom existingRoom = layout.get(randomPos);
				randomPos = randomPos.move(Direction.fromHorizontal(random.nextInt(4)));
				if(!layout.contains(randomPos))
				{
					if(y == 0 || (layout.contains(randomPos.down()) && layout.get(randomPos.down()).canSupportRoof()))
					{
						MansionRoom newRoom = new MansionRoom(randomPos, RoomType.ROOM);
						layout.put(randomPos, newRoom);
						if(existingRoom.getRoomType() == RoomType.ROOM)
							newRoom.setLayoutType(LayoutType.REQUIRED);

						rooms.add(newRoom);
						roomsPlaced++;
						lastSuccess = new BlockPos(randomPos.getX(), randomPos.getY(), randomPos.getZ());
					}
				}
			}
			else
			{
				attempts--;
			}
		}
		return rooms;
	}

	public Grid<MansionRoom> getLayout()
	{
		return layout;
	}
}
