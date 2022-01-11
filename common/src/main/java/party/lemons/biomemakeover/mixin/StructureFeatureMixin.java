package party.lemons.biomemakeover.mixin;

import com.google.common.collect.Maps;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(StructureFeature.class)
public class StructureFeatureMixin
{
    @Shadow @Final @Mutable public static List<StructureFeature<?>> NOISE_AFFECTING_FEATURES;

    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void clinit(CallbackInfo cbi)
    {
        NOISE_AFFECTING_FEATURES = new ArrayList<>(NOISE_AFFECTING_FEATURES);
    }
}
