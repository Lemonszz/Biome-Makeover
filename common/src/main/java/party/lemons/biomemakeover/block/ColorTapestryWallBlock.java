package party.lemons.biomemakeover.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class ColorTapestryWallBlock extends AbstractTapestryWallBlock {
    private final DyeColor color;

    public ColorTapestryWallBlock(DyeColor color, Properties properties) {
        super(properties);
        this.color = color;
    }

    @Override
    public ResourceLocation getRenderTexture() {
        return ColorTapestryBlock.TEXTURES.get(color);
    }
}