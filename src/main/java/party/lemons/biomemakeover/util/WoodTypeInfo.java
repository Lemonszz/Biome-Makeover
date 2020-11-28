package party.lemons.biomemakeover.util;

import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SignItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.block.*;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.item.BMBoatItem;
import party.lemons.biomemakeover.util.access.BlockEntityTypeAccessor;
import party.lemons.biomemakeover.util.boat.BoatType;

import java.util.Map;
import java.util.function.Supplier;

public class WoodTypeInfo
{
	private final Map<Type, Block> blocks = Maps.newHashMap();
	private final Map<Type, Item> items = Maps.newHashMap();
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

		defaultState = getBlock(Type.PLANK).getDefaultState();
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

	public WoodTypeInfo sign(SignType type)
	{
		SignBlock standing = new SignBlock(BMBlocks.settings(Material.WOOD, 1.0F).noCollision(), type);
		WallSignBlock wall = new WallSignBlock(BMBlocks.settings(Material.WOOD, 1.0F).noCollision(), type);
		set(Type.SIGN, standing);
		set(Type.SIGN_WALL, wall);
		set(Type.SIGN, new SignItem(BMItems.settings(), standing, wall));

		((BlockEntityTypeAccessor) BlockEntityType.SIGN).addBlockTypes(standing, wall);
		return this;
	}

	public WoodTypeInfo boat(Supplier<BoatType> boatType)
	{
		set(Type.BOAT, new BMBoatItem(()->boatType.get(), BMItems.settings().maxCount(1)));
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

	private void set(Type type, Item item)
	{
		this.items.put(type, item);
	}

	public Block getBlock(Type type)
	{
		return blocks.get(type);
	}

	public Item getItem(Type type)
	{
		return items.get(type);
	}

	public WoodTypeInfo register()
	{
		for(Type key : blocks.keySet())
		{
			Block bl = Registry.register(Registry.BLOCK, key.make(name), blocks.get(key));

			if(key.hasBlockItem)
				Registry.register(Registry.ITEM, key.make(name), new BlockItem(bl, BMItems.settings()));

			if(callback != null)
			{
				callback.onCreateBlock(bl);
			}
		}

		for(Type key : items.keySet())
		{
			Registry.register(Registry.ITEM, key.make(name), items.get(key));
		}
		return this;
	}

	public interface Callback
	{
		void onCreateBlock(Block block);
	}

	public enum Type
	{
		LOG("","log", true),
		WOOD("", "wood", true),
		PLANK("", "planks", true),
		STRIPPED_LOG("stripped", "log", true),
		STRIPPED_WOOD("stripped", "wood", true),
		SLAB("", "slab", true),
		STAIR("", "stairs", true),
		FENCE("", "fence", true),
		FENCE_GATE("", "fence_gate", true),
		PRESSURE_PLATE("", "pressure_plate", true),
		BUTTON("", "button", true),
		TRAP_DOOR("", "trapdoor", true),
		DOOR("", "door", true),
		SIGN("", "sign", false),
		SIGN_WALL("", "wall_sign", false),
		BOAT("", "boat", true);

		private final String postfix;
		private final String prefix;
		private final boolean isItem;
		private final boolean hasBlockItem;

		Type(String prefix, String postfix, boolean hasBlockItem)
		{
			this(prefix, postfix, hasBlockItem, false);
		}

		Type(String prefix, String postfix, boolean hasBlockItem, boolean isItem)
		{
			this.postfix = postfix;
			this.prefix = prefix;
			this.isItem = isItem;
			this.hasBlockItem = hasBlockItem;
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
