package party.lemons.biomemakeover.world;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import party.lemons.biomemakeover.entity.TumbleweedEntity;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.List;

public class TumbleweedSpawner
{
	public static void update(ServerWorld world)
	{
		List<TumbleweedPlayerGroup> groups = Lists.newArrayList();
		for(PlayerEntity pl : world.getPlayers())
		{
			if(pl.isSpectator()) continue;

			boolean added = false;
			for(TumbleweedPlayerGroup group : groups)
			{
				if(group.addPlayer(pl))
				{
					added = true;
					break;
				}
			}

			if(!added)
			{
				groups.add(new TumbleweedPlayerGroup(pl));
			}
		}

		for(TumbleweedPlayerGroup group : groups)
		{
			if(world.random.nextInt(200) == 0)
			{
				BlockPos pos = group.getSpawnPos();

				if(pos != null && world.isChunkLoaded(pos) && world.isAir(pos) && world.getBiome(pos).getCategory() == Biome.Category.MESA)
				{
					TumbleweedEntity tumble = BMEntities.TUMBLEWEED.create(world);
					tumble.refreshPositionAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
					world.spawnEntity(tumble);
				}
			}
		}

		/*
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
		}*/
	}

	private static class TumbleweedPlayerGroup
	{
		private static final int MAX_DISTANCE = 50;
		private final PlayerEntity main;
		private final List<PlayerEntity> nearPlayers;

		private TumbleweedPlayerGroup(PlayerEntity main)
		{
			this.main = main;
			nearPlayers = Lists.newArrayList(main);
		}

		public boolean addPlayer(PlayerEntity playerEntity)
		{
			if(playerEntity.distanceTo(main) <= MAX_DISTANCE)
			{
				nearPlayers.add(playerEntity);
				return true;
			}
			return false;
		}

		public BlockPos getSpawnPos()
		{
			int minX = Integer.MAX_VALUE;
			int minZ = Integer.MAX_VALUE;
			int maxX = Integer.MIN_VALUE;
			int maxZ = Integer.MIN_VALUE;

			for(PlayerEntity p : nearPlayers)
			{
				if((p.getX() + 20) + 45 > maxX) maxX = (int) p.getX() + 20 + 45;
				if((p.getZ() + 20) + 45 > maxZ) maxZ = (int) p.getZ() + 20 + 45;
				if((p.getX() - 20) - 45 < minX) minX = (int) p.getX() - 20 - 45;
				if((p.getZ() - 20) - 45 < minZ) minZ = (int) p.getZ() - 20 - 45;
			}

			int spawnX = RandomUtil.randomRange(minX, maxX);
			int spawnZ = RandomUtil.randomRange(minZ, maxZ);
			int spawnY = main.world.getTopY(Heightmap.Type.WORLD_SURFACE, spawnX, spawnZ);

			int attempts = 20;
			while(!isOutOfRange(spawnX, spawnY, spawnZ) && attempts-- > 0)
			{
				spawnX = RandomUtil.randomRange(minX, maxX);
				spawnZ = RandomUtil.randomRange(minZ, maxZ);
				spawnY = main.world.getTopY(Heightmap.Type.WORLD_SURFACE, spawnX, spawnZ);
			}

			if(attempts <= 0) return null;

			return new BlockPos(spawnX, spawnY, spawnZ);
		}

		private boolean isOutOfRange(double x, double y, double z)
		{
			for(PlayerEntity pl : nearPlayers)
			{
				if(pl.squaredDistanceTo(x, y, z) < 20) return false;
			}
			return true;
		}
	}
}
