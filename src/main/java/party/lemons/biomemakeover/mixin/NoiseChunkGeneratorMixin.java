package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMStructures;

import java.util.List;

@Mixin(NoiseChunkGenerator.class)
public class NoiseChunkGeneratorMixin
{
	@Inject(at = @At("HEAD"), method = "getEntitySpawnList", cancellable = true)
	public void getEntitySpawnList(Biome biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos, CallbackInfoReturnable<List<SpawnSettings.SpawnEntry>> cbi)
	{
		if (accessor.getStructureAt(pos, true, BMStructures.GHOST_TOWN).hasChildren()) {
			if (group == SpawnGroup.MONSTER) {
				cbi.setReturnValue(BMStructures.GHOST_TOWN.getMonsterSpawns());
			}

			if (group == SpawnGroup.CREATURE) {
				cbi.setReturnValue(BMStructures.GHOST_TOWN.getCreatureSpawns());
			}
		}
	}
}
