package party.lemons.biomemakeover.entity.adjudicator;

import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.NetworkUtil;

public class AdjudicatorMimicEntity extends MobEntity implements AdjudicatorStateProvider
{
	private AttributeContainer attributes;


	public AdjudicatorMimicEntity(World world)
	{
		super(BMEntities.ADJUDICATOR_MIMIC, world);
	}

	@Override
	protected void initGoals()
	{
		super.initGoals();
		goalSelector.add(0, new LookAtEntityGoal(this, PlayerEntity.class, 10));
	}

	@Override
	public void onDeath(DamageSource source)
	{
		remove();

		if(!world.isClient()) NetworkUtil.doBlockEnderParticles(world, getBlockPos(), 6);
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributes == null)
			attributes = new AttributeContainer(HostileEntity.createHostileAttributes()
					                                    .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0)
					                                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0)
														.add(EntityAttributes.GENERIC_MAX_HEALTH, 1F)
					                                    .build());
		return attributes;
	}

	@Override
	public AdjudicatorState getState()
	{
		return AdjudicatorState.SUMMONING;
	}
}
