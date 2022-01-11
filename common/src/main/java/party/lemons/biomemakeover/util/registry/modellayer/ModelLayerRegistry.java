package party.lemons.biomemakeover.util.registry.modellayer;

/*
    BASED ON FABRIC's IMPLEMENTATION
 */

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import party.lemons.biomemakeover.mixin.client.ModelLayersAccessor;

import java.util.HashMap;
import java.util.Map;

public final class ModelLayerRegistry
{
    public static final Map<ModelLayerLocation, TexturedModelDataProvider> PROVIDERS = new HashMap<>();

    public static void registerModelLayer(ModelLayerLocation modelLayer, TexturedModelDataProvider provider)
    {
        if (PROVIDERS.putIfAbsent(modelLayer, provider) != null) {
            throw new IllegalArgumentException(String.format("Cannot replace registration for entity model layer \"%s\"", modelLayer));
        }

        ModelLayersAccessor.getLayers().add(modelLayer);
    }

    private ModelLayerRegistry() {
    }

    @FunctionalInterface
    public interface TexturedModelDataProvider {
        LayerDefinition createModelData();
    }
}