package party.lemons.biomemakeover.compat.moretags.fabric;

import net.gudenau.minecraft.moretags.MoreTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MoreTagsCompatImpl {
    public static boolean CropMayPlaceOn(BlockState blockState) {
        return true;
    }


    public static boolean CropIsFarmland(BlockState blockState) {
        return blockState.is(MoreTags.FARMLAND);
    }
}
