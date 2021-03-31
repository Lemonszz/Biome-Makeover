package party.lemons.biomemakeover.entity.adjudicator.phase;

import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorMimicEntity;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.NetworkUtil;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.util.extensions.LootBlocker;

import java.util.List;

public class MimicPhase extends BowAttackingPhase
{
	private boolean hit = false;

	public MimicPhase(Identifier phaseID, AdjudicatorEntity adjudicator)
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

			if(world.getBlockState(spawnPos.down()).isAir())
				world.setBlockState(spawnPos.down(), Blocks.COBBLESTONE.getDefaultState());

			AdjudicatorMimicEntity mimic = BMEntities.ADJUDICATOR_MIMIC.create(world);
			((LootBlocker)mimic).setLootBlocked(true);
			mimic.initialize((ServerWorldAccess) world, world.getLocalDifficulty(spawnPos), SpawnReason.NATURAL, null, null);
			mimic.refreshPositionAndAngles(spawnPos.getX() + 0.5F, spawnPos.getY(), spawnPos.getZ() + 0.5F, 0, 0);
			world.spawnEntity(mimic);
			adjudicator.clearArea(mimic);

			NetworkUtil.doEnderParticles(world, mimic, 10);
		}
		NetworkUtil.doEnderParticles(world, adjudicator, 10);
	}

	@Override
	public void onExitPhase()
	{
		super.onExitPhase();
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
