package party.lemons.biomemakeover.util.color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.util.MathUtils;

public class FoliageShiftBlockColorProvider extends FoliageBlockColorProvider
{
    protected final int rShift, gShift, bShift;

    public FoliageShiftBlockColorProvider(int rShift, int gShift, int bShift)
    {
        this.rShift = rShift;
        this.gShift = gShift;
        this.bShift = bShift;
    }

    @Override
    public int getColor(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tintIndex) {
        if(tintIndex == 0)
        {

                int color = super.getColor(state, world, pos, tintIndex);
                int[] boosts = getColorBoosts(world, state, pos, tintIndex);

                return MathUtils.colourBoost(color, boosts[0], boosts[1], boosts[2]);
        }
        return 0xFFFFFF;
    }


    protected int[] getColorBoosts(BlockAndTintGetter world, BlockState state, BlockPos pos, int tintIndex)
    {
        return new int[]{rShift, gShift, bShift};
    }

    public static class Lillies extends FoliageShiftBlockColorProvider
    {
        public Lillies()
        {
            super(-10, 10, -10);
        }

        @Override
        protected int[] getColorBoosts(BlockAndTintGetter world, BlockState state, BlockPos pos, int tintIndex)
        {
            if(world instanceof ClientLevel && pos != null)
            {
                if(((ClientLevel) world).getBiome(pos).getBiomeCategory() == Biome.BiomeCategory.SWAMP)
                {
                    return new int[]{-20, 40, -20};
                }
            }

            return super.getColorBoosts(world, state, pos, tintIndex);
        }
    }

    public static class Willow extends FoliageShiftBlockColorProvider
    {
        public Willow()
        {
            super(0, 0, 0);
        }

        @Override
        protected int[] getColorBoosts(BlockAndTintGetter world, BlockState state, BlockPos pos, int tintIndex)
        {
            if(world instanceof ClientLevel)
            {
                if(((ClientLevel) world).getBiome(pos).getBiomeCategory() == Biome.BiomeCategory.SWAMP)
                {
                    return new int[]{-10, 15, -10};
                }
            }

            return super.getColorBoosts(world, state, pos, tintIndex);
        }
    }
}