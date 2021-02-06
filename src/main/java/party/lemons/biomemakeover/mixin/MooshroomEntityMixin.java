package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MooshroomEntity.class)
public abstract class MooshroomEntityMixin
{
	@Inject(at = @At("TAIL"), method = "<init>")
	public void onConstruct(EntityType<? extends MooshroomEntity> entityType, World world, CallbackInfo cbi)
	{
		if(getMooshroomType() != MooshroomEntity.Type.BROWN)
			if(world.random.nextBoolean()) setType(MooshroomEntity.Type.BROWN);

	}

	@Shadow
	public abstract MooshroomEntity.Type getMooshroomType();

	@Shadow
	public abstract void setType(MooshroomEntity.Type type);
}
