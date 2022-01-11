package party.lemons.biomemakeover.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Random;

public class BlightBatEntity extends Bat {
    public BlightBatEntity(EntityType<? extends Bat> entityType, Level level) {
        super(entityType, level);
    }

    public static boolean checkSpawnRules(EntityType<BlightBatEntity> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, Random random) {
        if (blockPos.getY() >= levelAccessor.getSeaLevel()) {
            return false;
        }
        int i = levelAccessor.getMaxLocalRawBrightness(blockPos);
        int j = 4;
        if (isHalloween()) {
            j = 7;
        } else if (random.nextBoolean()) {
            return false;
        }
        if (i > random.nextInt(j)) {
            return false;
        }
        return checkMobSpawnRules(entityType, levelAccessor, mobSpawnType, blockPos, random);
    }

    private static boolean isHalloween() {
        LocalDate localDate = LocalDate.now();
        int i = localDate.get(ChronoField.DAY_OF_MONTH);
        int j = localDate.get(ChronoField.MONTH_OF_YEAR);
        return j == 10 && i >= 20 || j == 11 && i <= 3;
    }
}
