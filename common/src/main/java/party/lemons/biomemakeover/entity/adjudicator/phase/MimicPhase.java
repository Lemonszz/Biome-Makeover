package party.lemons.biomemakeover.entity.adjudicator.phase;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorMimicEntity;
import party.lemons.biomemakeover.entity.event.EntityEvent;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.util.extension.LootBlocker;

import java.util.List;

public class MimicPhase extends BowAttackingPhase
{
    private boolean hit = false;

    public MimicPhase(ResourceLocation phaseID, AdjudicatorEntity adjudicator)
    {
        super(phaseID, adjudicator);
    }

    @Override
    public void onEnterPhase()
    {
        super.onEnterPhase();

        List<BlockPos> setPositions = Lists.newArrayList();

        int mimicCount = RandomUtil.randomRange(3, 6);
        for(int i = 0; i < mimicCount; i++)
        {
            BlockPos spawnPos;
            do
            {
                spawnPos = adjudicator.findSuitableArenaPos();
            }while(setPositions.contains(spawnPos));
            setPositions.add(spawnPos);

            if(level.getBlockState(spawnPos.below()).isAir())
                level.setBlock(spawnPos.below(), Blocks.COBBLESTONE.defaultBlockState(), 3);

            AdjudicatorMimicEntity mimic = BMEntities.ADJUDICATOR_MIMIC.get().create(level);
            ((LootBlocker)mimic).setLootBlocked(true);
            mimic.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(spawnPos), MobSpawnType.NATURAL, null, null);
            mimic.moveTo(spawnPos.getX() + 0.5F, spawnPos.getY(), spawnPos.getZ() + 0.5F, 0, 0);
            level.addFreshEntity(mimic);
            adjudicator.clearArea(mimic);
            adjudicator.broadcastEvent(mimic, EntityEvent.ENDER_PARTICLES);

        }
        adjudicator.broadcastEvent(adjudicator, EntityEvent.ENDER_PARTICLES);

    }

    @Override
    public void onExitPhase()
    {
        super.onExitPhase();
        hit = false;
        level.getEntitiesOfClass(AdjudicatorMimicEntity.class, adjudicator.getArenaBounds(), (e)->true).forEach(e->e.remove(Entity.RemovalReason.DISCARDED));
    }

    @Override
    public boolean isPhaseOver()
    {
        return hit;
    }

    @Override
    public void onHurt(DamageSource source, float amount)
    {
        if(source.getEntity() instanceof Player)
            hit = true;
    }

    @Override
    public CompoundTag toTag()
    {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Hit", hit);
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag)
    {
        hit = tag.getBoolean("Hit");
    }
}