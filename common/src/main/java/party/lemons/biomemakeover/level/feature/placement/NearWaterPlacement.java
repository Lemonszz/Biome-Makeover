package party.lemons.biomemakeover.level.feature.placement;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import party.lemons.biomemakeover.init.BMFeatures;

public class NearWaterPlacement extends PlacementFilter
{
    public static final Codec<NearWaterPlacement> CODEC = ExtraCodecs.POSITIVE_INT
            .fieldOf("range")
            .xmap(NearWaterPlacement::new, rarityFilter -> rarityFilter.range)
            .codec();

    private final int range;

    public NearWaterPlacement(int range)
    {
        this.range = range;
    }

    @Override
    protected boolean shouldPlace(PlacementContext placementContext, RandomSource randomSource, BlockPos blockPos)
    {
        for(BlockPos pos : BlockPos.withinManhattan(blockPos, range, range, range))
        {
            if(placementContext.getBlockState(pos).is(Blocks.WATER))
                return true;
        }
        return false;
    }

    @Override
    public PlacementModifierType<?> type() {
        return BMFeatures.NEAR_WATER_PLACEMENT.get();
    }
}
