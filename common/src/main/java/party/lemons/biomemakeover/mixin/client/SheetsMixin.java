package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMSigns;

import java.util.Map;
import java.util.function.Consumer;

@Mixin(Sheets.class)
public class SheetsMixin
{
	@Shadow
	@Final
	public static Map<WoodType, Material> SIGN_MATERIALS;
	@Shadow @Final public static ResourceLocation SIGN_SHEET = new ResourceLocation("textures/atlas/signs.png");

	@Inject(at = @At("HEAD"), method = "getAllMaterials")
	private static void getAllMaterials(Consumer<Material> consumer, CallbackInfo cbi)
	{
		BMSigns.getTypes().forEach(t->{
			if(!SIGN_MATERIALS.containsKey(t))	//In case wood types don't get registered due to mod shenanigans.
				SIGN_MATERIALS.put(t, new Material(SIGN_SHEET, new ResourceLocation("entity/signs/" + t.name())));
		});
	}

	@Inject(at = @At("HEAD"), method = "getSignMaterial")
	private static void getSignMaterial(WoodType woodType, CallbackInfoReturnable<Material> cbi)
	{
		if(!SIGN_MATERIALS.containsKey(woodType))	//In case wood types don't get registered due to mod shenanigans.
			SIGN_MATERIALS.put(woodType, new Material(SIGN_SHEET, new ResourceLocation("entity/signs/" + woodType.name())));
	}
}
