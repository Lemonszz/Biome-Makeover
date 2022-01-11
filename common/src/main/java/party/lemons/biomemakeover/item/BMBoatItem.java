package party.lemons.biomemakeover.item;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import party.lemons.biomemakeover.entity.BMBoatEntity;
import party.lemons.biomemakeover.util.registry.boat.BoatType;

import java.util.List;
import java.util.function.Supplier;

public class BMBoatItem extends BMItem
{
    private final Supplier<BoatType> type;

    public BMBoatItem(Supplier<BoatType> boatType, Properties settings)
    {
        super(settings);
        this.type = boatType;
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
        BMBoatEntity boatEntity = new BMBoatEntity(world, x, y, z);
        boatEntity.setBoatType(type.get());
        boatEntity.setYRot(yaw);
        return boatEntity;
    }
}