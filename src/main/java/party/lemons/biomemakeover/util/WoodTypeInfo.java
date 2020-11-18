package party.lemons.biomemakeover.util;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.block.*;
import party.lemons.biomemakeover.init.BMItems;

import java.util.Map;

public class WoodTypeInfo
{
	private final Map<Type, Block> blocks = Maps.newHashMap();
	private final String name;
	private final Block.Settings settings;
	private final DecorationBlockInfo.Callback callback;
	private final BlockState defaultState;

	public WoodTypeInfo(String name, Block.Settings settings)
	{
		this(name, settings, null);
	}

	public WoodTypeInfo(String name, Block.Settings settings, DecorationBlockInfo.Callback callback)
	{
		this.name = name;
		this.settings = settings;
		this.callback = callback;

		set(Type.LOG, new BMPillarBlock(settings));
		set(Type.STRIPPED_LOG, new BMPillarBlock(settings));
		set(Type.PLANK, new BMBlock(settings));

		defaultState = get(Type.PLANK).getDefaultState();
	}

	public WoodTypeInfo slab()
	{
		set(Type.SLAB, new SlabBlock(settings));
		return this;
	}

	public WoodTypeInfo stair()
	{
		set(Type.STAIR, new BMStairBlock(defaultState, settings));
		return this;
	}

	public WoodTypeInfo fence()
	{
		set(Type.FENCE, new BMFenceBlock(settings));
		set(Type.FENCE_GATE, new BMFenceGateBlock(settings));
		return this;
	}

	public WoodTypeInfo wood()
	{
		set(Type.WOOD, new BMPillarBlock(settings));
		set(Type.STRIPPED_WOOD, new BMPillarBlock(settings));
		return this;
	}

	public WoodTypeInfo pressure_plate()
	{
		set(Type.PRESSURE_PLATE, new BMPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, settings));
		return this;
	}

	public WoodTypeInfo button()
	{
		set(Type.BUTTON, new BMButtonBlock(settings));
		return this;
	}

	public WoodTypeInfo trapdoor()
	{
		set(Type.TRAP_DOOR, new BMTrapdoorBlock(settings));
		return this;
	}

	public WoodTypeInfo door()
	{
		set(Type.DOOR, new BMDoorBlock(settings));
		return this;
	}

	public WoodTypeInfo all()
	{
		return slab().stair().fence().wood().pressure_plate().button().trapdoor().door();
	}

	private void set(Type type, Block block)
	{
		this.blocks.put(type, block);
	}

	public Block get(Type type)
	{
		return blocks.get(type);
	}

	public WoodTypeInfo register()
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
		LOG("","log"),
		WOOD("", "wood"),
		PLANK("", "planks"),
		STRIPPED_LOG("stripped", "log"),
		STRIPPED_WOOD("stripped", "wood"),
		SLAB("", "slab"),
		STAIR("", "stairs"),
		FENCE("", "fence"),
		FENCE_GATE("", "fence_gate"),
		PRESSURE_PLATE("", "pressure_plate"),
		BUTTON("", "button"),
		TRAP_DOOR("", "trapdoor"),
		DOOR("", "door");

		private final String postfix;
		private final String prefix;

		Type(String prefix, String postfix)
		{
			this.postfix = postfix;
			this.prefix = prefix;
		}

		public Identifier make(String name)
		{
			String s = "";
			if(!prefix.isEmpty())
				s += prefix + "_";

			s += name;

			if(!postfix.isEmpty())
				s += "_" + postfix;

			return BiomeMakeover.ID(s);
		}
	}
}
