package party.lemons.biomemakeover.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import party.lemons.biomemakeover.entity.BMBoatEntity;
import party.lemons.biomemakeover.entity.BMChestBoatEntity;
import party.lemons.biomemakeover.util.registry.boat.BoatType;

import java.util.List;
import java.util.function.Supplier;

public class BMBoatItem extends BMItem
{
    private final Supplier<BoatType> type;
    private final boolean hasChest;

    public BMBoatItem(boolean chest, Supplier<BoatType> boatType, Properties settings)
    {
        super(settings);
        this.type = boatType;
        this.hasChest = chest;

        DispenserBlock.registerBehavior(this, new BMBoatDispenseItemBehavior(boatType));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        BlockHitResult hitResult = BoatItem.getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (((HitResult)hitResult).getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        }
        Vec3 vec3 = player.getViewVector(1.0f);
        double scale = 5.0;
        List<Entity> list = level.getEntities(player, player.getBoundingBox().expandTowards(vec3.scale(scale)).inflate(1.0),  EntitySelector.NO_SPECTATORS.and(Entity::isPickable));
        if (!list.isEmpty()) {
            Vec3 vec32 = player.getEyePosition();
            for (Entity entity : list) {
                AABB aABB = entity.getBoundingBox().inflate(entity.getPickRadius());
                if (!aABB.contains(vec32)) continue;
                return InteractionResultHolder.pass(itemStack);
            }
        }
        if (((HitResult)hitResult).getType() == HitResult.Type.BLOCK) {
              BMBoatEntity boatEntity = createBoat(level, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, player.getYRot());
              if (!level.noCollision(boatEntity, boatEntity.getBoundingBox())) {
                  return InteractionResultHolder.fail(itemStack);
              }
            if (!level.isClientSide) {
                level.addFreshEntity(boatEntity);
                level.gameEvent(player, GameEvent.ENTITY_PLACE, new BlockPos(hitResult.getLocation()));
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }
        return InteractionResultHolder.pass(itemStack);
    }

    public BMBoatEntity createBoat(Level world, double x, double y, double z, float yaw)
    {
        BMBoatEntity boatEntity = hasChest ? new BMChestBoatEntity(world, x, y, z) : new BMBoatEntity(world, x, y, z);
        boatEntity.setBoatType(type.get());
        boatEntity.setYRot(yaw);
        return boatEntity;
    }

    private class BMBoatDispenseItemBehavior extends DefaultDispenseItemBehavior
    {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
        private Supplier<BoatType> type;

        public BMBoatDispenseItemBehavior(Supplier<BoatType> type)
        {
            this.type = type;
        }

        @Override
        public ItemStack execute(BlockSource blockSource, ItemStack itemStack) {
            double g;
            Direction direction = blockSource.getBlockState().getValue(DispenserBlock.FACING);
            ServerLevel level = blockSource.getLevel();
            double d = blockSource.x() + (double)((float)direction.getStepX() * 1.125f);
            double e = blockSource.y() + (double)((float)direction.getStepY() * 1.125f);
            double f = blockSource.z() + (double)((float)direction.getStepZ() * 1.125f);
            BlockPos blockPos = blockSource.getPos().relative(direction);
            if (level.getFluidState(blockPos).is(FluidTags.WATER)) {
                g = 1.0;
            } else if (level.getBlockState(blockPos).isAir() && level.getFluidState(blockPos.below()).is(FluidTags.WATER)) {
                g = 0.0;
            } else {
                return this.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
            }
            BMBoatEntity boat = new BMBoatEntity(level, d, e + g, f);
            boat.setBoatType(this.type.get());
            boat.setYRot(direction.toYRot());
            level.addFreshEntity(boat);
            itemStack.shrink(1);
            return itemStack;
        }

        @Override
        protected void playSound(BlockSource blockSource) {
            blockSource.getLevel().levelEvent(1000, blockSource.getPos(), 0);
        }
    }
}