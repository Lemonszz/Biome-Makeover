package party.lemons.biomemakeover.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.awt.*;

public class AdjudicatorTapestryWallBlock extends AbstractTapestryWallBlock {
    public AdjudicatorTapestryWallBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ResourceLocation getRenderTexture() {
        return AdjudicatorTapestryBlock.TEXTURE;
    }
}