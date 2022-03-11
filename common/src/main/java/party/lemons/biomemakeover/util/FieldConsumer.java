package party.lemons.biomemakeover.util;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public class FieldConsumer
{
    public static <T> void run(Class<?> from, Class<T> typeClass, Consumer<T> consumer)
    {
        try
        {
            Field[] fields = from.getDeclaredFields();

            for(Field field : fields)
            {
                if(typeClass.isAssignableFrom(field.getType()))
                {
                    consumer.accept((T)field.get(from));
                }
            }

        }catch(Exception e)
        {
            //if crash == true; dont();
            e.printStackTrace();
        }
    }
}
