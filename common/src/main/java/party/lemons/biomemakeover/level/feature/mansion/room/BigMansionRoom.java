package party.lemons.biomemakeover.level.feature.mansion.room;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import party.lemons.biomemakeover.level.feature.mansion.*;
import party.lemons.taniwha.util.collections.Grid;

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
    public ResourceLocation getTemplate(MansionTemplates templates, RandomSource random)
    {
        if(isDummy())
            return MansionTemplateType.EMPTIES.getRandomTemplate(templates, random);

        return MansionTemplateType.ROOMS_BIG.getRandomTemplate(templates, random);
    }

    public boolean isDummy()
    {
        return isDummy;
    }

    @Override
    public void addWalls(MansionDetails details, MansionTemplates templates, RandomSource random, BlockPos wallPos, StructureTemplateManager manager, Grid<MansionRoom> roomGrid, StructurePiecesBuilder children) {
        boolean ground = getPosition().getY() == 0;

        if(getRoomType().hasWalls())
        {
            if(isConnected(Direction.NORTH))
                addWall(details, Direction.NORTH, manager, children, getInnerWall(templates, random),  wallPos.relative(Direction.NORTH, 2), Rotation.NONE, ground);
            else if(!roomGrid.contains(getPosition().north()) || !roomGrid.get(getPosition().north()).getRoomType().hasWalls())
                children.addPiece(new MansionFeature.Piece(details, manager, getOuterWall(templates, Direction.NORTH, roomGrid, random), wallPos.relative(Direction.EAST, 11), Rotation.CLOCKWISE_180, ground, true));
            else if(roomGrid.contains(getPosition().north()))
                addWall(details, Direction.NORTH, manager, children, getFlatWall(templates, random),  wallPos.relative(Direction.NORTH, 2), Rotation.NONE, ground);

            if(isConnected(Direction.WEST))
                addWall(details, Direction.WEST, manager, children, getInnerWall(templates, random),  wallPos, Rotation.CLOCKWISE_90, ground);
            else if(!roomGrid.contains(getPosition().west()) || !roomGrid.get(getPosition().west()).getRoomType().hasWalls())
                children.addPiece(new MansionFeature.Piece(details, manager, getOuterWall(templates, Direction.WEST, roomGrid, random), wallPos.north(), Rotation.CLOCKWISE_90, ground, true));
            else if(roomGrid.contains(getPosition().west()))
                addWall(details, Direction.WEST, manager, children, getFlatWall(templates, random),  wallPos, Rotation.CLOCKWISE_90, ground);

            if(!roomGrid.contains(getPosition().east()) || !roomGrid.get(getPosition().east()).getRoomType().hasWalls())
                children.addPiece(new MansionFeature.Piece(details, manager, getOuterWall(templates, Direction.EAST, roomGrid, random), wallPos.relative(Direction.EAST, 11).west().south(11), Rotation.COUNTERCLOCKWISE_90, ground, true));
            if(!roomGrid.contains(getPosition().south()) || !roomGrid.get(getPosition().south()).getRoomType().hasWalls())
                children.addPiece(new MansionFeature.Piece(details, manager, getOuterWall(templates, Direction.SOUTH, roomGrid, random), wallPos.relative(Direction.SOUTH, 10).west(), Rotation.NONE, ground, true));

            BlockPos cornerPos1 = getPosition().relative(Direction.NORTH).relative(Direction.WEST);
            if(roomGrid.contains(cornerPos1) && roomGrid.get(cornerPos1).getRoomType().hasWalls())
                children.addPiece(new MansionFeature.Piece(details, manager, MansionTemplateType.CORNER_FILLERS.getRandomTemplate(templates, random).toString(), wallPos.relative(Direction.WEST).relative(Direction.NORTH).offset(0, 0, 0), Rotation.NONE, ground, false));
        }
    }

    private void addWall(MansionDetails details, Direction direction, StructureTemplateManager manager, StructurePiecesBuilder children, String wall, BlockPos pos, Rotation rotation, boolean ground)
    {
        if(direction == partnerDirection)
            return;

        else
            children.addPiece(new MansionFeature.Piece(details, manager, wall, pos, rotation, ground, false));
    }
}