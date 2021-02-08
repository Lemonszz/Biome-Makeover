package party.lemons.biomemakeover.world.feature.mansion;

import com.google.common.collect.Lists;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.util.BMUtil;
import party.lemons.biomemakeover.util.Grid;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.world.feature.mansion.processor.CorridorReplaceProcessor;
import party.lemons.biomemakeover.world.feature.mansion.processor.FloorRoomReplaceProcessor;
import party.lemons.biomemakeover.world.feature.mansion.processor.GardenRoomReplaceProcessor;
import party.lemons.biomemakeover.world.feature.mansion.room.BossRoom;
import party.lemons.biomemakeover.world.feature.mansion.room.MansionRoom;
import party.lemons.biomemakeover.world.feature.mansion.room.RoofMansionRoom;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MansionLayout
{
	//TODO: make more generic to support future structures without much work

	protected final Grid<MansionRoom> layout = new Grid<>();
	protected final List<FloorRoomReplaceProcessor> floorProcessors = Lists.newArrayList();

	public MansionLayout()
	{
		floorProcessors.add(new CorridorReplaceProcessor());
		floorProcessors.add(new GardenRoomReplaceProcessor());
	}

	public void generateLayout(Random random, int startY)
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

		//Extras
		createDungeon(random, allRooms, startY);
		createTowers(random, allRooms);

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

	protected void createTowers(Random random, List<MansionRoom> allRooms)
	{
		//Towers
		final int maxTowers = 5;
		final int maxTowerHeight = 5;

		for(int i = 1; i < maxTowers; i++)
		{
			MansionRoom room = allRooms.get(random.nextInt(allRooms.size()));
			if(room.canSupportRoof())
			{
				while(layout.contains(room.getPosition().up())) room = layout.get(room.getPosition().up());

				if(room.getRoomType().isReplaceable())
				{
					room.setRoomType(RoomType.TOWER_BASE);
					BlockPos towerPos = room.getPosition().up();
					int height = 1 + random.nextInt(maxTowerHeight);
					for(int t = 0; t < height; t++)
					{
						layout.put(towerPos, new MansionRoom(towerPos, RoomType.TOWER_MID));
						towerPos = towerPos.up();
					}
					layout.put(towerPos, new MansionRoom(towerPos, RoomType.TOWER_TOP));
				}
			}
		}
	}

	protected void createDungeon(Random random, List<MansionRoom> allRooms, int startY)
	{
		//Dungeon
		final int maxY = 45;        //Max level dungeon will generate at
		final int downStep = 11;    //Amount to step down check per floor.

		//Pick a random replaceable room to use are our stairs down.
		MansionRoom dungeonStairs;
		do{
			dungeonStairs = allRooms.get(random.nextInt(allRooms.size()));
		}while(dungeonStairs.getPosition().getY() != 0 || !dungeonStairs.getRoomType().isReplaceable());
		dungeonStairs.setRoomType(RoomType.DUNGEON_STAIRS_TOP);

		//See how many stairs down we need until we get to a reasonable level.
		int amt = 1;
		int yy = startY;    //Too lazy to do this right lol
		while(yy > maxY)
		{
			amt++;
			yy -= downStep;
		}

		//Create stairs down
		BlockPos dungeonPos = dungeonStairs.getPosition().down();
		for(int i = 0; i < amt - 1; i++)    //Middle stairs
		{
			MansionRoom dungeonStairsMid = new MansionRoom(dungeonPos, RoomType.DUNGEON_STAIRS_MID);
			layout.put(dungeonPos, dungeonStairsMid);
			dungeonPos = dungeonPos.down();
		}
		MansionRoom dungeonStairsBottom = new MansionRoom(dungeonPos, RoomType.DUNGEON_STAIRS_BOTTOM);  //Stairs bottom
		layout.put(dungeonPos, dungeonStairsBottom);

		BlockPos dungeonStart = new BlockPos(dungeonPos);   //Dungeon start position (bottom of stairs)

		//Boss room will be at the end of a corridor, this is always straight from the stairs to ensure no collisions with any other room.
		Direction bossDir = BMUtil.randomHorizontal();  //Direction to boss room
		List<MansionRoom> dungeonRooms = Lists.newArrayList();
		dungeonRooms.add(dungeonStairsBottom);
		int dungeonCorridorLength = RandomUtil.randomRange(4, 6); //Length of the corridor
		for(int i = 1; i < dungeonCorridorLength; i++)  //Create corridor rooms
		{
			BlockPos roomPos = dungeonStart.offset(bossDir, i);
			MansionRoom room = new MansionRoom(roomPos, RoomType.DUNGEON_ROOM);
			room.setLayoutType(LayoutType.REQUIRED);
			room.getLayout().put(bossDir, true);
			layout.put(roomPos, room);
			dungeonRooms.add(room);
		}

		//Boss Room
		BlockPos bossPos = dungeonStart.offset(bossDir, dungeonCorridorLength + 1);
		BossRoom bossRoom = new BossRoom(bossPos);
		bossRoom.getLayout().put(bossDir.getOpposite(), true);
		layout.put(bossPos, bossRoom);

		//Extra dungeon rooms
		for(int i = 0; i < 10; i++)
		{
			MansionRoom rm;
			do{
				rm =  dungeonRooms.get(random.nextInt(dungeonRooms.size()));
			}while(rm.getPosition().equals(dungeonPos.offset(bossDir, dungeonCorridorLength - 1))); //Ensures there's no rooms on the furthest position to avoid collisions.

			Direction dir;
			do{
				dir = BMUtil.randomHorizontal();
			}while(dir == bossDir || dir == bossDir.getOpposite()); //Only create rooms sideways.

			//Place room
			BlockPos offPos = rm.getPosition().offset(dir);
			if(!layout.contains(offPos))
			{
				MansionRoom offsetRoom = new MansionRoom(offPos, RoomType.DUNGEON_ROOM);
				offsetRoom.setLayoutType(LayoutType.REQUIRED);
				layout.put(offPos, offsetRoom);
				dungeonRooms.add(offsetRoom);
			}
		}
		dungeonRooms.forEach(rm->rm.setLayout(this, random));
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
