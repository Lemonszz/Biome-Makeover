package party.lemons.biomemakeover.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.level.feature.GhostTownFeature;

@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseBasedChunkGeneratorMixin_StructureSpawns {

    @Inject(at = @At("TAIL"), method = "getMobsAt", cancellable = true)
    public void getMobsAt(Biome biome, StructureFeatureManager structureFeatureManager, MobCategory mobCategory, BlockPos blockPos, CallbackInfoReturnable<WeightedRandomList<MobSpawnSettings.SpawnerData>> cbi)
    {
        if (mobCategory == MobCategory.MONSTER)
        {
            if (structureFeatureManager.getStructureAt(blockPos, BMWorldGen.Badlands.GHOST_TOWN).isValid()) {
                cbi.setReturnValue(GhostTownFeature.SPAWNS);
            }
        }
    }
}
