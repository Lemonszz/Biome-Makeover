package party.lemons.biomemakeover.util.color;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class StaticBlockColorProvider implements BlockColor
{
    private final int color;

    public StaticBlockColorProvider(int color)
    {
        this.color = color;
    }

    @Override
    public int getColor(BlockState blockState, @Nullable BlockAndTintGetter blockAndTintGetter, @Nullable BlockPos blockPos, int i) {
        return color;
    }
}