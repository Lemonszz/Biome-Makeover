package party.lemons.biomemakeover.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.MathUtils;

public class ToadTargetEntity extends PathAwareEntity
{
	private ToadEntity eatenBy = null;

	protected ToadTargetEntity(EntityType<? extends PathAwareEntity> entityType, World world)
	{
		super(entityType, world);
	}

	public void setEatenBy(ToadEntity toad)
	{
		this.eatenBy = toad;
	}

	public boolean isBeingEaten()
	{
		return eatenBy != null;
	}

	@Override
	public void tick()
	{
		if(!isBeingEaten())
		{
			super.tick();
		}
		else
		{
			if(eatenBy == null || eatenBy.isDead() || eatenBy.removed)
			{
				setEatenBy(null);
				return;
			}

			if(eatenBy.isTongueReady())
			{
				double xx = MathUtils.approachValue(getPos().x, eatenBy.getX(), 0.7D);
				double yy = MathUtils.approachValue(getPos().y, eatenBy.getY() + 0.2F, 0.5D);
				double zz = MathUtils.approachValue(getPos().z, eatenBy.getZ(), 0.7D);
				updatePositionAndAngles(xx, yy, zz, yaw, pitch);
				setPos(xx, yy, zz);
				setVelocity(0, 0, 0);

				if(distanceTo(eatenBy) <= 0.2F)
				{
					eatenBy.playSound(BMEffects.TOAD_SWALLOW, 1F, 1F + ((float)random.nextGaussian() / 5F));
					remove();
				}
			}
		}
	}

	public boolean canBeLeashedBy(PlayerEntity player) {
		return false;
	}
}
