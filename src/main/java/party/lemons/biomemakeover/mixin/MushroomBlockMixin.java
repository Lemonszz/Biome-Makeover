package party.lemons.biomemakeover.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.sound.BlockSoundGroup;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MushroomBlock.class)
public abstract class MushroomBlockMixin extends Block
{
    public MushroomBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public BlockSoundGroup getSoundGroup(BlockState state) {
        return BlockSoundGroup.FUNGUS;
    }
}
