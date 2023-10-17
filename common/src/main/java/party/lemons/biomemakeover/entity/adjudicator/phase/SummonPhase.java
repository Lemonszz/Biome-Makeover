package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;
import party.lemons.biomemakeover.util.effect.BiomeMakeoverEffect;
import party.lemons.biomemakeover.util.effect.EffectHelper;
import party.lemons.biomemakeover.util.extension.GoalSelectorExtension;
import party.lemons.biomemakeover.util.extension.LootBlocker;

public class SummonPhase extends TimedPhase
{
    private final int mobCount;
    protected final EntityType<? extends LivingEntity>[] entities;
    protected int toSpawn;
    private BlockPos[] spawnPositions;
    int spawnIndex = 0;
    private boolean wasHit;

    public SummonPhase(ResourceLocation id, AdjudicatorEntity adjudicator, int mobCount, EntityType<? extends LivingEntity>... entities)
    {
        super(id, 120, adjudicator);

        this.mobCount = mobCount;
        this.entities = entities;
    }

    @Override
    protected void initAI()
    {

    }

    @Override
    public void onEnterPhase()
    {
        super.onEnterPhase();
        toSpawn = mobCount;
        spawnIndex = 0;
        wasHit = false;
        adjudicator.setState(AdjudicatorState.SUMMONING);

        populateSpawnPositions();
        adjudicator.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, 1F, 1F);
    }

    @Override
    public void tick()
    {
        super.tick();

        if(time % (maxTime / mobCount) == 0)
        {
            spawnEntity();
            spawnIndex++;
        }

        for(int i = spawnIndex; i < mobCount; i++)
            EffectHelper.doEffect(level, BiomeMakeoverEffect.BLOCK_ENDER_PARTICLES, spawnPositions[i], adjudicator.getBlockStateOn());
    }

    @Override
    public void onHurt(DamageSource source, float amount)
    {
        super.onHurt(source, amount);

        if(source.getEntity() instanceof Player)
        {
            wasHit = true;
        }
    }

    @Override
    public void onExitPhase()
    {
        super.onExitPhase();

        if(!wasHit)
        {
            for(int i = spawnIndex; i< mobCount; i++)
            {
                spawnEntity();
            }
        }
    }

    @Override
    public boolean isPhaseOver()
    {
        return spawnIndex >= spawnPositions.length || wasHit || super.isPhaseOver();
    }

    protected void spawnEntity()
    {
        BlockPos spawnPos = spawnPositions[spawnIndex];

        if(level.getBlockState(spawnPos.below()).isAir())
            level.setBlock(spawnPos.below(), Blocks.COBBLESTONE.defaultBlockState(), 3);

        LivingEntity entity = entities[random.nextInt(entities.length)].create(level);
        if(entity instanceof Mob)
            ((Mob) entity).finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(spawnPos), MobSpawnType.EVENT, null, null);

        ((LootBlocker)entity).setLootBlocked(true);
        entity.moveTo((double)spawnPos.getX() + 0.5D, (double)spawnPos.getY(), (double)spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
        level.addFreshEntity(entity);
        adjudicator.clearArea(entity);

        if(entity instanceof Evoker)
        {
            GoalSelectorExtension.removeGoal(entity, Evoker.EvokerSummonSpellGoal.class);
        }

        level.playSound(null, spawnPos, SoundEvents.EVOKER_CAST_SPELL, SoundSource.HOSTILE, 10F, 1F);
    }

    @Override
    public void fromTag(CompoundTag tag)
    {
        spawnIndex = tag.getInt("SpawnIndex");
        populateSpawnPositions();
    }

    @Override
    public CompoundTag toTag()
    {
        CompoundTag tag = super.toTag();
        tag.putInt("SpawnIndex", spawnIndex);
        return tag;
    }

    private void populateSpawnPositions()
    {
        this.spawnPositions = new BlockPos[mobCount];
        for(int i = 0; i < mobCount; i++) {
            BlockPos spawnPos = adjudicator.findSuitableArenaPos();
            spawnPositions[i] = spawnPos;
        }
    }

    @Override
    public boolean isSelectable()
    {
        return level.getEntitiesOfClass(Monster.class, adjudicator.getArenaBounds(), EntitySelector.LIVING_ENTITY_STILL_ALIVE).size() < 4;
    }
}