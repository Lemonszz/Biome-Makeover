package party.lemons.biomemakeover.mixin.multipart;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.entity.mutipart.EntityPart;
import party.lemons.biomemakeover.entity.mutipart.MultiPartEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Mixin(Level.class)
public class LevelMixin {

    @Inject(at = @At("RETURN"), method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", locals = LocalCapture.CAPTURE_FAILSOFT)
    private void getEntities(@Nullable Entity entity, AABB aABB, Predicate<? super Entity> predicate, CallbackInfoReturnable<List<Entity>> cbi, List<Entity> list)
    {
        List<Entity> toAdd = Lists.newArrayList();
        for(Entity e : list)
        {
            if(e instanceof MultiPartEntity<?> mpe)
                for(EntityPart<?> p : mpe.getParts())
                {
                    if(entity == p || !predicate.test(p))
                        continue;
                    toAdd.add(p);
                }
        }
        list.addAll(toAdd);
    }

    @Inject(at = @At("RETURN"), method = "getEntities(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", locals = LocalCapture.CAPTURE_FAILSOFT)
    private <T extends Entity> void getEntitiesTyped(EntityTypeTest<Entity, T> entityTypeTest, AABB aABB, Predicate<? super T> predicate, CallbackInfoReturnable<List<T>> cbi, List<T> list)
    {
        List<T> toAdd = Lists.newArrayList();
        for(Entity e : list)
        {
            if(e instanceof MultiPartEntity<?> mpe)
                for(EntityPart<?> p : mpe.getParts())
                {
                    T asEntity = entityTypeTest.tryCast(p);

                    if(asEntity == null || !predicate.test(asEntity))
                        continue;
                    toAdd.add(asEntity);
                }
        }
        list.addAll(toAdd);
    }
}
