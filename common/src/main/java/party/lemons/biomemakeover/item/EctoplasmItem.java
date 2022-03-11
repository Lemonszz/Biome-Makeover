package party.lemons.biomemakeover.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.init.BMBlocks;

public class EctoplasmItem extends BMItem{
    public EctoplasmItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx)
    {
        BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if(state.getBlock() == Blocks.COMPOSTER)
        {
            int compostLevel = state.getValue(ComposterBlock.LEVEL);
            if(compostLevel > 0)
            {
                if(!ctx.getLevel().isClientSide())
                {
                    ctx.getLevel().levelEvent(LevelEvent.COMPOSTER_FILL, ctx.getClickedPos(), 1);
                    ctx.getLevel().setBlock(ctx.getClickedPos(), BMBlocks.ECTOPLASM_COMPOSTER.get().defaultBlockState().setValue(ComposterBlock.LEVEL, compostLevel), 3);

                    if(ctx.getPlayer() != null && !ctx.getPlayer().isCreative())
                        ctx.getItemInHand().shrink(1);

                }
                return InteractionResult.SUCCESS;
            }
        }


        return super.useOn(ctx);
    }
}
