package party.lemons.biomemakeover.mixin.forge;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BiomeDefaultFeatures.class)
public abstract class DefaultBiomesMixin_RemoveSwampTrees
{
    /*
            I hate this
            forge gib api

            Removing from gen settings builder didn't work :(
     */


    /*
           Redirection the first feature add (swamp trees)
     */

    @Redirect(method = "addSwampVegetation", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/BiomeGenerationSettings$Builder;addFeature(Lnet/minecraft/world/level/levelgen/GenerationStep$Decoration;Lnet/minecraft/core/Holder;)Lnet/minecraft/world/level/biome/BiomeGenerationSettings$Builder;", ordinal = 0))
    private static BiomeGenerationSettings.Builder redirectSwampTrees(BiomeGenerationSettings.Builder builder, GenerationStep.Decoration step, Holder<PlacedFeature> feature)
    {
        //No
        return builder;
    }
}
