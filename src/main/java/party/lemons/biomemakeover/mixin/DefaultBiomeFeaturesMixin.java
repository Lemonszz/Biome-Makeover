package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMWorldGen;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin
{
	@Inject(at= @At("HEAD"), method = "addMushroomFieldsFeatures")
	private static void addToMushroomFields(GenerationSettings.Builder builder, CallbackInfo cbi)
	{
		builder.carver(GenerationStep.Carver.AIR, BMWorldGen.LARGE_CAVE_CONFIGURED_CARVER);
		builder.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, BMWorldGen.MYCELIUM_PATCH);


		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.BLIGHTED_BALSA_TREES);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_UNDERGROUND);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_ROOTS_UNDERGROUND);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_SPROUTS_UNDERGROUND);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_GLOWSHROOMS);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_UNDERGROUND_SHROOMS);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_SPROUTS);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_ROOTS);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_UNDERGROUND_GLOWSHROOMS);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.ORANGE_GLOWSHROOM_PATCH);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_TALL_SHROOMS);
		builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, BMWorldGen.MUSHROOM_FIELD_TALL_SHROOMS_UNDERGROUND);
	}

	@Inject(at = @At("TAIL"), method = "addMushroomMobs")
	private static void addMushroomMobs(SpawnSettings.Builder builder, CallbackInfo cbi)
	{
		builder.spawn(SpawnGroup.AMBIENT, new SpawnSettings.SpawnEntry(BMEntities.MUSHROOM_TRADER, 1, 1, 1));
		builder.spawn(SpawnGroup.AMBIENT, new SpawnSettings.SpawnEntry(BMEntities.BLIGHTBAT, 5, 1, 1));
		builder.spawn(SpawnGroup.WATER_AMBIENT, new SpawnSettings.SpawnEntry(BMEntities.GLOWFISH, 7, 2, 7));
	}
}
