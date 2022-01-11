package party.lemons.biomemakeover.mixin;

import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(StructureFeatures.class)
public class StructureFeaturesMixin_RemoveMansion {
    @Shadow @Final private static ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> WOODLAND_MANSION;

    //Stop mansion being registered to dark forest
    //TODO: Use an actual remove system at some point.
    @Inject(at = @At("HEAD"), method = "register(Ljava/util/function/BiConsumer;Lnet/minecraft/world/level/levelgen/feature/ConfiguredStructureFeature;Lnet/minecraft/resources/ResourceKey;)V", cancellable = true)
    private static void register(BiConsumer<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> biConsumer, ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome> resourceKey, CallbackInfo cbi)
    {
        if(configuredStructureFeature == WOODLAND_MANSION)
            cbi.cancel();
    }
}
