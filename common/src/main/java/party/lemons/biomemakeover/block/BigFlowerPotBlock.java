package party.lemons.biomemakeover.block;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.taniwha.block.types.TBlock;
import party.lemons.taniwha.util.ItemUtil;
import party.lemons.taniwha.util.LevelUtil;

import java.util.Map;
import java.util.function.Supplier;

public class BigFlowerPotBlock extends TBlock {

    private static final VoxelShape INSIDE = box(1.0, 1.0, 1.0, 15.0, 16.0, 15.0);
    protected static final VoxelShape SHAPE_EMPTY = Shapes.join(
            Shapes.block(),
            INSIDE,
            BooleanOp.ONLY_FIRST
    );

    private static boolean initialised = false;
    private static final Map<ItemLike, BigFlowerPotBlock> BIG_POT_BLOCKS = Maps.newHashMap();
    private static final Map<Supplier<ItemLike>, BigFlowerPotBlock> BIG_POT_BLOCK_SUPPLIERS = Maps.newHashMap();
    protected ItemLike fillItem;
    private boolean isEmpty;


    public BigFlowerPotBlock(Supplier<ItemLike> placeItem, Properties properties) {
        super(properties);
        BIG_POT_BLOCK_SUPPLIERS.put(placeItem, this);
        isEmpty = false;
    }

    public BigFlowerPotBlock(Properties properties)
    {
        this(()-> Items.AIR, properties);
        isEmpty = true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return isEmpty ? SHAPE_EMPTY : super.getShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.block();
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(BMBlocks.BIG_FLOWER_POT.get());
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult)
    {
        checkInit();

        if(player.blockPosition().equals(pos))
            return InteractionResult.CONSUME;

        ItemStack heldItem = player.getItemInHand(interactionHand);
        Item item = heldItem.getItem();

        BigFlowerPotBlock targetBlock = BIG_POT_BLOCKS.getOrDefault(item, null);

        if(this.fillItem.asItem() == Items.AIR)
        {
            if(targetBlock != null && targetBlock != this) {
                LevelUtil.playBlockPlaceSound(level, getBlockForSounds(targetBlock.fillItem), pos);
                level.setBlock(pos, targetBlock.defaultBlockState(),  Block.UPDATE_ALL);
                ItemUtil.consumeItem(player, heldItem);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        else
        {
            if(blockHitResult.getDirection() == Direction.UP && item instanceof BlockItem blockItem)
            {
                if(blockItem.getBlock().canSurvive(blockItem.getBlock().defaultBlockState(), level, pos.above()))
                    return InteractionResult.PASS;
            }

            ItemStack returnStack = new ItemStack(fillItem);
            ItemUtil.giveOrDropItem(player, interactionHand, returnStack);

            LevelUtil.playBlockBreakSound(level, getBlockForSounds(fillItem), pos);
            level.setBlock(pos, BMBlocks.BIG_FLOWER_POT.get().defaultBlockState(), Block.UPDATE_ALL);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

            return InteractionResult.SUCCESS;
        }
    }

    private static void checkInit()
    {
        if(initialised)
            return;

        initialised = true;
        BIG_POT_BLOCK_SUPPLIERS.forEach((itemLikeSupplier, bigFlowerPotBlock) -> registerPot(itemLikeSupplier.get().asItem(), bigFlowerPotBlock));
    }

    public static void registerPot(ItemLike dirt, BigFlowerPotBlock potBlock)
    {
        BIG_POT_BLOCKS.put(dirt, potBlock);
        potBlock.fillItem = dirt;

        DispenserBlock.registerBehavior(dirt, new BigPotDispenserBehaviour(potBlock));
    }

    private static Block getBlockForSounds(ItemLike itemLike)
    {
        if(itemLike instanceof BlockItem bi)
        {
            return bi.getBlock();
        }
        else if(itemLike instanceof Block bl)
            return bl;

        return Blocks.DIRT;
    }

    private static class BigPotDispenserBehaviour extends OptionalDispenseItemBehavior
    {
        private final BigFlowerPotBlock potBlock;

        public BigPotDispenserBehaviour(BigFlowerPotBlock potBlock)
        {
            this.potBlock = potBlock;
        }

        @Override
        protected ItemStack execute(BlockSource ctx, ItemStack itemStack)
        {
            setSuccess(false);
            Item item = itemStack.getItem();
            Direction direction = ctx.getBlockState().getValue(DispenserBlock.FACING);
            BlockPos placePos = ctx.getPos().relative(direction);

            if(ctx.getLevel().getBlockState(placePos).is(BMBlocks.BIG_FLOWER_POT.get()))
            {
                LevelUtil.playBlockPlaceSound(ctx.getLevel(), getBlockForSounds(item), placePos);
                ctx.getLevel().setBlock(placePos, potBlock.defaultBlockState(),  Block.UPDATE_ALL);
                setSuccess(true);
                return itemStack;
            }

            return super.execute(ctx, itemStack);
        }
    }

    public static void bootstrap()
    {
        checkInit();
    }

    public static class BigFlowerPotMyceliumBlock extends BigFlowerPotBlock
    {

        public BigFlowerPotMyceliumBlock(Supplier<ItemLike> placeItem, Properties properties) {
            super(placeItem, properties);
        }

        @Override
        public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
            super.animateTick(blockState, level, blockPos, randomSource);
            if (randomSource.nextInt(10) == 0) {
                level.addParticle(ParticleTypes.MYCELIUM, (double)blockPos.getX() + randomSource.nextDouble(), (double)blockPos.getY() + 1.1, (double)blockPos.getZ() + randomSource.nextDouble(), 0.0, 0.0, 0.0);
            }
        }
    }
}
