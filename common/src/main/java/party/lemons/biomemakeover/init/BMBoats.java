package party.lemons.biomemakeover.init;

import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.taniwha.block.WoodBlockFactory;
import party.lemons.taniwha.entity.boat.BoatShape;
import party.lemons.taniwha.entity.boat.BoatType;
import party.lemons.taniwha.entity.boat.BoatTypes;

public class BMBoats
{
	public static final BoatType BLIGHTED_BALSA = new BoatType(BiomeMakeover.ID("blighted_balsa"), BoatTypes.REGULAR_SHAPE, ()-> BMBlocks.BLIGHTED_BALSA_WOOD_INFO.getItem(WoodBlockFactory.Type.BOAT).get(), ()-> BMBlocks.BLIGHTED_BALSA_WOOD_INFO.getItem(WoodBlockFactory.Type.CHEST_BOAT).get());
	public static final BoatType WILLOW = new BoatType(BiomeMakeover.ID("willow"),  BoatTypes.REGULAR_SHAPE, ()->BMBlocks.WILLOW_WOOD_INFO.getItem(WoodBlockFactory.Type.BOAT).get(), ()->BMBlocks.WILLOW_WOOD_INFO.getItem(WoodBlockFactory.Type.CHEST_BOAT).get());
	public static final BoatType SWAMP_CYPRESS = new BoatType(BiomeMakeover.ID("swamp_cypress"),  BoatTypes.REGULAR_SHAPE, ()->BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getItem(WoodBlockFactory.Type.BOAT).get(), ()->BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getItem(WoodBlockFactory.Type.CHEST_BOAT).get());
	public static final BoatType ANCIENT_OAK = new BoatType(BiomeMakeover.ID("ancient_oak"),  BoatTypes.REGULAR_SHAPE, ()->BMBlocks.ANCIENT_OAK_WOOD_INFO.getItem(WoodBlockFactory.Type.BOAT).get(), ()->BMBlocks.ANCIENT_OAK_WOOD_INFO.getItem(WoodBlockFactory.Type.CHEST_BOAT).get());


	public static void init()
	{
		//NOFU
	}
}
