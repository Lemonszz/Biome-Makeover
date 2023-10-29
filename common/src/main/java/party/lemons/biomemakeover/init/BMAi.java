package party.lemons.biomemakeover.init;

import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;

import java.util.Optional;
import java.util.function.Supplier;

public class BMAi
{
    private static final DeferredRegister<MemoryModuleType<?>> MEMORIES = DeferredRegister.create(Constants.MOD_ID, Registries.MEMORY_MODULE_TYPE);
    private static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(Constants.MOD_ID, Registries.ACTIVITY);

    public static final RegistrySupplier<MemoryModuleType<Unit>> CRAFTING_COOLDOWN = MEMORIES.register(BiomeMakeover.ID("crafting_cooldown"), memory(Codec.unit(Unit.INSTANCE)));
    public static final RegistrySupplier<MemoryModuleType<Boolean>> IS_GRINDING = MEMORIES.register(BiomeMakeover.ID("is_grinding"), memory(Codec.BOOL));
    public static final RegistrySupplier<MemoryModuleType<Boolean>> IS_GRINDING_DISABLED = MEMORIES.register(BiomeMakeover.ID("is_grinding_disabled"), memory(Codec.BOOL));

    public static final RegistrySupplier<Activity> GRINDING = ACTIVITIES.register(BiomeMakeover.ID("grinding"), ()->activity("grinding"));

    private static Activity activity(String name)
    {
        return new Activity(name);
    }

    public static void init()
    {
        MEMORIES.register();
        ACTIVITIES.register();
    }

    private static <U> Supplier<MemoryModuleType<U>> memory(Codec<U> codec)
    {
        return ()->new MemoryModuleType<>(Optional.of(codec));
    }
}
