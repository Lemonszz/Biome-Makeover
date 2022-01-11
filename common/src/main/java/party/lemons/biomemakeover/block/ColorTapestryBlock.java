package party.lemons.biomemakeover.block;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import party.lemons.biomemakeover.BiomeMakeover;

import java.util.Map;

public class ColorTapestryBlock extends AbstractTapestryBlock
{
    public static Map<DyeColor, ResourceLocation> TEXTURES = Maps.newHashMap();
    static {
        for(DyeColor c : DyeColor.values())
            TEXTURES.put(c, BiomeMakeover.ID("textures/tapestry/" + c.getName() + "_tapestry.png"));
    }

    private final DyeColor color;

    public ColorTapestryBlock(DyeColor color, Properties properties) {
        super(properties);
        this.color = color;
    }

    @Override
    public ResourceLocation getRenderTexture() {
        return TEXTURES.get(color);
    }
}