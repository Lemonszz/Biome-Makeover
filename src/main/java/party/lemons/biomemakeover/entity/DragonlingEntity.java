package party.lemons.biomemakeover.entity;

import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMEntities;
import software.bernie.geckolib.animation.builder.AnimationBuilder;
import software.bernie.geckolib.animation.controller.EntityAnimationController;
import software.bernie.geckolib.entity.IAnimatedEntity;
import software.bernie.geckolib.event.AnimationTestEvent;
import software.bernie.geckolib.manager.EntityAnimationManager;

public class DragonlingEntity extends HostileEntity implements IAnimatedEntity
{
	public AttributeContainer attributeContainer;

	EntityAnimationManager manager = new EntityAnimationManager();
	EntityAnimationController controller = new EntityAnimationController(this, "controller", 5, this::animationPredicate);

	public DragonlingEntity(World world)
	{
		super(BMEntities.DRAGONLING, world);
		manager.addAnimationController(controller);
	}

	protected void initGoals() {
		this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(8, new LookAtEntityGoal(this, EndermanEntity.class, 8.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
		this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
		this.targetSelector.add(1, new FollowTargetGoal(this, EndermiteEntity.class, true));
		this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, true));
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer =  new AttributeContainer(
					HostileEntity.createHostileAttributes()
					.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5F)
					.add(EntityAttributes.GENERIC_MAX_HEALTH, 3.0D)
					.build());
		return attributeContainer;
	}

	@Override
	public EntityAnimationManager getAnimationManager()
	{
		return manager;
	}

	private <E extends DragonlingEntity> boolean animationPredicate(AnimationTestEvent<E> event)
	{
		if(event.isWalking())
		{
			controller.setAnimation(new AnimationBuilder().addAnimation("walk", true));
			return true;
		}
		else
		{
			return false;
		}
	}
}
