package party.lemons.biomemakeover.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.DirectionalDataHandler;

import java.util.List;
import java.util.Random;

@Mixin(SimpleStructurePiece.class)
public class SimpleStructurePieceMixin
{
	@Shadow protected Structure structure;

	@Shadow protected BlockPos pos;

	@Shadow protected StructurePlacementData placementData;

	@Inject(method = "generate",
			at = @At(
				value = "INVOKE",
				ordinal = 0,
				target = "Lnet/minecraft/structure/Structure;getInfosForBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;Lnet/minecraft/block/Block;)Ljava/util/List;"))
	public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox box, ChunkPos chunkPos, BlockPos blockPos, CallbackInfoReturnable<Boolean> cbi)
	{
		if(this instanceof DirectionalDataHandler)
		{
			List<Structure.StructureBlockInfo> list = this.structure.getInfosForBlock(this.pos, this.placementData, BMBlocks.DIRECTIONAL_DATA);

			for(Structure.StructureBlockInfo info : list)
			{
				if(info.tag != null)
				{
					String meta = info.tag.getString("metadata");
					Direction dir = info.state.get(FacingBlock.FACING);
					world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);

					((DirectionalDataHandler)this).handleDirectionalMetadata(meta, dir, info.pos, world, random, box);
				}
			}
		}
	}
}
