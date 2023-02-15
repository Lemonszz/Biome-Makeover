package party.lemons.biomemakeover.mixin.mushroom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomCow.class)
public abstract class MushroomCowMixin extends Cow
{
    public MushroomCowMixin(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract MushroomCow.MushroomType getMushroomType();

    @Shadow protected abstract void setMushroomType(MushroomCow.MushroomType mushroomType);

    @Inject(at = @At("TAIL"), method = "<init>")
    public void onContruct(EntityType<? extends MushroomCow> type, Level level, CallbackInfo cbi)
    {
        if(getMushroomType() != MushroomCow.MushroomType.BROWN)
            if(this.random.nextBoolean())
                setMushroomType(MushroomCow.MushroomType.BROWN);
    }
}
