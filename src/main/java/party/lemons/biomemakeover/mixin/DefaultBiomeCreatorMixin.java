package party.lemons.biomemakeover.mixin;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeCreator;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.init.BMEntities;

@Mixin(DefaultBiomeCreator.class)
public class DefaultBiomeCreatorMixin
{
	@Inject(method = "createBadlands", at = @At(target = "Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addBatsAndMonsters(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V", value = "INVOKE"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private static void createBadlands(ConfiguredSurfaceBuilder arg0, float arg1, float arg2, boolean arg3, boolean arg4, CallbackInfoReturnable<Biome> cir, SpawnSettings.Builder builder)
	{
		builder = builder.creatureSpawnProbability(0.025F).spawnCost(BMEntities.SCUTTLER, 0.3D, 0.4D);
	}
}
