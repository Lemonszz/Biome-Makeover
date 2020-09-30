package party.lemons.biomemakeover.mixin;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.init.BMWorldGen;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin
{
	@Inject(at= @At("RETURN"), method = "addMushroomFieldsFeatures")
	private static void addToMushroomFields(GenerationSettings.Builder builder, CallbackInfo cbi)
	{
		builder.carver(GenerationStep.Carver.AIR, BMWorldGen.LARGE_CAVE_CONFIGURED_CARVER);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_UNDERGROUND);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_GLOWSHROOMS);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_UNDERGROUND_SHROOMS);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_SPROUTS);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_ROOTS);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_UNDERGROUND_GLOWSHROOMS);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_TALL_SHROOMS);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_TALL_SHROOMS_UNDERGROUND);
	}
}
