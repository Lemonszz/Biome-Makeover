package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.entity.EntityPart;

@Mixin(ThreadedAnvilChunkStorage.class)
public class ThreadedAnvilChunkStorageMixin
{
	@Inject(at = @At("HEAD"), method = "loadEntity", cancellable = true)
	private void onLoadEntity(Entity e, CallbackInfo cbi)
	{
		if(e instanceof EntityPart)
			cbi.cancel();
	}
}
