package party.lemons.biomemakeover.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.access.BlockEntityTypeAccessor;

import java.util.HashSet;
import java.util.Set;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin implements BlockEntityTypeAccessor
{
	@Shadow
	@Final
	@Mutable
	private Set<Block> blocks;

	@Override
	public void addBlockTypes(Block... toAdd)
	{
		Set<Block> b = new HashSet<>(blocks);

		for(Block block : toAdd)
			b.add(block);

		blocks = b;
	}
}
