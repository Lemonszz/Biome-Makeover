package party.lemons.biomemakeover.level.feature.mansion.room;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import party.lemons.biomemakeover.level.feature.mansion.MansionFeature;
import party.lemons.biomemakeover.level.feature.mansion.RoomType;
import party.lemons.biomemakeover.util.Grid;

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
        this.partnerPos = position.relative(partnerDirection);
    }

    @Override
    public Rotation getRotation(RandomSource random)
    {
        if(isDummy())
            return Rotation.NONE;

        switch(partnerDirection)
        {
            case NORTH:
                return Rotation.COUNTERCLOCKWISE_90;
            case SOUTH:
                return Rotation.CLOCKWISE_90;
            case WEST:
                return Rotation.CLOCKWISE_180;
            case EAST:
                return Rotation.NONE;
        }

        return Rotation.NONE;
    }

    @Override
    public ResourceLocation getTemplate(RandomSource random)
    {
        if(isDummy())
            return MansionFeature.EMPTY;

        return MansionFeature.ROOM_BIG.get(random.nextInt(MansionFeature.ROOM_BIG.size()));
    }

    public boolean isDummy()
    {
        return isDummy;
    }

    @Override
    public void addWalls(RandomSource random, BlockPos wallPos, StructureTemplateManager manager, Grid<MansionRoom> roomGrid, StructurePiecesBuilder children) {
        boolean ground = getPosition().getY() == 0;

        if(getRoomType().hasWalls())
        {
            if(isConnected(Direction.NORTH))
                addWall(Direction.NORTH, manager, children, getInnerWall(random),  wallPos.relative(Direction.NORTH, 2), Rotation.NONE, ground);
            else if(!roomGrid.contains(getPosition().north()) || !roomGrid.get(getPosition().north()).getRoomType().hasWalls())
                children.addPiece(new MansionFeature.Piece(manager, getOuterWall(Direction.NORTH, roomGrid, random), wallPos.relative(Direction.EAST, 11), Rotation.CLOCKWISE_180, ground, true));
            else if(roomGrid.contains(getPosition().north()))
                addWall(Direction.NORTH, manager, children, getFlatWall(random),  wallPos.relative(Direction.NORTH, 2), Rotation.NONE, ground);

            if(isConnected(Direction.WEST))
                addWall(Direction.WEST, manager, children, getInnerWall(random),  wallPos, Rotation.CLOCKWISE_90, ground);
            else if(!roomGrid.contains(getPosition().west()) || !roomGrid.get(getPosition().west()).getRoomType().hasWalls())
                children.addPiece(new MansionFeature.Piece(manager, getOuterWall(Direction.WEST, roomGrid, random), wallPos.north(), Rotation.CLOCKWISE_90, ground, true));
            else if(roomGrid.contains(getPosition().west()))
                addWall(Direction.WEST, manager, children, getFlatWall(random),  wallPos, Rotation.CLOCKWISE_90, ground);

            if(!roomGrid.contains(getPosition().east()) || !roomGrid.get(getPosition().east()).getRoomType().hasWalls())
                children.addPiece(new MansionFeature.Piece(manager, getOuterWall(Direction.EAST, roomGrid, random), wallPos.relative(Direction.EAST, 11).west().south(11), Rotation.COUNTERCLOCKWISE_90, ground, true));
            if(!roomGrid.contains(getPosition().south()) || !roomGrid.get(getPosition().south()).getRoomType().hasWalls())
                children.addPiece(new MansionFeature.Piece(manager, getOuterWall(Direction.SOUTH, roomGrid, random), wallPos.relative(Direction.SOUTH, 10).west(), Rotation.NONE, ground, true));

            BlockPos cornerPos1 = getPosition().relative(Direction.NORTH).relative(Direction.WEST);
            if(roomGrid.contains(cornerPos1) && roomGrid.get(cornerPos1).getRoomType().hasWalls())
                children.addPiece(new MansionFeature.Piece(manager, MansionFeature.CORNER_FILLER.toString(), wallPos.relative(Direction.WEST).relative(Direction.NORTH).offset(0, 0, 0), Rotation.NONE, ground, false));
        }
    }

    private void addWall(Direction direction, StructureTemplateManager manager, StructurePiecesBuilder children, String wall, BlockPos pos, Rotation rotation, boolean ground)
    {
        if(direction == partnerDirection)
            return;

        else
            children.addPiece(new MansionFeature.Piece(manager, wall, pos, rotation, ground, false));
    }
}