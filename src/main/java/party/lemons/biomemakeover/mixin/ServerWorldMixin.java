package party.lemons.biomemakeover.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.entity.EntityPart;
import party.lemons.biomemakeover.entity.MultiPartEntity;
import party.lemons.biomemakeover.init.BMStructures;

@Mixin(ServerWorld.class)
public class ServerWorldMixin
{
	@Shadow private boolean inBlockTick;

	@Shadow @Final private Int2ObjectMap<Entity> entitiesById;

	@Shadow private boolean inEntityTick;

	@ModifyVariable(method = "locateStructure", at = @At("HEAD"))
	private StructureFeature<?> changeMansion(StructureFeature<?> feature)
	{
		if(feature == StructureFeature.MANSION)
			return BMStructures.MANSION;

		return feature;
	}

	@Inject(at = @At("HEAD"), method = "unloadEntity")
	private void onUnloadEntity(Entity e, CallbackInfo cbi)
	{
		if(e instanceof MultiPartEntity)
			MultiPartEntity.unload(e);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;loadEntity(Lnet/minecraft/entity/Entity;)V"), method = "loadEntityUnchecked")
	private void onLoadEntityUnchecked(Entity e, CallbackInfo cbi)
	{
		if(e instanceof MultiPartEntity)
			MultiPartEntity.loadUnchecked(e, entitiesById);
	}

	@Inject(at = @At("HEAD"), method = "tickEntity", cancellable = true)
	private void onTickEntity(Entity e, CallbackInfo cbi)
	{
		if(e instanceof EntityPart)
		{
			cbi.cancel();
		}
	}
}
