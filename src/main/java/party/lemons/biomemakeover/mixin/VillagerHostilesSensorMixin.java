package party.lemons.biomemakeover.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.sensor.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.init.BMEntities;

import java.util.HashMap;
import java.util.Map;

@Mixin(VillagerHostilesSensor.class)
public class VillagerHostilesSensorMixin
{
	@Shadow @Final @Mutable private static ImmutableMap<EntityType<?>, Float> SQUARED_DISTANCES_FOR_DANGER;

	@Inject(at = @At("TAIL"), method = "<clinit>")
	private static void onInit(CallbackInfo cbi)
	{
		Map<EntityType<?>, Float> copy = new HashMap<>(SQUARED_DISTANCES_FOR_DANGER);
		copy.put(BMEntities.COWBOY, 12F);
		copy.put(BMEntities.DECAYED, 5F);

		SQUARED_DISTANCES_FOR_DANGER = ImmutableMap.copyOf(copy);
	}
}
