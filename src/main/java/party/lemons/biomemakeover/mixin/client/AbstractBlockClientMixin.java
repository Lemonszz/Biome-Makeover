package party.lemons.biomemakeover.mixin.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.util.BMBlockSettings;
import party.lemons.biomemakeover.util.RLayer;

@Mixin(AbstractBlock.class)
public class AbstractBlockClientMixin
{
	@Inject(at = @At("TAIL"), method = "<init>")
	public void onConstruct(AbstractBlock.Settings settings, CallbackInfo cbi)
	{
		if(settings instanceof BMBlockSettings)
		{
			RLayer layer = ((BMBlockSettings)settings).getLayer();
			if(layer != null)
				BlockRenderLayerMap.INSTANCE.putBlock(((Block)(Object)this), layer.getAsRenderLayer());
		}
	}
}
