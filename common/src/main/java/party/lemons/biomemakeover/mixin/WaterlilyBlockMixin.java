package party.lemons.biomemakeover.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import party.lemons.biomemakeover.init.BMBlocks;

@Mixin(WaterlilyBlock.class)
public class WaterlilyBlockMixin extends Block
{
    public WaterlilyBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public SoundType getSoundType(BlockState blockState) {
        return BMBlocks.BM_LILY_PAD_SOUND;
    }
}
