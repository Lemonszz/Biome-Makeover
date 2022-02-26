package party.lemons.biomemakeover.init;

import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.taniwha.block.WoodBlockFactory;
import party.lemons.taniwha.entity.boat.BoatType;

public class BMBoatTypes
{
    public static final BoatType BLIGHTED_BALSA = new BoatType(BiomeMakeover.ID("blighted_balsa"), ()-> BMBlocks.BLIGHTED_BALSA_WOOD_INFO.getItem(WoodBlockFactory.Type.BOAT));
    public static final BoatType WILLOW = new BoatType(BiomeMakeover.ID("willow"), ()->BMBlocks.WILLOW_WOOD_INFO.getItem(WoodBlockFactory.Type.BOAT));
    public static final BoatType SWAMP_CYPRESS = new BoatType(BiomeMakeover.ID("swamp_cypress"), ()->BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getItem(WoodBlockFactory.Type.BOAT));
    public static final BoatType ANCIENT_OAK = new BoatType(BiomeMakeover.ID("ancient_oak"), ()->BMBlocks.ANCIENT_OAK_WOOD_INFO.getItem(WoodBlockFactory.Type.BOAT));

}
