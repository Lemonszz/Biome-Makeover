package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.collection.TypeFilterableList;
import net.minecraft.util.math.Box;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.entity.MultiPartEntity;

import java.util.List;
import java.util.function.Predicate;

@Mixin(WorldChunk.class)
public class WorldChunkMixin
{
	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;get(I)Ljava/lang/Object;"), method = "collectOtherEntities", locals = LocalCapture.CAPTURE_FAILSOFT)
	private void onCollectOtherEntities(Entity except, Box box, List<Entity> entityList, Predicate<? super Entity> predicate, CallbackInfo cbi, int i, int j, int k, TypeFilterableList typeFilterableList, List list, int l, int m)
	{
		Entity e = (Entity)list.get(m);
		if(e instanceof MultiPartEntity)
			MultiPartEntity.onCollectOtherEntities(except, box, predicate, e, entityList);
	}
}
