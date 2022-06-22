package party.lemons.biomemakeover.level;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import party.lemons.biomemakeover.util.RandomUtil;

public class WindSystem
{
    public static float windX = RandomUtil.randomDirection(RandomUtil.RANDOM.nextFloat() / 10F);
    public static float windZ = RandomUtil.randomDirection(RandomUtil.RANDOM.nextFloat() / 10F);

    private static float directionX = RandomUtil.randomDirection(1);
    private static float directionZ = RandomUtil.randomDirection(1);

    public static void update(RandomSource random)
    {
        if(random.nextInt(100) == 0)
        {
            directionX = directionX * -1;
            windX += directionX * (random.nextFloat() / 25F);
        }

        if(random.nextInt(100) == 0)
        {
            directionZ = directionZ * -1;
            windZ += directionZ * (random.nextFloat() / 25F);
        }

        if(random.nextInt(20) == 0)
        {
            windX += directionX * (random.nextFloat() / 30F);
            windZ += directionZ * (random.nextFloat() / 30F);

            windX = Mth.clamp(windX, -0.7F, 0.7F);
            windZ = Mth.clamp(windZ, -0.7F, 0.7F);
        }

    }
}