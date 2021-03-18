package party.lemons.biomemakeover.entity;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public interface MultiPartEntity<T extends EntityPart<?>>
{
	@Environment(EnvType.CLIENT)
	static void handleClientSpawn(MobSpawnS2CPacket packet, LivingEntity livingEntity)
	{
		List<EntityPart> parts = ((MultiPartEntity)livingEntity).getParts();

		for(int i = 0; i < parts.size(); i++)
		{
			parts.get(i).setEntityId(i + packet.getId());
		}
	}

	static void unload(Entity e)
	{
		for(EntityPart part : ((MultiPartEntity<?>) e).getParts())
			part.remove();
	}

	static void loadUnchecked(Entity e, Int2ObjectMap<Entity> entitiesById)
	{
		for(EntityPart part : ((MultiPartEntity<?>) e).getParts())
			entitiesById.put(part.getEntityId(), part);
	}

	static void onCollectOtherEntities(Entity except, Box box, Predicate<? super Entity> predicate, Entity e, List<Entity> entityList)
	{
		for(EntityPart part : ((MultiPartEntity<?>) e).getParts())
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

			p.lastRenderX = p.getX();
			p.lastRenderY = p.getY();
			p.lastRenderZ = p.getZ();
		});
	}
}
