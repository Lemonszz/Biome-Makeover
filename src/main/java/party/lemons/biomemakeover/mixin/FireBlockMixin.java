package party.lemons.biomemakeover.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.Block;
import net.minecraft.block.FireBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.access.FireBlockAccessor;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin implements FireBlockAccessor
{
	@Shadow
	@Final
	private Object2IntMap<Block> burnChances;

	@Shadow
	@Final
	private Object2IntMap<Block> spreadChances;

	@Shadow
	protected abstract void registerFlammableBlock(Block block, int burnChance, int spreadChance);

	@Override
	public void bm_registerFlammable(Block block, int burnChance, int spreadChance)
	{
		registerFlammableBlock(block, burnChance, spreadChance);
	}
}
