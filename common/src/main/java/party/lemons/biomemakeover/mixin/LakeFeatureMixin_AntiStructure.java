package party.lemons.biomemakeover.mixin;

import net.minecraft.core.SectionPos;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMWorldGen;

@Mixin(LakeFeature.class)
public class LakeFeatureMixin_AntiStructure {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;startsForFeature(Lnet/minecraft/core/SectionPos;Lnet/minecraft/world/level/levelgen/feature/StructureFeature;)Ljava/util/List;"), method = "place", cancellable = true)
    public void place(FeaturePlaceContext<LakeFeature.Configuration> ctx, CallbackInfoReturnable<Boolean> cbi)
    {
        //TODO: Some list of these elsewhere
        if(!ctx.level().startsForFeature(SectionPos.of(ctx.origin()), BMWorldGen.Badlands.GHOST_TOWN).isEmpty() || !ctx.level().startsForFeature(SectionPos.of(ctx.origin()), BMWorldGen.DarkForest.MANSION).isEmpty())
        {
            cbi.setReturnValue(false);
        }
    }
}