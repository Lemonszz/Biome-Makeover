package party.lemons.biomemakeover.init;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.block.DirectionalDataBlock;
import party.lemons.biomemakeover.block.blockentity.*;
import party.lemons.biomemakeover.util.RegistryHelper;

public class BMBlockEntities
{
	public static final BlockEntityType<PoltergeistBlockEntity> POLTERGEIST = BlockEntityType.Builder.create(PoltergeistBlockEntity::new, BMBlocks.POLTERGEIST).build(null);
	public static final BlockEntityType<LightningBugBottleBlockEntity> LIGHTNING_BUG_BOTTLE = BlockEntityType.Builder.create(LightningBugBottleBlockEntity::new, BMBlocks.LIGHTNING_BUG_BOTTLE).build(null);
	public static final BlockEntityType<AltarBlockEntity> ALTAR = BlockEntityType.Builder.create(AltarBlockEntity::new, BMBlocks.ALTAR).build(null);
	public static final BlockEntityType<TapestryBlockEntity> TAPESTRY = BlockEntityType.Builder.create(TapestryBlockEntity::new, BMBlocks.TAPESTRY_BLOCKS.toArray(new Block[0])).build(null);
	public static final BlockEntityType<DirectionalDataBlockEntity> DIRECTIONAL_DATA = BlockEntityType.Builder.create(DirectionalDataBlockEntity::new, BMBlocks.DIRECTIONAL_DATA).build(null);

	public static void init()
	{
		RegistryHelper.register(Registry.BLOCK_ENTITY_TYPE, BlockEntityType.class, BMBlockEntities.class);
	}
}
