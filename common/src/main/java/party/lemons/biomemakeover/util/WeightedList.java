package party.lemons.biomemakeover.util;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class WeightedList<T>
{
    private final List<Entry<T>> entries = Lists.newArrayList();
    private int totalWeight = 0;

    public void add(T object, int weight)
    {
        entries.add(new Entry<T>(object, weight));
        totalWeight += weight;
    }

    public T sample()
    {
        Collections.shuffle(entries);
        int w = totalWeight;

        for(Entry<T> entry : entries)
        {
            w -= entry.weight;
            if(w <= 0)
                return entry.object;
        }

        throw new RuntimeException("Weighted List Was Empty");
    }


    private record Entry<T>(T object, int weight)
    {
    }
}
