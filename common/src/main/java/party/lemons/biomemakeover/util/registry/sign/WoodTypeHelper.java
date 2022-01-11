package party.lemons.biomemakeover.util.registry.sign;

import net.minecraft.world.level.block.state.properties.WoodType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public interface WoodTypeHelper
{
    Set<WoodType> bm_getTypes();

    static WoodType register(String name)
    {
        WoodType type = createWoodType(name);

        ((WoodTypeHelper) type).bm_getTypes().add(type);

        return type;
    }

    static WoodType createWoodType(String name)
    {
        try {
            Constructor<WoodType> constructor = WoodType.class.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);

            WoodType type = constructor.newInstance(name);
            return type;
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}