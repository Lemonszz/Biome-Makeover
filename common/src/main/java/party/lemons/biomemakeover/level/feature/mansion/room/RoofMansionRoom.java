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

import java.util.List;

public class RoofMansionRoom extends NonRoofedMansionRoom
{
    public RoofMansionRoom(BlockPos position)
    {
        super(position, RoomType.ROOF);
    }

    @Override
    public int getSortValue()
    {
        return -4 + getLayout().doorCount();
    }

    @Override
    public void addWalls(MansionDetails details, MansionTemplates templates, RandomSource random, BlockPos wallPos, StructureTemplateManager manager, Grid<MansionRoom> roomGrid, StructurePiecesBuilder children) {
        if(isRoofConnected(Direction.NORTH, roomGrid))
            children.addPiece(new MansionFeature.Piece(details, manager, getRoofSplit(templates, random).toString(), wallPos.relative(Direction.NORTH).offset(-2, 0, 0), Rotation.NONE, getPosition().getY() == 0, true));
        if(isRoofConnected(Direction.WEST, roomGrid))
            children.addPiece(new MansionFeature.Piece(details, manager, getRoofSplit(templates, random).toString(), wallPos.relative(Direction.WEST).offset(0, 0, -2), Rotation.CLOCKWISE_90, getPosition().getY() == 0, true));
    }

    private ResourceLocation getRoofSplit(MansionTemplates templates, RandomSource random)
    {
        return MansionTemplateType.ROOF_SPLIT.getRandomTemplate(templates, random);
    }

    public BlockPos getOffsetForRotation(BlockPos offsetPos, Rotation rotation)
    {
        switch(layout.doorCount())
        {
            case 1:
                if(layout.get(Direction.SOUTH)) return offsetPos.offset(12, 0, 10);
                else if(layout.get(Direction.NORTH)) return offsetPos.offset(-2, 0, 0);
                else if(layout.get(Direction.EAST)) return offsetPos.offset(10, 0, -2);
                else if(layout.get(Direction.WEST)) return offsetPos.offset(0, 0, 12);
            case 2:
                if(layout.get(Direction.SOUTH) && layout.get(Direction.NORTH))
                    return offsetPos.offset(-2, 0, 0);
                else if(layout.get(Direction.SOUTH) && layout.get(Direction.EAST)) return offsetPos.offset(-2, 0, -2);
                else if(layout.get(Direction.SOUTH) && layout.get(Direction.WEST))
                    return offsetPos.offset(12, 0, -2);
                else if(layout.get(Direction.EAST) && layout.get(Direction.WEST))
                    return offsetPos.offset(0, 0, 12);
                else if(layout.get(Direction.NORTH) && layout.get(Direction.EAST)) return offsetPos.offset(-2, 0, 12);
                else if(layout.get(Direction.NORTH) && layout.get(Direction.WEST)) return offsetPos.offset(12, 0, 12);
            case 3:
                if(layout.get(Direction.NORTH) && layout.get(Direction.SOUTH) && layout.get(Direction.WEST))
                    return offsetPos.offset(12, 0, 10);
                else if(layout.get(Direction.NORTH) && layout.get(Direction.SOUTH) && layout.get(Direction.EAST))
                    return offsetPos.offset(-2, 0, 0);
                else if(layout.get(Direction.NORTH) && layout.get(Direction.EAST) && layout.get(Direction.WEST))
                    return offsetPos.offset(0, 0, 12);
                else if(layout.get(Direction.SOUTH) && layout.get(Direction.EAST) && layout.get(Direction.WEST))
                    return offsetPos.offset(10, 0, -2);
            case 4:
                return offsetPos.offset(0, 0, 0);
            case 0:
                return offsetPos.offset(-2, 0, -2);
        }
        return offsetPos;
    }

    public ResourceLocation getTemplate(MansionTemplates templates, RandomSource random)
    {
        List<ResourceLocation> ids;
        switch(layout.doorCount())
        {
            case 0:
                ids = MansionTemplateType.ROOF_0.getTemplates(templates);
                break;
            case 1:
                ids = MansionTemplateType.ROOF_1.getTemplates(templates);
                break;
            case 2:
                if((layout.get(Direction.NORTH) && layout.get(Direction.SOUTH)) || (layout.get(Direction.EAST) && layout.get(Direction.WEST)))
                    ids = MansionTemplateType.ROOF_2_STRAIGHT.getTemplates(templates);
                else ids = MansionTemplateType.ROOF_2.getTemplates(templates);;
                break;
            case 3:
                ids = MansionTemplateType.ROOF_3.getTemplates(templates);
                break;
            case 4:
                ids = MansionTemplateType.ROOF_4.getTemplates(templates);
                break;
            default:
                ids = MansionTemplateType.INNER_WALL.getTemplates(templates);
                break;
        }
        return ids.get(random.nextInt(ids.size()));
    }

    public Rotation getRotation(RandomSource random)
    {
        switch(layout.doorCount())
        {
            case 1:
                if(layout.get(Direction.SOUTH)) return Rotation.CLOCKWISE_180;
                else if(layout.get(Direction.NORTH)) return Rotation.NONE;
                else if(layout.get(Direction.EAST)) return Rotation.CLOCKWISE_90;
                else if(layout.get(Direction.WEST)) return Rotation.COUNTERCLOCKWISE_90;
            case 2:
                if(layout.get(Direction.SOUTH) && layout.get(Direction.NORTH)) return Rotation.NONE;
                else if(layout.get(Direction.SOUTH) && layout.get(Direction.EAST)) return Rotation.NONE; // !
                else if(layout.get(Direction.SOUTH) && layout.get(Direction.WEST))
                    return Rotation.CLOCKWISE_90; // ~
                else if(layout.get(Direction.EAST) && layout.get(Direction.WEST))
                    return Rotation.COUNTERCLOCKWISE_90;
                else if(layout.get(Direction.NORTH) && layout.get(Direction.EAST))
                    return Rotation.COUNTERCLOCKWISE_90;
                else if(layout.get(Direction.NORTH) && layout.get(Direction.WEST))
                    return Rotation.CLOCKWISE_180; //~ !!
            case 3:
                if(layout.get(Direction.NORTH) && layout.get(Direction.SOUTH) && layout.get(Direction.WEST))
                    return Rotation.CLOCKWISE_180;
                else if(layout.get(Direction.NORTH) && layout.get(Direction.SOUTH) && layout.get(Direction.EAST))
                    return Rotation.NONE;
                else if(layout.get(Direction.NORTH) && layout.get(Direction.EAST) && layout.get(Direction.WEST))
                    return Rotation.COUNTERCLOCKWISE_90;
                else if(layout.get(Direction.SOUTH) && layout.get(Direction.EAST) && layout.get(Direction.WEST))
                    return Rotation.CLOCKWISE_90;
            case 4:
                return Rotation.NONE;
            case 0:
                return Rotation.NONE;
        }
        return null;
    }

    @Override
    public void setLayout(MansionLayout layout, RandomSource random)
    {
        Grid<MansionRoom> lo = layout.getLayout();
        for(int i = 0; i < 4; i++)
        {
            Direction dir = Direction.from2DDataValue(i);
            MansionRoom neighbour = lo.get(getPosition().relative(dir));

            if(neighbour != null)
            {
                if(neighbour.getRoomType() == RoomType.ROOF || neighbour.getRoomType().hasWalls())
                {
                    this.getLayout().put(dir, true);
                }
            }
        }
    }

    public boolean isRoofConnected(Direction direction, Grid<MansionRoom> roomGrid)
    {
        return layout.get(direction) && roomGrid.get(getPosition().relative(direction)).getRoomType() == RoomType.ROOF;
    }

}