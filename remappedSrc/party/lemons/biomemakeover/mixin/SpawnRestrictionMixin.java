package party.lemons.biomemakeover.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.init.BMEntities;

import java.util.Map;

@Mixin(SpawnRestriction.class)
public class SpawnRestrictionMixin
{
	@Shadow @Final private static Map<EntityType<?>, SpawnRestriction.Entry> RESTRICTIONS;

	@Inject(at = @At("TAIL"), method = "<clinit>")
	private static void onInit(CallbackInfo cbi)
	{
		BMEntities.registerSpawnRestrictions(RESTRICTIONS);
	}
}
