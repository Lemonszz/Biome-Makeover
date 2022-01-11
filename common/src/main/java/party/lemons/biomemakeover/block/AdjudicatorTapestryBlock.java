package party.lemons.biomemakeover.block;

import net.minecraft.resources.ResourceLocation;
import party.lemons.biomemakeover.BiomeMakeover;

public class AdjudicatorTapestryBlock extends AbstractTapestryBlock {
    public static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/tapestry/adjudicator_tapestry.png");

    public AdjudicatorTapestryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ResourceLocation getRenderTexture() {
        return TEXTURE;
    }
}
