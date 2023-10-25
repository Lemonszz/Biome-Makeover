package party.lemons.biomemakeover.entity.behavior;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.allay.AllayAi;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.hoglin.HoglinAi;
import net.minecraft.world.entity.schedule.Activity;
import party.lemons.biomemakeover.entity.DustDevilEntity;
import party.lemons.biomemakeover.init.BMAi;

import java.util.Optional;
import java.util.function.Predicate;

public class DustDevilAI
{

    public static final ImmutableList<SensorType<? extends Sensor<? super DustDevilEntity>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NEAREST_ITEMS
    );
    public static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.PATH,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,
            BMAi.CRAFTING_COOLDOWN.get()
    );

    public static Brain<DustDevilEntity> makeBrain(Brain<DustDevilEntity> brain)
    {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<DustDevilEntity> brain)
    {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS)
                )
        );
    }

    private static void initIdleActivity(Brain<DustDevilEntity> brain) {
        brain.addActivityWithConditions(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, GoToWantedItem.create(allay -> true, 1.75F, true, 32)),
                        Pair.of(1, StartAttacking.create(DustDevilAI::findNearestValidAttackTarget)),

                        Pair.of(3, SetEntityLookTargetSometimes.create(6.0F, UniformInt.of(30, 60))),
                        Pair.of(4,
                                new RunOne<>(
                                        ImmutableList.of(
                                                Pair.of(RandomStroll.stroll(1.0F), 2),
                                                Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 2),
                                                Pair.of(new DoNothing(5, 25), 1)
                                        )
                                )
                        )
                ),
                ImmutableSet.of()
        );
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(DustDevilEntity devil)
    {
        return devil.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
    }

    private static void initFightActivity(Brain<DustDevilEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        MeleeAttack.create(40),
                        StopAttackingIfTargetInvalid.create()
                ),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    public static void updateActivity(DustDevilEntity dustDevil) {
        Brain<DustDevilEntity> brain = dustDevil.getBrain();
        Activity oldActivity = brain.getActiveNonCoreActivity().orElse(null);

        dustDevil.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        Activity newActivity = brain.getActiveNonCoreActivity().orElse(null);

        boolean newHasTornado = hasTornado(newActivity);

        if(newHasTornado)
        {
            if(oldActivity != newActivity && !hasTornado(oldActivity))
                dustDevil.setTornado(true);
        }
        else
        {
            dustDevil.setTornado(false);
        }
    }

    public static boolean hasTornado(Activity activity)
    {
        return activity == Activity.FIGHT;
    }
}
