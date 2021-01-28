package party.lemons.biomemakeover.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.LakeFeature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMStructures;

import java.util.Random;

@Mixin(LakeFeature.class)
public abstract class LakeFeatureMixin
{
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/StructureWorldAccess;getStructures(Lnet/minecraft/util/math/ChunkSectionPos;Lnet/minecraft/world/gen/feature/StructureFeature;)Ljava/util/stream/Stream;", ordinal = 0), method = "generate", cancellable = true)
	private void generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos blockPos, SingleStateFeatureConfig cfg, CallbackInfoReturnable<Boolean> cbi)
	{
		if(world.getStructures(ChunkSectionPos.from(blockPos), BMStructures.GHOST_TOWN).findAny().isPresent() || world.getStructures(ChunkSectionPos.from(blockPos), BMStructures.MANSION).findAny().isPresent())
		{
			cbi.setReturnValue(false);
		}
	}
}
