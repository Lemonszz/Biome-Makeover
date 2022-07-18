package party.lemons.biomemakeover.util;

import com.google.common.collect.Maps;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class FlammabilityRegistry
{
	private static final Map<Block, Entry> entries = Maps.newHashMap();

	public static void setBlockFlammable(Block block, int catchOdds, int burnOdds)
	{
		entries.put(block, new Entry(catchOdds, burnOdds));
	}

	public static Entry getEntry(Block block)
	{
		return entries.get(block);
	}

	public record Entry(int catchOdds, int burnOdds)
	{

	}
}
