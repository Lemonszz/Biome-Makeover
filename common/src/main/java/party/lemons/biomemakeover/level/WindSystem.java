package party.lemons.biomemakeover.level;

import net.minecraft.util.Mth;
import party.lemons.biomemakeover.util.RandomUtil;

public class WindSystem
{
    public static float windX = RandomUtil.randomDirection(RandomUtil.RANDOM.nextFloat() / 10F);
    public static float windZ = RandomUtil.randomDirection(RandomUtil.RANDOM.nextFloat() / 10F);

    private static float directionX = RandomUtil.randomDirection(1);
    private static float directionZ = RandomUtil.randomDirection(1);

    public static void update()
    {
        if(RandomUtil.RANDOM.nextInt(100) == 0)
        {
            directionX = directionX * -1;
            windX += directionX * (RandomUtil.RANDOM.nextFloat() / 25F);
        }

        if(RandomUtil.RANDOM.nextInt(100) == 0)
        {
            directionZ = directionZ * -1;
            windZ += directionZ * (RandomUtil.RANDOM.nextFloat() / 25F);
        }

        if(RandomUtil.RANDOM.nextInt(20) == 0)
        {
            windX += directionX * (RandomUtil.RANDOM.nextFloat() / 30F);
            windZ += directionZ * (RandomUtil.RANDOM.nextFloat() / 30F);

            windX = Mth.clamp(windX, -0.7F, 0.7F);
            windZ = Mth.clamp(windZ, -0.7F, 0.7F);
        }

    }
}