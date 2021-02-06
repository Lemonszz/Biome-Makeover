package party.lemons.biomemakeover.util.boat;

import com.mojang.serialization.Lifecycle;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Items;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.RegistryHelper;
import party.lemons.biomemakeover.util.WoodTypeInfo;

public class BoatTypes
{
	public static final RegistryKey<Registry<BoatType>> REG_KEY = RegistryKey.ofRegistry(BiomeMakeover.ID("boat_type"));
	public static final MutableRegistry<BoatType> REGISTRY = new SimpleRegistry<>(REG_KEY, Lifecycle.stable());

	static
	{
		((MutableRegistry) Registry.REGISTRIES).add(REG_KEY, REGISTRY, Lifecycle.stable());
	}

	public static final BoatType BLIGHTED_BALSA = new BoatType(BiomeMakeover.ID("blighted_balsa"), ()->BMBlocks.BLIGHTED_BALSA_WOOD_INFO.getItem(WoodTypeInfo.Type.BOAT));
	public static final BoatType WILLOW = new BoatType(BiomeMakeover.ID("willow"), ()->BMBlocks.WILLOW_WOOD_INFO.getItem(WoodTypeInfo.Type.BOAT));
	public static final BoatType SWAMP_CYPRESS = new BoatType(BiomeMakeover.ID("swamp_cypress"), ()->BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getItem(WoodTypeInfo.Type.BOAT));
	public static final BoatType ANCIENT_OAK = new BoatType(BiomeMakeover.ID("ancient_oak"), ()->BMBlocks.ANCIENT_OAK_WOOD_INFO.getItem(WoodTypeInfo.Type.BOAT));

	//Vanilla Types
	public static final BoatType ACACIA = new VanillaBoatType(BiomeMakeover.ID("acacia"), BoatEntity.Type.ACACIA, Items.ACACIA_BOAT);
	public static final BoatType BIRCH = new VanillaBoatType(BiomeMakeover.ID("birch"), BoatEntity.Type.BIRCH, Items.BIRCH_BOAT);
	public static final BoatType DARK_OAK = new VanillaBoatType(BiomeMakeover.ID("dark_oak"), BoatEntity.Type.DARK_OAK, Items.DARK_OAK_BOAT);
	public static final BoatType JUNGLE = new VanillaBoatType(BiomeMakeover.ID("jungle"), BoatEntity.Type.JUNGLE, Items.JUNGLE_BOAT);
	public static final BoatType OAK = new VanillaBoatType(BiomeMakeover.ID("oak"), BoatEntity.Type.OAK, Items.OAK_BOAT);
	public static final BoatType SPRUCE = new VanillaBoatType(BiomeMakeover.ID("spruce"), BoatEntity.Type.SPRUCE, Items.SPRUCE_BOAT);

	public static void init()
	{
		RegistryHelper.register(REGISTRY, BoatType.class, BoatTypes.class);
	}

	public static BoatType getVanillaType(BoatEntity.Type boatType)
	{
		for(BoatType t : REGISTRY)
		{
			if(t instanceof VanillaBoatType)
			{
				if(((VanillaBoatType) t).getVanillaType() == boatType) return t;
			}
		}

		return null;
	}
}