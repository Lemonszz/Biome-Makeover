package party.lemons.biomemakeover.init.forge;

import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.world.level.block.ComposterBlock;
import party.lemons.biomemakeover.init.BMBlocks;

public class BMBlocksImpl {
    public static void postRegister() {
        LifecycleEvent.SETUP.register(()->{
            BMBlocks.initModifiers();
            BMBlocks.initCompost();
        });
    }
}
