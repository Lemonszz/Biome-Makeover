package party.lemons.biomemakeover.level.feature.mansion.room;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import party.lemons.biomemakeover.level.feature.mansion.MansionFeature;
import party.lemons.biomemakeover.level.feature.mansion.RoomType;
import party.lemons.taniwha.util.collections.Grid;

import java.util.Random;

public class EntranceRoom extends MansionRoom
{
    public EntranceRoom(BlockPos position, RoomType type)
    {
        super(position, type);
        setSortValue(1);
    }

    @Override
    public void addWalls(Random random, BlockPos wallPos, StructureManager manager, Grid<MansionRoom> roomGrid, StructurePiecesBuilder children) {
        boolean ground = getPosition().getY() == 0;

        if(getRoomType().hasWalls())
        {
            if(isConnected(Direction.NORTH))
                children.addPiece(new MansionFeature.Piece(manager, getInnerWall(random), wallPos.relative(Direction.NORTH, 2), Rotation.NONE, ground, false));

            if(isConnected(Direction.WEST))
                children.addPiece(new MansionFeature.Piece(manager, getInnerWall(random), wallPos, Rotation.CLOCKWISE_90, ground, false));

            BlockPos cornerPos1 = getPosition().relative(Direction.NORTH).relative(Direction.WEST);
            if(roomGrid.contains(cornerPos1) && roomGrid.get(cornerPos1).getRoomType().hasWalls())
                children.addPiece(new MansionFeature.Piece(manager, MansionFeature.CORNER_FILLER.toString(), wallPos.relative(Direction.WEST).relative(Direction.NORTH).offset(0, 0, 0), Rotation.NONE, ground, false));
        }
    }
    public Rotation getRotation(Random random)
    {
        switch(layout.doorCount())
        {
            case 1:
                if(layout.get(Direction.NORTH)) return Rotation.NONE;
                else if(layout.get(Direction.SOUTH)) return Rotation.CLOCKWISE_180;
                else if(layout.get(Direction.EAST)) return Rotation.CLOCKWISE_90;
                else if(layout.get(Direction.WEST)) return Rotation.COUNTERCLOCKWISE_90;
        }
        return Rotation.NONE;
    }

    public BlockPos getOffsetForRotation(BlockPos offsetPos, Rotation rotation)
    {
        switch(rotation)
        {
            case CLOCKWISE_180:
                return offsetPos.offset(10, 0, 10);
            case CLOCKWISE_90:
                return offsetPos.offset(10, 0, 0);
            case NONE:
                return offsetPos.offset(0, 0, 0);
            case COUNTERCLOCKWISE_90:
                return offsetPos.offset(0, 0, 10);
        }
        return offsetPos;
    }
}