package party.lemons.biomemakeover.world.feature.mansion;

import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.util.Grid;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.world.feature.mansion.processor.CorridorReplaceProcessor;
import party.lemons.biomemakeover.world.feature.mansion.processor.FloorRoomReplaceProcessor;
import party.lemons.biomemakeover.world.feature.mansion.processor.GardenRoomReplaceProcessor;
import party.lemons.biomemakeover.world.feature.mansion.room.MansionRoom;
import party.lemons.biomemakeover.world.feature.mansion.room.RoofMansionRoom;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MansionLayout
{
	private final Grid<MansionRoom> layout = new Grid<>();
	private static final List<FloorRoomReplaceProcessor> floorProcessors = Lists.newArrayList(new CorridorReplaceProcessor(), new GardenRoomReplaceProcessor());


	public MansionLayout()
	{

	}

	public void generateLayout(Random random)
	{
		int floorCorridorTarget = 10 + random.nextInt(5);
		int floors = 2 + random.nextInt(4);
		int floorRoomTarget = floorCorridorTarget - random.nextInt(5);
		List<MansionRoom> allRooms = Lists.newArrayList();

		List<BlockPos.Mutable> corridorStarts = Lists.newArrayList(new BlockPos.Mutable(0, 0, 0));
		for(int floor = 0; floor < floors; floor++)
		{
			List<MansionRoom> corridors = placeCorridors(floor, floorCorridorTarget, corridorStarts, random);
			List<MansionRoom> rooms = Lists.newArrayList();
			if(corridors.size() > 0) rooms = placeRooms(floor, floorRoomTarget, random);

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

			allRooms.addAll(rooms);
			allRooms.addAll(corridors);
			int size = allRooms.size();

			for(int i = 0; i < size; i++)
			{
				MansionRoom room = allRooms.get(i);
				if(room.getRoomType().isReplaceable())
				{
					for(FloorRoomReplaceProcessor processor : floorProcessors)
					{
						if(processor.isValid(random, floor, layout, room))
						{
							room.active = false;
							MansionRoom newRoom = processor.getReplaceRoom(room);
							newRoom.active = true;
							layout.put(room.getPosition(), newRoom);
							rooms.add(newRoom);
							break;
						}
					}
				}
			}
			rooms.removeIf(rm->!rm.active);
			corridors.removeIf(rm->!rm.active);
			allRooms.removeIf(rm->!rm.active);
		}
		layout.getEntries().forEach((rm)->rm.setLayout(this, random));
		layout.getEntries().forEach((rm)->
		{
			if(rm.getRoomType() == RoomType.CORRIDOR && (random.nextFloat() < 0.3F || rm.getLayout().doorCount() < 2))
				rm.setRoomType(RoomType.ROOM);
		});

		allRooms.removeIf(rm->!rm.active);
		//Towers
		for(int i = 1; i < 5; i++)
		{
			MansionRoom room = allRooms.get(random.nextInt(allRooms.size()));
			if(room.canSupportRoof())
			{
				while(layout.contains(room.getPosition().up())) room = layout.get(room.getPosition().up());

				if(room.getRoomType().isReplaceable())
				{
					room.setRoomType(RoomType.TOWER_BASE);
					BlockPos towerPos = room.getPosition().up();
					int height = 1 + random.nextInt(5);
					for(int t = 0; t < height; t++)
					{
						layout.put(towerPos, new MansionRoom(towerPos, RoomType.TOWER_MID));
						towerPos = towerPos.up();
					}
					layout.put(towerPos, new MansionRoom(towerPos, RoomType.TOWER_TOP));
				}
			}
		}

		//Roof
		Iterator<MansionRoom> it = allRooms.iterator();
		BlockPos.Mutable upPos = new BlockPos.Mutable();
		List<MansionRoom> roofs = Lists.newArrayList();
		while(it.hasNext())
		{
			MansionRoom rm = it.next();
			upPos.set(rm.getPosition().getX(), rm.getPosition().getY() + 1, rm.getPosition().getZ());
			if(rm.canSupportRoof() && !layout.contains(upPos))
			{
				BlockPos immu = upPos.toImmutable();
				MansionRoom roofRoom = new RoofMansionRoom(immu);
				layout.put(immu, roofRoom);
				roofs.add(roofRoom);
			}
		}
		roofs.forEach(rm->
		{
			rm.setLayout(this, random);
		});
	}

	public List<MansionRoom> placeCorridors(int y, int maxCount, List<BlockPos.Mutable> positions, Random random)
	{
		int attempts = 500;
		int placed = 0;
		List<MansionRoom> corridors = Lists.newArrayList();
		for(BlockPos p : positions)
		{
			MansionRoom room = layout.get(p.toImmutable());
			if(room != null) corridors.add(room);
		}

		BlockPos.Mutable pos;
		if(y == 0) pos = positions.get(0);
		else pos = corridors.get(random.nextInt(corridors.size())).getPosition().mutableCopy();
		while(placed < maxCount && attempts > 0)
		{
			if(!layout.contains(pos))
			{
				if(y != 0 && !layout.contains(pos.down()))
				{
					attempts--;
					continue;
				}
				if(!(y != 0 && !layout.get(pos.down()).canSupportRoof()))
				{
					MansionRoom room = new MansionRoom(pos.toImmutable(), RoomType.CORRIDOR);
					room.setLayoutType(LayoutType.REQUIRED);
					layout.put(pos.toImmutable(), room);
					corridors.add(room);
					placed++;
				}
			}
			BlockPos nextPos = corridors.get(random.nextInt(corridors.size())).getPosition();
			BlockPos checkPos = nextPos;
			for(int i = 0; i < 4; i++)
			{
				checkPos = nextPos.offset(Direction.fromHorizontal(random.nextInt(4)));
				if(y == 0 || (layout.contains(checkPos.down()) && layout.get(checkPos.down()).canSupportRoof())) break;
			}
			pos.set(checkPos.getX(), checkPos.getY(), checkPos.getZ());
		}

		return corridors;
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
			BlockPos.Mutable randomPos = new BlockPos.Mutable(RandomUtil.randomRange(layout.getMinX(), layout.getMaxX()), y, RandomUtil.randomRange(layout.getMinZ(), layout.getMaxZ()));

			if(lastSuccess != null && random.nextFloat() < 0.10F)
			{
				randomPos = new BlockPos.Mutable(lastSuccess.getX(), lastSuccess.getY(), lastSuccess.getZ());
			}

			if(layout.contains(randomPos))
			{
				if(y != 0 && !layout.contains(randomPos.down())) continue;

				MansionRoom existingRoom = layout.get(randomPos);
				randomPos = randomPos.move(Direction.fromHorizontal(random.nextInt(4)));
				if(!layout.contains(randomPos))
				{
					if(y == 0 || (layout.contains(randomPos.down()) && layout.get(randomPos.down()).canSupportRoof()))
					{
						MansionRoom newRoom = new MansionRoom(randomPos.toImmutable(), RoomType.ROOM);
						layout.put(randomPos, newRoom);
						if(existingRoom.getRoomType() == RoomType.ROOM) newRoom.setLayoutType(LayoutType.REQUIRED);

						rooms.add(newRoom);
						roomsPlaced++;
						lastSuccess = new BlockPos(randomPos.getX(), randomPos.getY(), randomPos.getZ());
					}
				}
			}else
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
