package party.lemons.biomemakeover.util.registry;

import com.google.common.collect.Maps;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.apache.commons.lang3.tuple.Pair;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public final class RegistryHelper
{
    //Forge bullshit
    //Let me add to a vanilla registry you cowards
    private static HashMap<Registry, DeferredRegister> DEFERRED_REGISTRIES = Maps.newHashMap();


    /***
     * Registers all static final fields of the given type in the given class to the given registry
     * @param modid
     * @param registry
     * @param typeClass
     * @param from
     * @param callbacks
     */
    @SafeVarargs
    public static <T> void register(String modid, Registry<T> registry, Class typeClass, Class from, RegistryCallback<T>... callbacks)
    {
        ResourceKey<Registry<T>> key = (ResourceKey<Registry<T>>) registry.key();
        DeferredRegister<T> register = DeferredRegister.create(modid, key);

        try
        {
            Field[] fields = from.getDeclaredFields();

            for(Field field : fields)
            {
                if(typeClass.isAssignableFrom(field.getType()) && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
                {

                    T value = (T) field.get(from);
                    String regName = field.getName().toLowerCase(Locale.ENGLISH);
                    ResourceLocation id = new ResourceLocation(modid, regName);

                    register.register(id, ()->value);
                    for(RegistryCallback<T> cb : callbacks)
                    {
                        cb.callback(registry, value, id);
                    }
                }
            }

            register.register();

        }catch(Exception e)
        {
            //if crash == true; dont();
            e.printStackTrace();
        }
    }

    public static <T> void registerObject(Registry<T> registry, ResourceLocation id, T object)
    {
        DeferredRegister r = DeferredRegister.create(id.getNamespace(), (ResourceKey)registry.key());
        r.register(id, ()->object);
        r.register();
    }

    public static <T> void gatherFields(String modid, Class<T> typeClass, Class<?> from, List<Pair<ResourceLocation, T>> list)
    {
        try {
            Field[] fields = from.getDeclaredFields();

            for (Field field : fields) {
                if (typeClass.isAssignableFrom(field.getType()) && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {

                    T value = (T) field.get(from);
                    String regName = field.getName().toLowerCase(Locale.ENGLISH);
                    ResourceLocation id = new ResourceLocation(modid, regName);

                    list.add(Pair.of(id, value));
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public interface RegistryCallback<T>
    {
        void callback(Registry<T> registry, T registryObject, ResourceLocation identifier);
    }

    public static class BlockWithItemCallback implements RegistryCallback<Block>
    {
        private final CreativeModeTab group;

        public BlockWithItemCallback(CreativeModeTab group)
        {
            this.group = group;
        }

        @Override
        public void callback(Registry<Block> registry, Block bl, ResourceLocation id)
        {
            if(!(bl instanceof BlockWithItem)) return;

            BlockWithItem info = (BlockWithItem) bl;
            if(!info.hasItem()) return;

            info.registerItem(id, group);
        }
    }

    private RegistryHelper()
    {
    }
}