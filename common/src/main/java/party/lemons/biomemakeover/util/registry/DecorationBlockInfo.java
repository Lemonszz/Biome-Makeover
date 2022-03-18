package party.lemons.biomemakeover.util.registry;

import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import party.lemons.biomemakeover.block.BMSlabBlock;
import party.lemons.biomemakeover.block.BMStairBlock;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DecorationBlockInfo
{
    public static final List<DecorationBlockInfo> REGISTERED_FACTORIES = Lists.newArrayList();

    private final List<DecorationBlockInfo.Type> types = Lists.newArrayList();
    private final Map<DecorationBlockInfo.Type, Supplier<Block>> blocks = Maps.newHashMap();
    private final String name;
    private final BlockBehaviour.Properties settings;
    private final Supplier<Block> base;
    private final Consumer<Supplier<Block>> callback;
    private final String modid;
    private final CreativeModeTab tab;
    protected Supplier<Item.Properties> blockItemProperties;

    public DecorationBlockInfo(String modid, CreativeModeTab tab, String name, Supplier<Block> baseBlock, Block.Properties settings)
    {
        this(modid, tab, name, baseBlock, settings, null);
    }

    public DecorationBlockInfo(String modid, CreativeModeTab tab, String name, Supplier<Block> baseBlock, Block.Properties settings, Consumer<Supplier<Block>> callback)
    {
        this.modid = modid;
        this.name = name;
        this.settings = settings;
        this.base = baseBlock;
        this.tab = tab;
        this.callback = callback;

        this.blockItemProperties = ()->new Item.Properties().tab(this.tab);
    }

    public DecorationBlockInfo slab()
    {
        types.add(DecorationBlockInfo.Type.SLAB);
        return this;
    }

    public DecorationBlockInfo stair()
    {
        types.add(DecorationBlockInfo.Type.STAIR);
        return this;
    }

    public DecorationBlockInfo wall()
    {
        types.add(DecorationBlockInfo.Type.WALL);
        return this;
    }

    public DecorationBlockInfo all()
    {
        return slab().stair().wall();
    }

    public DecorationBlockInfo blocKItemProperties(Supplier<Item.Properties> properties)
    {
        this.blockItemProperties = properties;
        return this;
    }

    private void set(DecorationBlockInfo.Type type, Supplier<Block> block)
    {
        this.blocks.put(type, block);
    }

    public Supplier<Block> get(DecorationBlockInfo.Type type)
    {
        return blocks.get(type);
    }

    public boolean has(DecorationBlockInfo.Type type)
    {
        return blocks.containsKey(type);
    }

    public Supplier<Block> getBase()
    {
        return base;
    }

    public DecorationBlockInfo register(DeferredRegister<Block> blockRegister, DeferredRegister<Item> itemRegister)
    {
        for(DecorationBlockInfo.Type type : types)
        {
            switch (type)
            {
                case SLAB -> {
                    set(type, ()->new BMSlabBlock(this.settings));
                }
                case STAIR -> {
                    set(type, ()->new BMStairBlock(base.get().defaultBlockState(), this.settings));
                }
                case WALL -> {
                    set(type, ()->new WallBlock(this.settings));
                }
            }
        }

        for(DecorationBlockInfo.Type key : blocks.keySet())
        {
            Supplier<Block> bl = blocks.get(key);

            ResourceLocation id = key.make(this.modid, name);

            RegistrySupplier<Block> regBlock = blockRegister.register(id, bl);
            itemRegister.register(id, ()->new BlockItem(regBlock.get(), blockItemProperties.get()));

            if(callback != null)
            {
                callback.accept(bl);
            }
        }

        REGISTERED_FACTORIES.add(this);
        return this;
    }

    public enum Type
    {
        SLAB("slab"), STAIR("stairs"), WALL("wall");

        private final String postfix;

        Type(String postfix)
        {
            this.postfix = postfix;
        }

        public ResourceLocation make(String modid, String name)
        {
            return new ResourceLocation(modid, name + "_" + postfix);
        }
    }
}