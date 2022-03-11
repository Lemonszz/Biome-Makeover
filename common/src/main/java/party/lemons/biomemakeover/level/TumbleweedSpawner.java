package party.lemons.biomemakeover.level;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import party.lemons.biomemakeover.entity.TumbleweedEntity;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.List;

public class TumbleweedSpawner
{
    public static void update(ServerLevel level)
    {
        List<TumbleweedPlayerGroup> groups = Lists.newArrayList();
        for(ServerPlayer pl : level.getPlayers((p)->true))
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
            if(level.random.nextInt(200) == 0)
            {
                BlockPos pos = group.getSpawnPos();

                if(pos != null && level.hasChunkAt(pos) && level.isEmptyBlock(pos) && level.getBiome(pos).is(BiomeTags.IS_BADLANDS))
                {
                    TumbleweedEntity tumble = BMEntities.TUMBLEWEED.get().create(level);
                    tumble.moveTo(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
                    level.addFreshEntity(tumble);
                }
            }
        }
    }

    private static class TumbleweedPlayerGroup
    {
        private static final int MAX_DISTANCE = 50;
        private final ServerPlayer main;
        private final List<ServerPlayer> nearPlayers;

        private TumbleweedPlayerGroup(ServerPlayer main)
        {
            this.main = main;
            nearPlayers = Lists.newArrayList(main);
        }

        public boolean addPlayer(ServerPlayer playerEntity)
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

            for(ServerPlayer p : nearPlayers)
            {
                if((p.getX() + 20) + 45 > maxX) maxX = (int) p.getX() + 20 + 45;
                if((p.getZ() + 20) + 45 > maxZ) maxZ = (int) p.getZ() + 20 + 45;
                if((p.getX() - 20) - 45 < minX) minX = (int) p.getX() - 20 - 45;
                if((p.getZ() - 20) - 45 < minZ) minZ = (int) p.getZ() - 20 - 45;
            }

            int spawnX = RandomUtil.randomRange(minX, maxX);
            int spawnZ = RandomUtil.randomRange(minZ, maxZ);
            int spawnY = main.level.getHeight(Heightmap.Types.WORLD_SURFACE, spawnX, spawnZ);

            int attempts = 20;
            while(!isOutOfRange(spawnX, spawnY, spawnZ) && attempts-- > 0)
            {
                spawnX = RandomUtil.randomRange(minX, maxX);
                spawnZ = RandomUtil.randomRange(minZ, maxZ);
                spawnY = main.level.getHeight(Heightmap.Types.WORLD_SURFACE, spawnX, spawnZ);
            }

            if(attempts <= 0) return null;

            return new BlockPos(spawnX, spawnY, spawnZ);
        }

        private boolean isOutOfRange(double x, double y, double z)
        {
            for(ServerPlayer pl : nearPlayers)
            {
                if(pl.distanceToSqr(x, y, z) < 20) return false;
            }
            return true;
        }
    }
}
