package party.lemons.biomemakeover.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMItems;

@Mixin(PowderSnowBlock.class)
public class PoweredSnowBlockMixin {
    @Inject(at = @At("RETURN"), method = "canEntityWalkOnPowderSnow", cancellable = true)
    private static void canEntityWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cbi) {
        if (entity instanceof LivingEntity living) {
            cbi.setReturnValue(living.getItemBySlot(EquipmentSlot.FEET).is(BMItems.CLADDED_BOOTS));
        }
    }
}
