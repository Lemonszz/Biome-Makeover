package party.lemons.biomemakeover.util.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.block.BlockProperties;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import party.lemons.biomemakeover.block.*;
import party.lemons.biomemakeover.block.modifier.FlammableModifier;
import party.lemons.biomemakeover.block.modifier.RTypeModifier;
import party.lemons.biomemakeover.block.modifier.StrippableModifier;
import party.lemons.biomemakeover.init.BMSigns;
import party.lemons.biomemakeover.item.BMBoatItem;
import party.lemons.biomemakeover.util.access.BlockEntityTypeAccess;
import party.lemons.biomemakeover.util.registry.boat.BoatType;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WoodTypeInfo
{
    private final List<Type> types = Lists.newArrayList();
    private final List<WoodTypeInfo.Type> itemTypes = Lists.newArrayList();

    private final Map<Type, Supplier<Block>> blocks = Maps.newHashMap();
    private final Map<Type, Supplier<Item>> items = Maps.newHashMap();
    private final WoodType woodType;
    private final String name;
    private final BlockBehaviour.Properties properties;
    private final Consumer<Supplier<Block>> callback;
    private final String modid;
    private final CreativeModeTab tab;
    private Supplier<BoatType> boatType;

    public WoodTypeInfo(String modid, CreativeModeTab group, String name)
    {
        this(modid, group, name, BlockBehaviour.Properties.of(Material.WOOD).strength(2F, 3F).sound(SoundType.WOOD), null);
    }

    public WoodTypeInfo(String modid, CreativeModeTab group, String name, Block.Properties settings, Consumer<Supplier<Block>> callback)
    {
        this.modid = modid;
        this.name = name;
        this.properties = settings;
        this.callback = callback;
        this.tab = group;

        woodType = BMSigns.getSignType("bm_" + name);
        types.add(Type.LOG);
        types.add(Type.STRIPPED_LOG);
        types.add(Type.PLANK);
    }

    public WoodTypeInfo slab()
    {
        types.add(Type.SLAB);
        return this;
    }

    public WoodTypeInfo stair()
    {
        types.add(Type.STAIR);
        return this;
    }

    public WoodTypeInfo fence()
    {
        types.add(Type.FENCE);
        types.add(Type.FENCE_GATE);

        return this;
    }

    public WoodTypeInfo wood()
    {
        types.add(Type.WOOD);
        types.add(Type.STRIPPED_WOOD);

        return this;
    }

    public WoodTypeInfo pressure_plate()
    {
        types.add(Type.PRESSURE_PLATE);
        return this;
    }

    public WoodTypeInfo button()
    {
        types.add(Type.BUTTON);
        return this;
    }

    public WoodTypeInfo trapdoor()
    {
        types.add(Type.TRAP_DOOR);
        return this;
    }

    public WoodTypeInfo door()
    {
        types.add(Type.DOOR);
        return this;
    }

    public WoodTypeInfo sign()
    {
        types.add(Type.SIGN);
        types.add(Type.SIGN_WALL);

        itemTypes.add(Type.SIGN_ITEM);

        return this;
    }

    public WoodTypeInfo boat(Supplier<BoatType> boatType)
    {
        this.boatType = boatType;
        itemTypes.add(Type.BOAT);
        return this;
    }

    public Item.Properties properties()
    {
        return new Item.Properties().tab(this.tab);
    }

    public WoodTypeInfo all(Supplier<BoatType> boatType)
    {
        return slab().stair().fence().wood().pressure_plate().button().trapdoor().door().sign().boat(boatType);
    }

    private void set(Type type, Supplier<Block> block)
    {
        this.blocks.put(type, block);
    }

    private void setItem(Type type, Supplier<Item> item)
    {
        this.items.put(type, item);
    }

    public Supplier<Block> getBlock(Type type)
    {
        return blocks.get(type);
    }

    public Supplier<Item> getItem(Type type)
    {
        return items.get(type);
    }

    public WoodTypeInfo register(DeferredRegister<Block> blockRegister, DeferredRegister<Item> itemRegister)
    {
        for(Type type : types) {
            Supplier<Block> blockSupplier = type.blockSupplier.getSupplier(this);

            if (blockSupplier != null) {
                ResourceLocation id = type.make(this.modid, name);
                RegistrySupplier<Block> regBlock = blockRegister.register(id, blockSupplier);
                set(type, regBlock);

                if (type.hasBlockItem) {
                    itemRegister.register(id, () -> new BlockItem(regBlock.get(), properties()));
                }

                if (callback != null) {
                    callback.accept(regBlock);
                }
            }
        }
        for(Type type : itemTypes)
        {
            Supplier<Item> itemSupplier = type.itemSupplier.getSupplier(this);

            if(itemSupplier != null) {
                RegistrySupplier<Item> item = itemRegister.register(type.make(modid, name), itemSupplier);
                setItem(type, item);
            }
        }

         if(types.contains(Type.SIGN)) {
             LifecycleEvent.SETUP.register(()->{
                 ((BlockEntityTypeAccess)BlockEntityType.SIGN).bm_addBlockTypes( blocks.get(Type.SIGN).get(), blocks.get(Type.SIGN_WALL).get());
             });
         }
        return this;
    }

    @FunctionalInterface
    private interface TypeBlockSupplier<T>
    {
        public Supplier<T> getSupplier(WoodTypeInfo factory);
    }

    public enum Type
    {
        STRIPPED_WOOD("stripped", "wood", true, (f)->()->new BMPillarBlock(BlockProperties.copy(f.properties).explosionResistance(2.0F)).modifiers(FlammableModifier.WOOD)),
        STRIPPED_LOG("stripped", "log", true, (f)->()->new BMPillarBlock(BlockProperties.copy(f.properties).explosionResistance(2.0F)).modifiers(FlammableModifier.WOOD)),
        PLANK("", "planks", true, (f)->()->new BMBlock(BlockProperties.copy(f.properties)).modifiers(FlammableModifier.WOOD)),
        LOG("", "log", true, (f)->()->new BMPillarBlock(BlockProperties.copy(f.properties).explosionResistance(2.0F)).modifiers(FlammableModifier.WOOD, new StrippableModifier(()->f.getBlock(Type.STRIPPED_LOG).get()))),
        WOOD("", "wood", true, (f)->()->new BMPillarBlock(BlockProperties.copy(f.properties).explosionResistance(2.0F)).modifiers(FlammableModifier.WOOD, new StrippableModifier(()->f.getBlock(Type.STRIPPED_WOOD).get()))),
        SLAB("", "slab", true, (f)->()->new BMSlabBlock(BlockProperties.copy(f.properties)).modifiers(FlammableModifier.WOOD)),
        STAIR("", "stairs", true, (f)->()->new BMStairBlock(f.getBlock(Type.PLANK).get().defaultBlockState(), BlockProperties.copy(f.properties)).modifiers(FlammableModifier.WOOD)),
        FENCE("", "fence", true, (f)->()->new BMFenceBlock(BlockProperties.copy(f.properties)).modifiers(FlammableModifier.WOOD)),
        FENCE_GATE("", "fence_gate", true, (f)->()->new BMFenceGateBlock(BlockProperties.copy(f.properties)).modifiers(FlammableModifier.WOOD)),
        PRESSURE_PLATE("", "pressure_plate", true, (f)->()->new BMPressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockProperties.copy(f.properties).strength(0.5F).noCollission())),
        BUTTON("", "button", true, (f)-> ()->new BMButtonBlock(BlockProperties.copy(f.properties).strength(0.5F).noCollission())),
        TRAP_DOOR("", "trapdoor", true, (f)->()->new BMTrapdoorBlock(BlockProperties.copy(f.properties).strength(3F).noOcclusion()).modifiers(RTypeModifier.create(RType.CUTOUT))),
        DOOR("", "door", true, (f)->()->new BMDoorBlock(BlockProperties.copy(f.properties).strength(3.0F).noOcclusion()).modifiers(RTypeModifier.create(RType.CUTOUT))),
        SIGN("", "sign", false, (f)->()->new StandingSignBlock(BlockProperties.of(Material.WOOD).strength(1F).sound(SoundType.WOOD).noCollission(), f.woodType)),
        SIGN_WALL("", "wall_sign", false, (f)->()->new WallSignBlock(BlockProperties.of(Material.WOOD).strength(1F).sound(SoundType.WOOD).noCollission(), f.woodType)),
        SIGN_ITEM("", "sign", false, null, (f)->()->new SignItem(f.properties().stacksTo(16), f.getBlock(Type.SIGN).get(), f.getBlock(Type.SIGN_WALL).get())),
        BOAT("", "boat", false, null, (f)->()->new BMBoatItem(f.boatType, f.properties().stacksTo(1)));

        private final String postfix;
        private final String prefix;
        private final boolean hasBlockItem;
        private final TypeBlockSupplier<Block> blockSupplier;
        private final TypeBlockSupplier<Item> itemSupplier;

        Type(String prefix, String postfix, boolean hasBlockItem, TypeBlockSupplier<Block> blockSupplier)
        {
            this(prefix, postfix, hasBlockItem, blockSupplier, null);
        }

        Type(String prefix, String postfix, boolean hasBlockItem, TypeBlockSupplier<Block> blockSupplier, TypeBlockSupplier<Item> itemSupplier)
        {
            this.postfix = postfix;
            this.prefix = prefix;
            this.hasBlockItem = hasBlockItem;
            this.blockSupplier = blockSupplier;
            this.itemSupplier = itemSupplier;
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
