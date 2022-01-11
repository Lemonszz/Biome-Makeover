package party.lemons.biomemakeover.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.access.BlockEntityTypeAccess;

import java.util.HashSet;
import java.util.Set;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin implements BlockEntityTypeAccess {

    @Shadow
    @Final
    @Mutable
    private Set<Block> validBlocks;

    /**
     * Used to allow modded blocks to use vanilla blockentitytypes
     */

    @Override
    public void bm_addBlockTypes(Block... toAdd) {
        Set<Block> b = new HashSet<>(validBlocks);

        for (Block block : toAdd)
            b.add(block);

        validBlocks = b;
    }
}