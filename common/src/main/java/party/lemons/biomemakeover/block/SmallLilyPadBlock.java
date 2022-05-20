package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class SmallLilyPadBlock extends WaterlilyBlock implements BlockWithItem, BlockWithModifiers<SmallLilyPadBlock>
{
    public static final IntegerProperty PADS = IntegerProperty.create("pads", 0, 3);

    public SmallLilyPadBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(PADS, 0));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult)
    {
        ItemStack stack = player.getItemInHand(hand);
        BlockPos pos = blockHitResult.getBlockPos();

        if(level.mayInteract(player, pos) && state.getValue(PADS) < 3 && !stack.isEmpty() && stack.getItem() == BMBlocks.SMALL_LILY_PAD.get().asItem())
        {
            level.setBlock(pos, state.setValue(PADS, state.getValue(PADS) + 1), 3);
            if(!player.isCreative()) stack.shrink(1);

            SoundType blockSoundGroup = state.getSoundType();
            level.playSound(player, pos, blockSoundGroup.getPlaceSound(), SoundSource.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);

            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, blockPos, player, hand, blockHitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PADS);
    }

    @Override
    public Item makeItem(CreativeModeTab group) {
        return new PlaceOnWaterBlockItem(this, makeItemSettings(group));
    }

    @Override
    public SmallLilyPadBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);

        return this;
    }
}
