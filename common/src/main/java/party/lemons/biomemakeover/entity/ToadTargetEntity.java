package party.lemons.biomemakeover.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.taniwha.util.MathUtils;

public class ToadTargetEntity extends PathfinderMob {

    private ToadEntity eatenBy = null;

    protected ToadTargetEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
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
    protected void customServerAiStep()
    {
        if(!isBeingEaten())
        {
            super.customServerAiStep();
        }else
        {
            if(eatenBy == null || eatenBy.isDeadOrDying() || eatenBy.isRemoved())
            {
                setEatenBy(null);
                return;
            }

            if(eatenBy.isTongueReady())
            {
                double xx = MathUtils.approachValue(getX(), eatenBy.getX(), 0.7D);
                double yy = MathUtils.approachValue(getY(), eatenBy.getY() + 0.2F, 0.5D);
                double zz = MathUtils.approachValue(getZ(), eatenBy.getZ(), 0.7D);
                absMoveTo(xx, yy, zz, getYRot(), getXRot());
                setPos(xx, yy, zz);
                setDeltaMovement(0, 0, 0);

                if(distanceTo(eatenBy) <= 0.2F)
                {
                    eatenBy.playSound(BMEffects.TOAD_SWALLOW, 1F, 1F + ((float) random.nextGaussian() / 5F));
                    remove(RemovalReason.DISCARDED);
                }
            }
        }
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }
}
