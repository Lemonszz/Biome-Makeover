package party.lemons.biomemakeover.compat.moretags;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MoreTagsCompat {

    @ExpectPlatform
    public static boolean CropIsFarmland(BlockState blockState)
    {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean CropMayPlaceOn(BlockState blockState)
    {
        throw new AssertionError();
    }
}
