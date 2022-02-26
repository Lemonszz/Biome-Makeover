package party.lemons.biomemakeover.init;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.block.blockentity.*;
import party.lemons.taniwha.registry.RegistryHelper;

public class BMBlockEntities
{
    public static final BlockEntityType<PoltergeistBlockEntity> POLTERGEIST = BlockEntityType.Builder.of(PoltergeistBlockEntity::new, BMBlocks.POLTERGEIST).build(null);
    public static final BlockEntityType<LightningBugBottleBlockEntity> LIGHTNING_BUG_BOTTLE = BlockEntityType.Builder.of(LightningBugBottleBlockEntity::new, BMBlocks.LIGHTNING_BUG_BOTTLE).build(null);
    public static final BlockEntityType<TapestryBlockEntity> TAPESTRY = BlockEntityType.Builder.of(TapestryBlockEntity::new, BMBlocks.TAPESTRY_BLOCKS.toArray(new Block[0])).build(null);
    public static final BlockEntityType<AltarBlockEntity> ALTAR = BlockEntityType.Builder.of(AltarBlockEntity::new, BMBlocks.ALTAR).build(null);
    public static final BlockEntityType<DirectionalDataBlockEntity> DIRECTIONAL_DATA = BlockEntityType.Builder.of(DirectionalDataBlockEntity::new, BMBlocks.DIRECTIONAL_DATA).build(null);

    public static void init()
    {
        RegistryHelper.register(Constants.MOD_ID, Registry.BLOCK_ENTITY_TYPE, BlockEntityType.class, BMBlockEntities.class);
    }
}
