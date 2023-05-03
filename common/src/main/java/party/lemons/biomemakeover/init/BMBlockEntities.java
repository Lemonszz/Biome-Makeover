package party.lemons.biomemakeover.init;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.block.blockentity.*;

import java.util.function.Supplier;

public class BMBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final Supplier<BlockEntityType<PoltergeistBlockEntity>> POLTERGEIST = BLOCK_ENTITIES.register(BiomeMakeover.ID("poltergeist"), ()->BlockEntityType.Builder.of(PoltergeistBlockEntity::new, BMBlocks.POLTERGEIST.get()).build(null));
    public static final Supplier<BlockEntityType<LightningBugBottleBlockEntity>> LIGHTNING_BUG_BOTTLE = BLOCK_ENTITIES.register(BiomeMakeover.ID("lightning_bug_bottle"), ()->BlockEntityType.Builder.of(LightningBugBottleBlockEntity::new, BMBlocks.LIGHTNING_BUG_BOTTLE.get()).build(null));
    public static final Supplier<BlockEntityType<TapestryBlockEntity>> TAPESTRY = BLOCK_ENTITIES.register(BiomeMakeover.ID("tapestry"), ()->BlockEntityType.Builder.of(TapestryBlockEntity::new, BMBlocks.TAPESTRIES.getBlocks().stream().map(Supplier::get).toList().toArray(new Block[0])).build(null));
    public static final Supplier<BlockEntityType<AltarBlockEntity>> ALTAR = BLOCK_ENTITIES.register(BiomeMakeover.ID("altar"), ()->BlockEntityType.Builder.of(AltarBlockEntity::new, BMBlocks.ALTAR.get()).build(null));
    public static final Supplier<BlockEntityType<DirectionalDataBlockEntity>> DIRECTIONAL_DATA = BLOCK_ENTITIES.register(BiomeMakeover.ID("directional_data"), ()->BlockEntityType.Builder.of(DirectionalDataBlockEntity::new, BMBlocks.DIRECTIONAL_DATA.get()).build(null));

    public static void init()
    {
        BLOCK_ENTITIES.register();
    }
}
