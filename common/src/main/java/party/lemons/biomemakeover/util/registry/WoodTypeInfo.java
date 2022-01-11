package party.lemons.biomemakeover.util.registry;

import com.google.common.collect.Maps;
import dev.architectury.registry.block.BlockProperties;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.block.*;
import party.lemons.biomemakeover.block.modifier.FlammableModifier;
import party.lemons.biomemakeover.block.modifier.RTypeModifier;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.item.BMBoatItem;
import party.lemons.biomemakeover.util.StripUtil;
import party.lemons.biomemakeover.util.access.BlockEntityTypeAccess;
import party.lemons.biomemakeover.util.registry.boat.BoatType;
import party.lemons.biomemakeover.util.registry.sign.WoodTypeHelper;

import java.util.Map;
import java.util.function.Supplier;

public class WoodTypeInfo
{
    private final Map<Type, Block> blocks = Maps.newHashMap();
    private final Map<Type, Item> items = Maps.newHashMap();
    private final WoodType woodType;
    private final String name;
    private final BlockBehaviour.Properties settings;
    private final DecorationBlockInfo.Callback callback;
    private final BlockState defaultState;
    private final String modid;
    private final CreativeModeTab group;

    public WoodTypeInfo(String modid, CreativeModeTab group, String name, Block.Properties settings)
    {
        this(modid, group, name, settings, null);
    }

    public WoodTypeInfo(String modid, CreativeModeTab group, String name, Block.Properties settings, DecorationBlockInfo.Callback callback)
    {
        this.modid = modid;
        this.name = name;
        this.settings = settings;
        this.callback = callback;
        this.group = group;

        woodType = WoodTypeHelper.register(name);

        set(Type.LOG, new BMPillarBlock(settings).modifiers(FlammableModifier.WOOD));
        set(Type.STRIPPED_LOG, new BMPillarBlock(settings).modifiers(FlammableModifier.WOOD));;
        set(Type.PLANK, new BMBlock(settings).modifiers(FlammableModifier.WOOD));

        StripUtil.addStrippedLog(getBlock(Type.LOG), getBlock(Type.STRIPPED_LOG));

        defaultState = getBlock(Type.PLANK).defaultBlockState();
    }

    public WoodTypeInfo slab()
    {
        set(Type.SLAB, new BMSlabBlock(settings).modifiers(FlammableModifier.WOOD));
        return this;
    }

    public WoodTypeInfo stair()
    {
        set(Type.STAIR, new BMStairBlock(defaultState, settings).modifiers(FlammableModifier.WOOD));
        return this;
    }

    public WoodTypeInfo fence()
    {
        set(Type.FENCE, new BMFenceBlock(settings).modifiers(FlammableModifier.WOOD));
        set(Type.FENCE_GATE, new BMFenceGateBlock(settings).modifiers(FlammableModifier.WOOD));
        return this;
    }

    public WoodTypeInfo wood()
    {
        set(Type.WOOD, new BMPillarBlock(settings).modifiers(FlammableModifier.WOOD));
        set(Type.STRIPPED_WOOD, new BMPillarBlock(settings).modifiers(FlammableModifier.WOOD));

        StripUtil.addStrippedLog(getBlock(Type.WOOD), getBlock(Type.STRIPPED_WOOD));

        return this;
    }

    public WoodTypeInfo pressure_plate()
    {
        set(Type.PRESSURE_PLATE, new BMPressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockProperties.copy(settings).noCollission()));
        return this;
    }

    public WoodTypeInfo button()
    {
        set(Type.BUTTON, new BMButtonBlock(BlockProperties.copy(settings).noCollission()));
        return this;
    }

    public WoodTypeInfo trapdoor()
    {
        set(Type.TRAP_DOOR, new BMTrapdoorBlock(BlockProperties.copy(settings).noOcclusion()).modifiers(RTypeModifier.create(RType.CUTOUT)));
        return this;
    }

    public WoodTypeInfo door()
    {
        set(Type.DOOR, new BMDoorBlock(BlockProperties.copy(settings).noOcclusion()).modifiers(RTypeModifier.create(RType.CUTOUT)));
        return this;
    }

    public WoodTypeInfo sign()
    {
        SignBlock standing = new StandingSignBlock(BMBlocks.properties(Material.WOOD, 1.0F).noCollission(), woodType);
        WallSignBlock wall = new WallSignBlock(BMBlocks.properties(Material.WOOD, 1.0F).noCollission(), woodType);
        set(Type.SIGN, standing);
        set(Type.SIGN_WALL, wall);
        set(Type.SIGN, new SignItem(properties(), standing, wall));
        ((BlockEntityTypeAccess) BlockEntityType.SIGN).bm_addBlockTypes(standing, wall);

        return this;
    }

    //Duplicate of BMItems.properties() otherwise BMItems get initialized too early and it's annoying.
    public static Item.Properties properties()
    {
        return new Item.Properties().tab(BiomeMakeover.TAB);
    }

    public WoodTypeInfo boat(Supplier<BoatType> boatType)
    {
        set(Type.BOAT, new BMBoatItem(boatType, properties().stacksTo(1)));
        return this;
    }

    public WoodTypeInfo all()
    {
        return slab().stair().fence().wood().pressure_plate().button().trapdoor().door().sign();
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
        DeferredRegister<Block> bR = DeferredRegister.create(Constants.MOD_ID, Registry.BLOCK_REGISTRY);
        DeferredRegister<Item> iR = DeferredRegister.create(Constants.MOD_ID, Registry.ITEM_REGISTRY);

        for(Type key : blocks.keySet())
        {
            Block bl = blocks.get(key);
            bR.register(key.make(modid, name), ()->bl);

            if(key.hasBlockItem)
                if(bl instanceof BlockWithItem bwi)
                {
                    iR.register( key.make(modid, name), bwi::makeItem);
                }
                else
                {
                    iR.register( key.make(modid, name), ()->new BlockItem(bl, properties()));
                }

            if(callback != null)
            {
                callback.onCreateBlock(bl);
            }
        }

        for(Type key : items.keySet())
        {
            iR.register(key.make(modid, name), ()->items.get(key));
        }

        bR.register();
        iR.register();
        return this;
    }

    public interface Callback
    {
        void onCreateBlock(Block block);
    }

    public enum Type
    {
        LOG("", "log", true), WOOD("", "wood", true), PLANK("", "planks", true), STRIPPED_LOG("stripped", "log", true), STRIPPED_WOOD("stripped", "wood", true), SLAB("", "slab", true), STAIR("", "stairs", true), FENCE("", "fence", true), FENCE_GATE("", "fence_gate", true), PRESSURE_PLATE("", "pressure_plate", true), BUTTON("", "button", true), TRAP_DOOR("", "trapdoor", true), DOOR("", "door", true), SIGN("", "sign", false), SIGN_WALL("", "wall_sign", false), BOAT("", "boat", true);

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

        public ResourceLocation make(String modid, String name)
        {
            String s = "";
            if(!prefix.isEmpty()) s += prefix + "_";

            s += name;

            if(!postfix.isEmpty()) s += "_" + postfix;

            return new ResourceLocation(modid, s);
        }
    }
}