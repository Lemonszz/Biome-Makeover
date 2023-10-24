package party.lemons.biomemakeover.init;

import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;

import java.util.Optional;
import java.util.function.Supplier;

public class BMAi
{
    private static final DeferredRegister<MemoryModuleType<?>> MEMORIES = DeferredRegister.create(Constants.MOD_ID, Registries.MEMORY_MODULE_TYPE);

    public static final RegistrySupplier<MemoryModuleType<Unit>> CRAFTING_COOLDOWN = MEMORIES.register(BiomeMakeover.ID("crafting_cooldown"), memory(Codec.unit(Unit.INSTANCE)));

    public static void init()
    {
        MEMORIES.register();
    }

    private static <U> Supplier<MemoryModuleType<U>> memory(Codec<U> codec)
    {
        return ()->new MemoryModuleType<>(Optional.of(codec));
    }
}
