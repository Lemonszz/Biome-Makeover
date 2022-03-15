package party.lemons.biomemakeover.mixin.antigen;

import net.minecraft.world.level.levelgen.feature.WoodlandMansionFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(WoodlandMansionFeature.class)
public class WoodlandMansionFeatureMixin {

    @Inject(at = @At("HEAD"), method = "pieceGeneratorSupplier", cancellable = true)
    private static void pieceGeneratorSupplier(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context, CallbackInfoReturnable<Optional<PieceGenerator<NoneFeatureConfiguration>>> cbi)
    {
        cbi.setReturnValue(Optional.empty());
    }
}
