package party.lemons.biomemakeover.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.HoeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Set;

@Mixin(HoeItem.class)
public class HoeItemMixin
{
    @Shadow @Mutable private static Set<Block> EFFECTIVE_BLOCKS;

    @Inject(at = @At("RETURN"), method = "<clinit>")
    private static void onStaticInit(CallbackInfo cbi)
    {
        EFFECTIVE_BLOCKS = Sets.newHashSet(EFFECTIVE_BLOCKS);

        EFFECTIVE_BLOCKS.addAll(Lists.newArrayList(
                Blocks.MUSHROOM_STEM,
                Blocks.RED_MUSHROOM_BLOCK,
                Blocks.BROWN_MUSHROOM_BLOCK,
                BMBlocks.GLOWSHROOM_STEM,
                BMBlocks.PURPLE_GLOWSHROOM_BLOCK,
                BMBlocks.ORANGE_GLOWSHROOM_BLOCK,
                BMBlocks.GREEN_GLOWSHROOM_BLOCK
        ));
    }

}
