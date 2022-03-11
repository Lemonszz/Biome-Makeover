package party.lemons.biomemakeover.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.DirectionalDataHandler;

import java.util.List;
import java.util.Random;

@Mixin(TemplateStructurePiece.class)
public class TemplateStructurePieceMixin_DirectionalData
{
    @Shadow protected StructureTemplate template;

    @Shadow protected BlockPos templatePosition;

    @Shadow protected StructurePlaceSettings placeSettings;

    @Inject(method = "postProcess",
            at = @At(
                    value = "INVOKE",
                    ordinal = 0,
                    target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;filterBlocks(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Lnet/minecraft/world/level/block/Block;)Ljava/util/List;"))
    public void postProcess(WorldGenLevel arg, StructureFeatureManager arg2, ChunkGenerator arg3, Random random, BoundingBox arg4, ChunkPos arg5, BlockPos arg6, CallbackInfo cbi)
    {
        if(this instanceof DirectionalDataHandler)
        {
            List<StructureTemplate.StructureBlockInfo> list = this.template.filterBlocks(this.templatePosition, this.placeSettings, BMBlocks.DIRECTIONAL_DATA.get());
            for(StructureTemplate.StructureBlockInfo info : list)
            {
                if(info.nbt != null)
                {
                    String meta = info.nbt.getString("metadata");
                    Direction dir = info.state.getValue(DirectionalBlock.FACING);
                    arg.setBlock(info.pos, Blocks.AIR.defaultBlockState(), 3);

                    ((DirectionalDataHandler)this).handleDirectionalMetadata(meta, dir, info.pos, arg, random);
                }
            }
        }
    }
}
