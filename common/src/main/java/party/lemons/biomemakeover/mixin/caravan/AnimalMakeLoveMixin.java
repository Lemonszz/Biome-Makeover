package party.lemons.biomemakeover.mixin.caravan;

import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMEntities;

import java.util.Optional;

@Mixin(AnimalMakeLove.class)
public class AnimalMakeLoveMixin
{
    @Inject(at = @At("HEAD"), method = "findValidBreedPartner", cancellable = true)
    private void findValidBreedPartner(Animal animal, CallbackInfoReturnable<Optional<? extends Animal>> cbi)
    {
        if(animal.getType().is(BMEntities.CAMELS))
        {
            cbi.setReturnValue(animal.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get().findClosest(livingEntity -> {
                if (livingEntity.getType().is(BMEntities.CAMELS) && livingEntity instanceof Animal animal2 && animal.canMate(animal2)) {
                    return true;
                }
                return false;
            }).map(Animal.class::cast));
        }
    }
}
