package party.lemons.biomemakeover.mixin.mushroom;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HugeMushroomBlock.class)
public abstract class HugeMushroomBlockMixin extends Block {
    public HugeMushroomBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public SoundType getSoundType(BlockState blockState) {
        return SoundType.FUNGUS;
    }
}
