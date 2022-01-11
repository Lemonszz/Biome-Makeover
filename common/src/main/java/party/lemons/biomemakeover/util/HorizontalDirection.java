package party.lemons.biomemakeover.util;

import net.minecraft.core.BlockPos;

import java.util.Random;

public enum HorizontalDirection
{
    NORTH(0, -1, 4, true), NORTH_EAST(1, -1, 5, false), EAST(1, 0, 6, true), SOUTH_EAST(1, 1, 7, false), SOUTH(0, 1, 0, true), SOUTH_WEST(-1, 1, 1, false), WEST(-1, 0, 2, true), NORTH_WEST(-1, -1, 3, false);

    public int x;
    public int z;
    private final int opposite;
    public boolean isStraight;

    HorizontalDirection(int x, int z, int opposite, boolean straight)
    {
        this.x = x;
        this.z = z;
        this.opposite = opposite;
        this.isStraight = straight;
    }

    public HorizontalDirection opposite()
    {
        return values()[opposite];
    }

    public BlockPos offset(BlockPos pos)
    {
        return pos.offset(x, 0, z);
    }

    public static HorizontalDirection random(Random random)
    {
        return values()[random.nextInt(values().length)];
    }
}