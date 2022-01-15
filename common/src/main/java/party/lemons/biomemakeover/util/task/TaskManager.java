package party.lemons.biomemakeover.util.task;

import dev.architectury.event.events.common.TickEvent;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public final class TaskManager
{
    private final static List<Runnable> tasks = Lists.newArrayList();

    public static void init()
    {
        TickEvent.SERVER_LEVEL_POST.register((s)->tick());
    }

    public static void addTask(Runnable runnable)
    {
        tasks.add(runnable);
    }

    private static void tick()
    {
        tasks.forEach(Runnable::run);
        tasks.clear();
    }

    private TaskManager(){}
}
