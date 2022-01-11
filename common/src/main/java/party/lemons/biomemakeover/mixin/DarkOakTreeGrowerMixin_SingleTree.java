package party.lemons.biomemakeover.mixin;

import net.minecraft.world.level.block.grower.DarkOakTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.Random;

@Mixin(DarkOakTreeGrower.class)
public class DarkOakTreeGrowerMixin_SingleTree {
    @Inject(at = @At("HEAD"), method = "getConfiguredFeature", cancellable = true)
    protected void getConfiguredFeature(Random random, boolean bl, CallbackInfoReturnable<ConfiguredFeature<?, ?>> cbi)
    {
        cbi.setReturnValue(BMWorldGen.DarkForest.DARK_OAK_SMALL);
    }
}