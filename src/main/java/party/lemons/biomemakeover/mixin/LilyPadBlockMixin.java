package party.lemons.biomemakeover.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.sound.BlockSoundGroup;
import org.spongepowered.asm.mixin.Mixin;
import party.lemons.biomemakeover.init.BMBlocks;

@Mixin(LilyPadBlock.class)
public abstract class LilyPadBlockMixin extends Block
{
	public LilyPadBlockMixin(Settings settings)
	{
		super(settings);
	}

	@Override
	public BlockSoundGroup getSoundGroup(BlockState state) {
		return BMBlocks.BM_LILY_PAD_SOUNDS;
	}
}
