package party.lemons.biomemakeover.world;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import party.lemons.biomemakeover.entity.TumbleweedEntity;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.RandomUtil;

public class TumbleweedSpawner
{
	public static void update(ServerWorld world)
	{
		for(PlayerEntity pl : world.getPlayers())
		{
			if(world.random.nextInt(200) == 0)
			{
				int xDir = RandomUtil.randomDirection(1);
				int xx = (int)pl.getX() + ((20 * xDir) + (RandomUtil.RANDOM.nextInt(45) * xDir));

				int zDir = RandomUtil.randomDirection(1);
				int zz = (int)pl.getZ() + ((20 * zDir) + (RandomUtil.RANDOM.nextInt(45) * zDir));

				int yy = world.getTopY(Heightmap.Type.WORLD_SURFACE, xx, zz);
				BlockPos pos = new BlockPos(xx, yy + RandomUtil.randomRange(1, 3),zz);

				if(world.isAir(pos) && world.getBiome(pos).getCategory() == Biome.Category.MESA)
				{
					TumbleweedEntity tumble = BMEntities.TUMBLEWEED.create(world);
					tumble.refreshPositionAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
					world.spawnEntity(tumble);
				}
			}
		}
	}
}
