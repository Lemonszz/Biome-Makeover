package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;
import party.lemons.biomemakeover.util.NetworkUtil;

public class SummonPhase extends TimedPhase
{
	private final int mobCount;
	protected final EntityType<? extends LivingEntity>[] entities;
	protected int toSpawn;
	private BlockPos[] spawnPositions;
	int spawnIndex = 0;
	private boolean wasHit;

	public SummonPhase(Identifier id, AdjudicatorEntity adjudicator, int mobCount, EntityType<? extends LivingEntity>... entities)
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
		adjudicator.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, 10F, 1F);
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
			NetworkUtil.doBlockEnderParticles(world, spawnPositions[i], 2);
	}

	@Override
	public void onHurt(DamageSource source, float amount)
	{
		super.onHurt(source, amount);

		if(source.getAttacker() instanceof PlayerEntity)
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
		LivingEntity entity = entities[random.nextInt(entities.length)].create(world);
		if(entity instanceof MobEntity)
			((MobEntity) entity).initialize((ServerWorldAccess) world, world.getLocalDifficulty(spawnPos), SpawnReason.EVENT, null, null);

		entity.refreshPositionAndAngles((double)spawnPos.getX() + 0.5D, (double)spawnPos.getY(), (double)spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
		world.spawnEntity(entity);
		world.playSound(null, spawnPos, SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.HOSTILE, 10F, 1F);
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
		for(int i = 0; i < mobCount; i++)
			spawnPositions[i] = adjudicator.findSuitableArenaPos();
	}

	@Override
	public boolean isSelectable()
	{
		return world.getEntitiesByClass(HostileEntity.class, adjudicator.getArenaBounds(), EntityPredicates.VALID_LIVING_ENTITY).size() < 15;
	}
}
