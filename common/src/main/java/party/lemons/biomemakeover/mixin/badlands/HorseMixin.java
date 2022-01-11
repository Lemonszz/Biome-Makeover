package party.lemons.biomemakeover.mixin.badlands;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.util.extension.HorseHat;

@Mixin(Horse.class)
public class HorseMixin extends AbstractHorse implements HorseHat
{
    private static final EntityDataAccessor<Boolean> HAS_HAT = SynchedEntityData.defineId(Horse.class, EntityDataSerializers.BOOLEAN);
    private boolean cowboySpawned = false;

    protected HorseMixin(EntityType<? extends AbstractHorse> entityType, Level world)
    {
        super(entityType, world);
    }


    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    private void initDataTacker(CallbackInfo cbi)
    {
        this.getEntityData().define(HAS_HAT, false);
    }

    @Inject(at = @At("RETURN"), method = "addAdditionalSaveData")
    private void writeData(CompoundTag tag, CallbackInfo cbi)
    {
        tag.putBoolean("Hat", hasHat());
        tag.putBoolean("CowboySpawned", cowboySpawned);
    }

    @Inject(at = @At("RETURN"), method = "readAdditionalSaveData")
    private void readData(CompoundTag tag, CallbackInfo cbi)
    {
        if(tag.contains("Hat")) this.getEntityData().set(HAS_HAT, tag.getBoolean("Hat"));

        if(tag.contains("CowboySpawned")) cowboySpawned = tag.getBoolean("CowboySpawned");
    }

    @Override
    public boolean removeWhenFarAway(double distanceSquared)
    {
        if(getControllingPassenger() == null)
        {
            if(isSaddled() || isWearingArmor() || isLeashed())
            {
                cowboySpawned = false;
                return false;
            }

            return cowboySpawned;
        }else
        {
            if(getControllingPassenger() instanceof PatrollingMonster pm)
            {
                return pm.removeWhenFarAway(distanceSquared);
            }
        }
        return cowboySpawned;
    }

    @Override
    public boolean hasHat()
    {
        return this.getEntityData().get(HAS_HAT);
    }

    @Override
    public void setHat()
    {
        this.getEntityData().set(HAS_HAT, true);
    }

    @Override
    public void setCowboySpawned()
    {
        cowboySpawned = true;
    }
}