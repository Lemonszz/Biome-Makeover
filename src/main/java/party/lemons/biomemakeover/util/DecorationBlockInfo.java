package party.lemons.biomemakeover.util;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.block.BMStairBlock;
import party.lemons.biomemakeover.init.BMItems;

import java.util.Map;

public class DecorationBlockInfo
{
	private final Map<Type, Block> blocks = Maps.newHashMap();
	private final String name;
	private final Block.Settings settings;
	private final Block base;
	private final Callback callback;

	public DecorationBlockInfo(String name, Block baseBlock, Block.Settings settings)
	{
		this(name, baseBlock, settings, null);
	}

	public DecorationBlockInfo(String name, Block baseBlock, Block.Settings settings, Callback callback)
	{
		this.name = name;
		this.settings = settings;
		this.base = baseBlock;
		this.callback = callback;
	}

	public DecorationBlockInfo slab()
	{
		set(Type.SLAB, new SlabBlock(settings));
		return this;
	}

	public DecorationBlockInfo stair()
	{
		set(Type.STAIR, new BMStairBlock(base.getDefaultState(), settings));
		return this;
	}

	public DecorationBlockInfo wall()
	{
		set(Type.WALL, new WallBlock(settings));
		return this;
	}

	public DecorationBlockInfo all()
	{
		return slab().stair().wall();
	}

	private void set(Type type, Block block)
	{
		this.blocks.put(type, block);
	}

	public Block get(Type type)
	{
		return blocks.get(type);
	}

	public DecorationBlockInfo register()
	{
		for(Type key : blocks.keySet())
		{
			Block bl = Registry.register(Registry.BLOCK, key.make(name), blocks.get(key));
			Registry.register(Registry.ITEM, key.make(name), new BlockItem(bl, BMItems.settings()));

			if(callback != null)
			{
				callback.onCreateBlock(bl);
			}
		}

		return this;
	}

	public interface Callback
	{
		void onCreateBlock(Block block);
	}

	public enum Type
	{
		SLAB("slab"), STAIR("stairs"), WALL("wall");

		private final String postfix;

		Type(String postfix)
		{
			this.postfix = postfix;
		}

		public Identifier make(String name)
		{
			return BiomeMakeover.ID(name + "_" + postfix);
		}
	}
}