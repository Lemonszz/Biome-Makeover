package party.lemons.biomemakeover.level.feature.mansion;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import party.lemons.biomemakeover.level.feature.mansion.processor.CorridorReplaceProcessor;
import party.lemons.biomemakeover.level.feature.mansion.processor.FloorRoomReplaceProcessor;
import party.lemons.biomemakeover.level.feature.mansion.processor.GardenRoomReplaceProcessor;
import party.lemons.biomemakeover.level.feature.mansion.room.*;
import party.lemons.biomemakeover.util.BMUtil;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.taniwha.util.collections.Grid;

import java.util.*;
import java.util.stream.Collectors;

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

    public void generateLayout(RandomSource random, int startY)
    {
        int floorCorridorTarget = 10 + random.nextInt(5);   //How many corridors to target per floor
        int floors = 4 + random.nextInt(4); //How many floors to create
        int floorRoomTarget = floorCorridorTarget - random.nextInt(5);  //How many rooms per floor target.

        List<MansionRoom> allRooms = Lists.newArrayList();

		/*
			Corridor starts are possible places for the corridors to start from.
			First floor just has 0,0. Other floors use stair positions
		 */
        List<BlockPos.MutableBlockPos> corridorStarts = Lists.newArrayList(new BlockPos.MutableBlockPos(0, 0, 0));
        for(int floor = 0; floor < floors; floor++) //Createeach floor
        {
            List<MansionRoom> corridors = placeCorridors(floor, floorCorridorTarget, corridorStarts, random);   //Place corridors

            //Since we set corridors as staircases later, we have to remove any rooms that are not staircases from the corridor list. We add them to the all rooms list.
            allRooms.addAll(corridors.stream().filter(c->c.getRoomType() != RoomType.CORRIDOR).collect(Collectors.toList()));
            corridors.removeIf(c->c.getRoomType() != RoomType.CORRIDOR);

            List<MansionRoom> rooms = Lists.newArrayList();
            if(corridors.size() > 0) rooms = placeRooms(floor, floorRoomTarget, random);    //If we managed to place corridors, place rooms

            if(floor < floors - 1 && corridors.size() > 0)  //If we're not on the top floor and we placed rooms, place stairs
            {
                corridorStarts.clear(); //Clear previous corridor start locations
                for(int i = 0; i < Math.max(1, (1 + random.nextInt(3)) - floor); i++)  //Create multiple stair cases
                {
                    //Choose a corridor to become a staircase, set it to be stairs
                    MansionRoom stairCase = corridors.get(random.nextInt(corridors.size()));
                    stairCase.setRoomType(RoomType.STAIRS_UP);
                    BlockPos stairPos = stairCase.getPosition().above();

                    //Create above stairs room.
                    MansionRoom upStairs = new MansionRoom(stairPos, RoomType.STAIRS_DOWN);
                    upStairs.setLayoutType(LayoutType.REQUIRED);
                    layout.put(stairPos, upStairs);
                    corridorStarts.add(new BlockPos.MutableBlockPos(stairPos.getX(), stairPos.getY(), stairPos.getZ()));
                }
            }
            //Reduce floor targets as we go up
            floorCorridorTarget /= 1.5F;
            floorRoomTarget /= 1.5F;

            //Add rooms and corridors to allRooms list
            allRooms.addAll(rooms);
            allRooms.addAll(corridors);
            int size = allRooms.size();

            List<MansionRoom> floorRooms = Lists.newArrayList();
            floorRooms.addAll(rooms);
            floorRooms.addAll(corridors);

            //Loop through each room, running room processors
            for(int i = 0; i < floorRooms.size(); i++)
            {
                MansionRoom room = floorRooms.get(i);
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
        createEntrance(random, allRooms);
        createDungeon(random, allRooms, startY);
        createTowers(random, allRooms);
        createBigRooms(random, allRooms);

        //Roof
        Iterator<MansionRoom> it = allRooms.iterator();
        BlockPos.MutableBlockPos upPos = new BlockPos.MutableBlockPos();
        List<MansionRoom> roofs = Lists.newArrayList();
        while(it.hasNext())
        {
            MansionRoom rm = it.next();
            upPos.set(rm.getPosition().getX(), rm.getPosition().getY() + 1, rm.getPosition().getZ());
            if(rm.canSupportRoof() && !layout.contains(upPos))
            {
                BlockPos immu = upPos.immutable();
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

    private void createBigRooms(RandomSource random, List<MansionRoom> allRooms)
    {
        for(int i = 0; i < allRooms.size(); i++)
        {
            MansionRoom currentRoom = allRooms.get(i);
            if(currentRoom.active && currentRoom.getRoomType() == RoomType.ROOM)
            {
                for(Direction dir : BMUtil.randomOrderedHorizontals())
                {
                    BlockPos offset = currentRoom.getPosition().relative(dir);
                    if(getLayout().contains(offset))
                    {
                        MansionRoom offsetRoom = getLayout().get(offset);
                        if(offsetRoom.active && offsetRoom.getRoomType() == RoomType.ROOM)
                        {
                            if(random.nextFloat() < 0.25F)
                            {
                                BigMansionRoom bigRoom = new BigMansionRoom(currentRoom.getPosition(), dir, false);
                                BigMansionRoom bigRoomDummy = new BigMansionRoom(offset, dir.getOpposite(), true);

                                bigRoom.setLayout(currentRoom);
                                bigRoomDummy.setLayout(offsetRoom);

                                currentRoom.active = false;
                                offsetRoom.active = false;

                                getLayout().put(currentRoom.getPosition(), bigRoom);
                                getLayout().put(offset, bigRoomDummy);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    protected void createEntrance(RandomSource random, List<MansionRoom> allRooms)
    {
        MansionRoom entranceConnected = null;
        Direction offsetDirection = null;
        List<MansionRoom> possibleRooms = allRooms.stream().filter(room -> room.getPosition().getY() == 0 && room.getRoomType().hasWalls()).collect(Collectors.toList());
        Collections.shuffle(possibleRooms);

        for(MansionRoom room : possibleRooms)
        {
            if(room.getPosition().getY() != 0 || !room.getRoomType().hasWalls())
                continue;

            Direction outSide = getOutsideDirection(room.getPosition());
            if(outSide != null)
            {
                entranceConnected = room;
                offsetDirection = outSide;
                break;
            }
        }

        BlockPos entrancePos = entranceConnected.getPosition().relative(offsetDirection);
        MansionRoom entranceRoom = new EntranceRoom(entrancePos, RoomType.ENTRANCE);
        entranceRoom.getLayout().put(offsetDirection.getOpposite(), true);
        entranceConnected.getLayout().put(offsetDirection, true);
        layout.put(entrancePos, entranceRoom);
        allRooms.add(entranceRoom);
    }

    private Direction getOutsideDirection(BlockPos pos)
    {
        if(layout.getMinX() == pos.getX())
            return Direction.WEST;
        else if(layout.getMaxX() == pos.getX())
            return Direction.EAST;
        else if(layout.getMinZ() == pos.getZ())
            return Direction.NORTH;
        else if(layout.getMaxZ() == pos.getZ())
            return Direction.SOUTH;

        return null;
    }

    protected void createTowers(RandomSource random, List<MansionRoom> allRooms)
    {
        //Towers
        final int maxTowers = 3;
        final int maxTowerHeight = 3;

        for(int i = 1; i < maxTowers; i++)
        {
            MansionRoom room = allRooms.get(random.nextInt(allRooms.size()));
            if(room.canSupportRoof())
            {
                while(layout.contains(room.getPosition().above())) room = layout.get(room.getPosition().above());

                if(room.getRoomType().isReplaceable())
                {
                    room.setRoomType(RoomType.TOWER_BASE);
                    BlockPos towerPos = room.getPosition().above();
                    int height = 1 + random.nextInt(maxTowerHeight);
                    for(int t = 0; t < height; t++)
                    {
                        layout.put(towerPos, new MansionRoom(towerPos, RoomType.TOWER_MID));
                        towerPos = towerPos.above();
                    }
                    layout.put(towerPos, new MansionRoom(towerPos, RoomType.TOWER_TOP));
                }
            }
        }
    }

    protected void createDungeon(RandomSource random, List<MansionRoom> allRooms, int startY)
    {
        //Dungeon
        final int maxY = 30;        //Max level dungeon will generate at
        final int downStep = 7;    //Amount to step down check per floor.

        //Pick a random replaceable room to use are our stairs down.
        MansionRoom dungeonStairs;
        do{
            dungeonStairs = allRooms.get(random.nextInt(allRooms.size()));
        }while(dungeonStairs.getPosition().getY() != 0 || !dungeonStairs.getRoomType().isReplaceable()); //TODO: there's a lot of references to y == 0 in this class. Could this be the old 1.16 world limit? Change to Level.MIN_HEIGHT or w/e
        dungeonStairs.setRoomType(RoomType.DUNGEON_STAIRS_TOP);

        //See how many stairs down we need until we get to a reasonable level.
        float downDiff = startY - maxY;
        int amt = (int) Math.floor((downDiff) / downStep);

        //Create stairs down
        BlockPos dungeonPos = dungeonStairs.getPosition().below();
        for(int i = 0; i < amt - 1; i++)    //Middle stairs
        {
            MansionRoom dungeonStairsMid = new MansionRoom(dungeonPos, RoomType.DUNGEON_STAIRS_MID);
            layout.put(dungeonPos, dungeonStairsMid);
            dungeonPos = dungeonPos.below();
        }
        DungeonRoom dungeonStairsBottom = new DungeonRoom(dungeonPos, RoomType.DUNGEON_STAIRS_BOTTOM);  //Stairs bottom
        layout.put(dungeonPos, dungeonStairsBottom);

        BlockPos dungeonStart = new BlockPos(dungeonPos);   //Dungeon start position (bottom of stairs)

        //Boss room will be at the end of a corridor, this is always straight from the stairs to ensure no collisions with any other room.
        Direction bossDir = BMUtil.randomHorizontal();  //Direction to boss room
        List<MansionRoom> dungeonRooms = Lists.newArrayList();
        dungeonRooms.add(dungeonStairsBottom);
        int dungeonCorridorLength = RandomUtil.randomRange(4, 6); //Length of the corridor
        for(int i = 1; i < dungeonCorridorLength; i++)  //Create corridor rooms
        {
            BlockPos roomPos = dungeonStart.relative(bossDir, i);
            MansionRoom room = new DungeonRoom(roomPos, RoomType.DUNGEON_ROOM);
            room.setLayoutType(LayoutType.REQUIRED);
            room.getLayout().put(bossDir, true);
            layout.put(roomPos, room);
            dungeonRooms.add(room);
        }

        //Boss Room
        BlockPos bossPos = dungeonStart.relative(bossDir, dungeonCorridorLength + 1);
        BossRoom bossRoom = new BossRoom(bossPos);
        bossRoom.getLayout().put(bossDir.getOpposite(), true);
        layout.put(bossPos, bossRoom);

        //Extra dungeon rooms
        for(int i = 0; i < 10; i++)
        {
            MansionRoom rm;
            do{
                rm =  dungeonRooms.get(random.nextInt(dungeonRooms.size()));
            }while(rm.getPosition().equals(dungeonPos.relative(bossDir, dungeonCorridorLength - 1))); //Ensures there's no rooms on the furthest position to avoid collisions.

            Direction dir;
            do{
                dir = BMUtil.randomHorizontal();
            }while(dir == bossDir || dir == bossDir.getOpposite()); //Only create rooms sideways.

            //Place room
            BlockPos offPos = rm.getPosition().relative(dir);
            if(!layout.contains(offPos))
            {
                MansionRoom offsetRoom = new DungeonRoom(offPos, RoomType.DUNGEON_ROOM);
                offsetRoom.setLayoutType(LayoutType.REQUIRED);
                layout.put(offPos, offsetRoom);
                dungeonRooms.add(offsetRoom);
            }
        }
        dungeonRooms.forEach(rm->rm.setLayout(this, random));
    }

    public List<MansionRoom> placeCorridors(int y, int maxCount, List<BlockPos.MutableBlockPos> positions, RandomSource random)
    {
        int attempts = 20;
        int placed = 0;
        List<MansionRoom> corridors = Lists.newArrayList();
        for(BlockPos p : positions)
        {
            MansionRoom room = layout.get(p.immutable());
            if(room != null) corridors.add(room);
        }

        BlockPos.MutableBlockPos pos;
        if(y == 0) pos = positions.get(0);
        else pos = corridors.get(random.nextInt(corridors.size())).getPosition().mutable();
        while(placed < maxCount && attempts > 0)
        {
            if(!layout.contains(pos))
            {
                //attempts = 20;
                if(y != 0 && !layout.contains(pos.below()))
                {
                    setNextPos(corridors, random, y, pos);
                    continue;
                }
                if(!(y != 0 && !layout.get(pos.below()).canSupportRoof()))
                {
                    MansionRoom room = new MansionRoom(pos.immutable(), RoomType.CORRIDOR);
                    room.setLayoutType(LayoutType.REQUIRED);
                    layout.put(pos.immutable(), room);
                    corridors.add(room);
                    placed++;
                }
            }
            setNextPos(corridors, random, y, pos);
            attempts--;
        }
        return corridors;
    }

    private void setNextPos(List<MansionRoom> corridors, RandomSource random, int y, BlockPos.MutableBlockPos pos)
    {
        BlockPos nextPos = corridors.get(random.nextInt(corridors.size())).getPosition();
        BlockPos checkPos = nextPos;
        for(Direction dir : BMUtil.randomOrderedHorizontals())
        {
            checkPos = nextPos.relative(dir);
            if(y == 0 || (layout.contains(checkPos.below()) && layout.get(checkPos.below()).canSupportRoof())) break;
        }
        pos.set(checkPos.getX(), checkPos.getY(), checkPos.getZ());
    }

    public List<MansionRoom> placeRooms(int y, int maxCount, RandomSource random)
    {
        int roomsPlaced = 0;
        int roomTarget = maxCount;
        int attempts = 500;
        List<MansionRoom> rooms = Lists.newArrayList();
        BlockPos lastSuccess = null;

        while(attempts > 0 && roomsPlaced < roomTarget)
        {
            BlockPos.MutableBlockPos randomPos = new BlockPos.MutableBlockPos(RandomUtil.randomRange(layout.getMinX(), layout.getMaxX()), y, RandomUtil.randomRange(layout.getMinZ(), layout.getMaxZ()));

            if(lastSuccess != null && random.nextFloat() < 0.10F)
            {
                randomPos = new BlockPos.MutableBlockPos(lastSuccess.getX(), lastSuccess.getY(), lastSuccess.getZ());
            }

            if(layout.contains(randomPos))
            {
                if(y != 0 && !layout.contains(randomPos.below())) continue;

                MansionRoom existingRoom = layout.get(randomPos);
                randomPos = randomPos.move(Direction.from2DDataValue(random.nextInt(4)));
                if(!layout.contains(randomPos))
                {
                    if(y == 0 || (layout.contains(randomPos.below()) && layout.get(randomPos.below()).canSupportRoof()))
                    {
                        MansionRoom newRoom = new MansionRoom(randomPos.immutable(), RoomType.ROOM);
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