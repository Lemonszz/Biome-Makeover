package party.lemons.biomemakeover.compat.moretags.fabric;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MoreTagsCompatImpl {
    public static boolean CropMayPlaceOn(BlockState blockState) {
        return true;
    }

    private static TagKey<Block> FARMLAND;

    public static boolean CropIsFarmland(BlockState blockState) {
        return blockState.is(farmland());
    }

    public static TagKey<Block> farmland()
    {
        if(FARMLAND != null)
            return FARMLAND;

        FARMLAND = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("c", "farmland"));
        return FARMLAND;
    }
}
