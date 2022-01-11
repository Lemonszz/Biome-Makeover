package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ModelLayers.class)
public interface ModelLayersAccessor {
    @Accessor("ALL_MODELS")
    static Set<ModelLayerLocation> getLayers() {
        throw new AssertionError("This should not occur!");
    }
}