package party.lemons.biomemakeover.mixin;

import net.minecraft.block.sapling.DarkOakSaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.Random;

@Mixin(DarkOakSaplingGenerator.class)
public class DarkOakSaplingGeneratorMixin
{
	@Inject(at = @At("HEAD"), method = "createTreeFeature", cancellable = true)
	private void createTreeFeature(Random random, boolean bl, CallbackInfoReturnable<ConfiguredFeature<TreeFeatureConfig, ?>> cbi)
	{
		cbi.setReturnValue(BMWorldGen.DARK_OAK_SMALL);
	}
}
