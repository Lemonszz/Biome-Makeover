package party.lemons.biomemakeover.init;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.block.blockentity.PoltergeistBlockEntity;
import party.lemons.biomemakeover.util.RegistryHelper;

public class BMBlockEntities
{
	public static final BlockEntityType<PoltergeistBlockEntity> POLTERGEIST = BlockEntityType.Builder.create(PoltergeistBlockEntity::new, BMBlocks.POLTERGEIST).build(null);

	public static void init()
	{
		RegistryHelper.register(Registry.BLOCK_ENTITY_TYPE, BlockEntityType.class, BMBlockEntities.class);
	}
}
