package party.lemons.biomemakeover.util.registry;

import com.google.common.collect.Maps;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.block.BMStairBlock;
import party.lemons.biomemakeover.init.BMItems;

import java.util.Map;

public class DecorationBlockInfo
{
    private final Map<Type, Block> blocks = Maps.newHashMap();
    private final String name;
    private final BlockBehaviour.Properties settings;
    private final Block base;
    private final Callback callback;
    private final String modid;

    public DecorationBlockInfo(String modid, String name, Block baseBlock, Block.Properties settings)
    {
        this(modid, name, baseBlock, settings, null);
    }

    public DecorationBlockInfo(String modid, String name, Block baseBlock, Block.Properties settings, Callback callback)
    {
        this.modid = modid;
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
        set(Type.STAIR, new BMStairBlock(base.defaultBlockState(), settings));
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
        DeferredRegister bR = DeferredRegister.create(Constants.MOD_ID, Registry.BLOCK_REGISTRY);
        DeferredRegister iR = DeferredRegister.create(Constants.MOD_ID, Registry.ITEM_REGISTRY);

        for(Type key : blocks.keySet())
        {
            Block bl = blocks.get(key);
            bR.register(key.make(modid, name), ()->bl);
            iR.register( key.make(modid, name), ()->new BlockItem(bl, BMItems.properties()));

            if(callback != null)
            {
                callback.onCreateBlock(bl);
            }
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