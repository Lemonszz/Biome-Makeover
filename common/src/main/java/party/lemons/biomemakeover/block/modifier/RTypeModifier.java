package party.lemons.biomemakeover.block.modifier;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.world.level.block.Block;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.registry.RType;

public record RTypeModifier(RType type) implements BlockModifier {
    public static RTypeModifier create(RType type) {
        return new RTypeModifier(type);
    }

    @Override
    public void accept(Block block) {
        EnvExecutor.runInEnv(Env.CLIENT, () -> () ->
        {
            if (type != null) {
                BMBlocks.RTYPES.put(block, type);
            }
        });
    }
}