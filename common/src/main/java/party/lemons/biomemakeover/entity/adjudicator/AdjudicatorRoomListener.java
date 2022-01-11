package party.lemons.biomemakeover.entity.adjudicator;

import com.google.common.collect.Lists;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AdjudicatorRoomListener {
    private static List<AdjudicatorEntity> activeAdjudicators = Lists.newArrayList();

    public static void init() {
        BlockEvent.BREAK.register((level, pos, state, player, val) -> {
                    checkAndActivate(level, pos);

                    return EventResult.pass();
                }
        );

        BlockEvent.PLACE.register((level, pos, state, entity) -> {
            checkAndActivate(level, pos);

            return EventResult.pass();
        });
    }

    public static void checkAndActivate(Level level, BlockPos pos) {
        if (level.isClientSide())
            return;

        activeAdjudicators.removeIf((e) -> !e.isAlive() || e.isRemoved());
        for (AdjudicatorEntity adj : activeAdjudicators) {
            if (adj.getArenaBounds() != null && adj.getArenaBounds().contains(new Vec3(pos.getX(), pos.getY(), pos.getZ()))) {
                adj.setActive();
            }
        }
    }

    public static void enableAdjudicator(AdjudicatorEntity entity) {
        activeAdjudicators.add(entity);
    }

    public static void disableAdjudicator(AdjudicatorEntity entity) {
        activeAdjudicators.remove(entity);
    }

}
