package party.lemons.biomemakeover.mixin.antigen;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.WoodlandMansionFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(StructureFeature.class)
public class StructureFeatureMixin {

    @Inject(at = @At("HEAD"), method = "canGenerate", cancellable = true)
    public void canGenerate(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, StructureManager structureManager, long l, ChunkPos chunkPos, FeatureConfiguration featureConfiguration, LevelHeightAccessor levelHeightAccessor, Predicate<Holder<Biome>> predicate, CallbackInfoReturnable<Boolean> cbi)
    {
        if(((StructureFeature<?>)(Object)this) instanceof WoodlandMansionFeature)
        {
            cbi.setReturnValue(false);
        }
    }
}
