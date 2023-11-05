package party.lemons.biomemakeover.entity.behavior;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import party.lemons.biomemakeover.crafting.grinding.GrindingRecipe;
import party.lemons.biomemakeover.entity.DustDevilEntity;
import party.lemons.biomemakeover.init.BMAi;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,
            MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM,
            MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM,
            BMAi.CRAFTING_COOLDOWN.get(),
            BMAi.IS_GRINDING.get(),
            BMAi.IS_GRINDING_DISABLED.get()
    );

    public static Brain<DustDevilEntity> makeBrain(Brain<DustDevilEntity> brain)
    {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        initGrindActivity(brain);
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
                        StopGrindingIfComplete.create(),
                        StartGrindingItemIfSeen.create(120),
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

    private static void initGrindActivity(Brain<DustDevilEntity> brain)
    {
        brain.addActivityAndRemoveMemoryWhenStopped(
                BMAi.GRINDING.get(),
                10,
                ImmutableList.of(
                        GoToWantedItem.create(DustDevilAI::isNotHoldingGrindItem, 1.0F, true, 9),
                        StopGrindingIfItemTooFarAway.create(9),
                        StopGrindingIfTimeout.create(200, 200)
                ),
                BMAi.IS_GRINDING.get()
        );
    }

    private static boolean isNotHoldingGrindItem(DustDevilEntity dustDevil)
    {
        return dustDevil.getInventory().isEmpty() || !isGrindableItem(dustDevil);
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
                        StopAttackingIfTargetInvalid.create(),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        MeleeAttack.create(25)
                ),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    public static void updateActivity(DustDevilEntity dustDevil) {
        Brain<DustDevilEntity> brain = dustDevil.getBrain();
        Activity oldActivity = brain.getActiveNonCoreActivity().orElse(null);

        dustDevil.getBrain().setActiveActivityToFirstValid(ImmutableList.of(BMAi.GRINDING.get(), Activity.FIGHT, Activity.IDLE));
        Activity newActivity = brain.getActiveNonCoreActivity().orElse(null);

        boolean newHasTornado = hasTornado(newActivity);

        dustDevil.setGrinding(newActivity == BMAi.GRINDING.get());

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

    protected static boolean isGrindableItem(DustDevilEntity dustDevil, ItemStack itemStack)
    {
        final SimpleContainer checkContainer = new SimpleContainer(1);
        checkContainer.setItem(0, itemStack);

        return dustDevil.recipeCheck.getRecipeFor(checkContainer, dustDevil.level()).isPresent();
    }

    protected static boolean isGrindableItem(DustDevilEntity dustDevil)
    {
        return dustDevil.recipeCheck.getRecipeFor(dustDevil.getInventory(), dustDevil.level()).isPresent();
    }

    public static boolean hasTornado(Activity activity)
    {
        return activity == Activity.FIGHT || activity == BMAi.GRINDING.get();
    }

    public static void pickUpItem(DustDevilEntity dustDevil, ItemEntity stack)
    {
        stopWalking(dustDevil);

        dustDevil.take(stack, 1);
        ItemStack itemStack = removeOneItemFromItemEntity(stack);

        if (isGrindableItem(dustDevil, itemStack))
        {
            dustDevil.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            holdItem(dustDevil, itemStack);
            grindItem(dustDevil);
        }
    }

    private static void holdItem(DustDevilEntity dustDevil, ItemStack itemStack) {
        if (!dustDevil.getMainHandItem().isEmpty()) {
            dustDevil.spawnAtLocation(dustDevil.getMainHandItem());
        }

        dustDevil.holdItem(itemStack);
    }

    private static void stopWalking(DustDevilEntity dustDevil) {
        dustDevil.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        dustDevil.getNavigation().stop();
    }

    private static ItemStack removeOneItemFromItemEntity(ItemEntity itemEntity) {
        ItemStack itemStack = itemEntity.getItem();
        ItemStack itemStack2 = itemStack.split(1);
        if (itemStack.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(itemStack);
        }

        return itemStack2;
    }

    public static void wasHurtBy(DustDevilEntity dustDevil, LivingEntity hurtingEntity) {
        if (!dustDevil.getMainHandItem().isEmpty()) {
            stopGrindingItem(dustDevil, false);
        }

        Brain<DustDevilEntity> brain = dustDevil.getBrain();
        brain.eraseMemory(BMAi.IS_GRINDING.get());
        if (hurtingEntity instanceof Player) {
            brain.setMemoryWithExpiry(BMAi.IS_GRINDING_DISABLED.get(), true, 250L);
        }

        maybeRetaliate(dustDevil, hurtingEntity);
    }

    protected static void maybeRetaliate(DustDevilEntity dustDevil, LivingEntity target) {
        if (Sensor.isEntityAttackableIgnoringLineOfSight(dustDevil, target) && !BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(dustDevil, target, 4.0))
        {
           if(dustDevil.canAttack(target))
           {
               dustDevil.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
           }
        }
    }

    private static void grindItem(LivingEntity livingEntity) {
        livingEntity.getBrain().setMemoryWithExpiry(BMAi.IS_GRINDING.get(), true, 120L);
    }

    protected static void stopGrindingItem(DustDevilEntity dustDevil, boolean finishedGrinding) {
        ItemStack itemstack = dustDevil.getItemInHand(InteractionHand.MAIN_HAND);
        dustDevil.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        dustDevil.getInventory().setItem(0, ItemStack.EMPTY);
        if (finishedGrinding)
        {
            throwItems(dustDevil, getGrindingResultItems(dustDevil));
        }
        else
        {
            throwItems(dustDevil, Collections.singletonList(itemstack));
        }
        dustDevil.onGrindFinished(finishedGrinding);
    }

    private static List<ItemStack> getGrindingResultItems(DustDevilEntity dustDevil)
    {
        Optional<GrindingRecipe> recipe = dustDevil.getGrindRecipe();
        return recipe.map(grindingRecipe -> Collections.singletonList(grindingRecipe.assemble(dustDevil.getInventory(), dustDevil.level().registryAccess()))).orElse(Collections.emptyList());
    }

    private static void throwItems(DustDevilEntity dustDevil, List<ItemStack> list) {
        Vec3 position = LandRandomPos.getPos(dustDevil, 4, 2);
        position = position == null ? dustDevil.position() : position;

        if (!list.isEmpty()) {
            dustDevil.swing(InteractionHand.OFF_HAND);

            for(ItemStack itemstack : list) {
                BehaviorUtils.throwItem(dustDevil, itemstack, position.add(0.0, 1.0, 0.0));
            }
        }
    }

    public static boolean wantsToPickup(DustDevilEntity dustDevilEntity, ItemStack item)
    {
        Brain<DustDevilEntity> brain = dustDevilEntity.getBrain();

        if(brain.hasMemoryValue(BMAi.IS_GRINDING_DISABLED.get()) && brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET))
            return false;

        if(isGrindableItem(dustDevilEntity, item))
            return isNotHoldingGrindItem(dustDevilEntity);

        return false;
    }

    public static class StartGrindingItemIfSeen {
        public static BehaviorControl<DustDevilEntity> create(int i)
        {
            return BehaviorBuilder.create(inst -> inst.group
                    (
                            inst.present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM),
                            inst.absent(BMAi.IS_GRINDING.get()),
                            inst.absent(BMAi.IS_GRINDING_DISABLED.get()),
                            inst.absent(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM)

                    ).apply(inst, (visibleItem, isGrinding, isGrindingDisabled, isWalkDisabled) -> ((level, entity, l) -> {
                        ItemEntity itemEntity = inst.get(visibleItem);
                        if(!isGrindableItem(entity, itemEntity.getItem()))
                                return false;

                        isGrinding.setWithExpiry(true, i);
                        return true;
                    }))
            );
        }
    }

    public static class StopGrindingIfItemTooFarAway {
        public static BehaviorControl<LivingEntity> create(int i) {
            return BehaviorBuilder.create(
                    instance -> instance.group(
                                instance.present(BMAi.IS_GRINDING.get()),
                                instance.registered(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM)
                            )
                            .apply(instance, (isGrinding, nearestItem) -> (level, entity, l) ->
                            {
                                if (!entity.getMainHandItem().isEmpty()) {
                                    return false;
                                } else {
                                    Optional<ItemEntity> entityOptional = instance.tryGet(nearestItem);
                                    if (entityOptional.isPresent() && entityOptional.get().closerThan(entity, i)) {
                                        return false;
                                    } else {
                                        isGrinding.erase();
                                        return true;
                                    }
                                }
                            })
            );
        }
    }

    public static class StopGrindingIfTimeout {
        public static BehaviorControl<LivingEntity> create(int i, int timeoutTime)
        {
            return BehaviorBuilder.create(
                    instance -> instance.group(
                                    instance.present(BMAi.IS_GRINDING.get()),
                                    instance.present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM),
                                    instance.registered(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM),
                                    instance.registered(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM)
                            )
                            .apply(instance, (isGrinding, nearestItem, reachTime, timeout) -> (serverLevel, livingEntity, l) -> {
                                if (!livingEntity.getOffhandItem().isEmpty()) {
                                    return false;
                                } else {
                                    Optional<Integer> reachTimeOptional = instance.tryGet(reachTime);
                                    if (reachTimeOptional.isEmpty()) {
                                        reachTime.set(0);
                                    } else {
                                        int k = reachTimeOptional.get();
                                        if (k > i) {
                                            isGrinding.erase();
                                            reachTime.erase();
                                            timeout.setWithExpiry(true, timeoutTime);
                                        } else {
                                            reachTime.set(k + 1);
                                        }
                                    }

                                    return true;
                                }
                            })
            );
        }
    }

    public static class StopGrindingIfComplete {
        public static BehaviorControl<DustDevilEntity> create()
        {
            return BehaviorBuilder.create(inst -> inst.group(inst.absent(BMAi.IS_GRINDING.get())).apply(inst, argx -> (level, entity, l) ->
            {
                if (!entity.getMainHandItem().isEmpty())
                {
                    DustDevilAI.stopGrindingItem(entity, true);
                    return true;
                }
                else {
                    return false;
                }
            }));
        }
    }

}
