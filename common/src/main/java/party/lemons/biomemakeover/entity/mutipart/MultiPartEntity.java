package party.lemons.biomemakeover.entity.mutipart;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import party.lemons.biomemakeover.util.extension.MultipartHolder;

import java.util.List;
import java.util.function.Predicate;

public interface MultiPartEntity<T extends EntityPart<?>>
{
    @Environment(value= EnvType.CLIENT)
    static void handleClientSpawn(ClientboundAddMobPacket packet, MultiPartEntity<?> entity)
    {
        List<? extends EntityPart<?>> parts = entity.getParts();

        for(int i = 0; i < parts.size(); i++)
        {
            parts.get(i).setId(i + packet.getId());
        }
    }

    static void removeParts(MultiPartEntity<?> e)
    {
        for(EntityPart part : e.getParts()) {
            part.remove(Entity.RemovalReason.DISCARDED);
            ((MultipartHolder)part.level).getPartMap().remove(part.getId(), part);
        }
    }

    static void loadParts(MultiPartEntity<?> e)
    {
        for(EntityPart part : e.getParts())
            ((MultipartHolder)part.level).getPartMap().put(part.getId(), part);
    }

    static void onCollectOtherEntities(Entity except, AABB box, Predicate<? super Entity> predicate, MultiPartEntity<?> e, List<Entity> entityList)
    {
        for(EntityPart part : e.getParts())
        {
            if (part != except && part.getBoundingBox().intersects(box) && (predicate == null || predicate.test(part))) {
                entityList.add(part);
            }
        }
    }

    boolean damagePart(T part, DamageSource source, float amount);
    List<T> getParts();
    default void updateParts()
    {
        getParts().forEach(p->{
            p.updatePartPosition();

            p.xOld = p.getX();
            p.yOld = p.getY();
            p.zOld = p.getZ();
        });
    }
}
