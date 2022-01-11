package party.lemons.biomemakeover.mixin.client;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.util.registry.modellayer.ModelLayerRegistry;

import java.util.Map;

@Mixin(LayerDefinitions.class)
abstract class LayerDefinitionsMixin {
    @Inject(method = "createRoots", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void registerExtraModelData(CallbackInfoReturnable<Map<ModelLayerLocation, LayerDefinition>> info, ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> builder) {
        for (Map.Entry<ModelLayerLocation, ModelLayerRegistry.TexturedModelDataProvider> entry : ModelLayerRegistry.PROVIDERS.entrySet()) {
            builder.put(entry.getKey(), entry.getValue().createModelData());
        }
    }
}