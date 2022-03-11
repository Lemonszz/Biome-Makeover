package party.lemons.biomemakeover.init.fabric;

import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.world.level.block.ComposterBlock;
import party.lemons.biomemakeover.init.BMBlocks;

public class BMBlocksImpl {
    public static void postRegister() {
        BMBlocks.initModifiers();
        LifecycleEvent.SETUP.register(()->{
            BMBlocks.initCompost();
        });
    }
}
