package party.lemons.biomemakeover.mixin;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.init.BMStructures;
import party.lemons.biomemakeover.world.feature.mansion.MansionFeature;

import java.util.List;

@Mixin(NoiseChunkGenerator.class)
public class NoiseChunkGeneratorMixin
{
	@Inject(at = @At("HEAD"), method = "getEntitySpawnList", cancellable = true)
	public void getEntitySpawnList(Biome biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos, CallbackInfoReturnable<List<SpawnSettings.SpawnEntry>> cbi)
	{
		if(accessor.getStructureAt(pos, true, BMStructures.GHOST_TOWN).hasChildren())
		{
			if(group == SpawnGroup.MONSTER)
			{
				cbi.setReturnValue(BMStructures.GHOST_TOWN.getMonsterSpawns());
			}

			if(group == SpawnGroup.CREATURE)
			{
				cbi.setReturnValue(BMStructures.GHOST_TOWN.getCreatureSpawns());
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ProtoChunk;getHeightmap(Lnet/minecraft/world/Heightmap$Type;)Lnet/minecraft/world/Heightmap;", ordinal = 0), method = "populateNoise", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk, CallbackInfo cbi, ObjectList<StructurePiece> objectList, ObjectList<JigsawJunction> objectList2, ChunkPos chunkPos, int i, int j, int k, int l, double[][][] ds, ProtoChunk protoChunk)
	{
		objectList.removeIf((o)->
		{
			if(o instanceof MansionFeature.Piece)
			{
				MansionFeature.Piece piece = (MansionFeature.Piece) o;
				return !piece.isGround();
			}
			return false;
		});
	}
}
