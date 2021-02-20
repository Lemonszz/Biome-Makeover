package party.lemons.biomemakeover.entity.adjudicator.phase;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorMimicEntity;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.NetworkUtil;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.List;

public class MimicPhase extends AdjudicatorPhase
{
	private boolean hit = false;

	public MimicPhase(Identifier phaseID, AdjudicatorEntity adjudicator)
	{
		super(phaseID, adjudicator);
	}

	@Override
	protected void initAI()
	{
		goalSelector.add(0, new LookAtEntityGoal(adjudicator, PlayerEntity.class, 10));
	}

	@Override
	public void onEnterPhase()
	{
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

			AdjudicatorMimicEntity mimic = BMEntities.ADJUDICATOR_MIMIC.create(world);
			mimic.refreshPositionAndAngles(spawnPos.getX() + 0.5F, spawnPos.getY(), spawnPos.getZ() + 0.5F, 0, 0);
			world.spawnEntity(mimic);

			NetworkUtil.doEnderParticles(world, mimic, 10);
		}
		NetworkUtil.doEnderParticles(world, adjudicator, 10);
	}

	@Override
	public void onExitPhase()
	{
		hit = false;
		world.getEntitiesByClass(AdjudicatorMimicEntity.class, adjudicator.getArenaBounds(), (e)->true).forEach(Entity::remove);
	}

	@Override
	public boolean isPhaseOver()
	{
		return hit;
	}

	@Override
	public void onHurt(DamageSource source, float amount)
	{
		if(source.getAttacker() instanceof PlayerEntity)
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
