package party.lemons.biomemakeover.mixin.forge;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.StructureSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccessor {

    @Mutable
    @Accessor("settings")
    void bm_setSettings(StructureSettings dimensionStructuresSettings);

    @Invoker("codec")
    Codec<ChunkGenerator> bm_getCodec();
}