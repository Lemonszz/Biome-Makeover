package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallStandingBlockItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.block.TapestryBlock;
import party.lemons.biomemakeover.block.blockentity.TapestryBlockEntity;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltInModelItemRendererMixin
{
	private static TapestryBlockEntity TAPESTRY = new TapestryBlockEntity();

	@Inject(at = @At("RETURN"), method = "render", cancellable = true)
	public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo cbi)
	{
		if(stack.getItem() instanceof WallStandingBlockItem)
		{
			WallStandingBlockItem standingBlockItem = (WallStandingBlockItem) stack.getItem();
			if(standingBlockItem.getBlock() instanceof TapestryBlock)
			{
				TAPESTRY.setNonWorldColor(((TapestryBlock)standingBlockItem.getBlock()).color);
				BlockEntityRenderDispatcher.INSTANCE.renderEntity(TAPESTRY, matrices, vertexConsumers, light, overlay);
				cbi.cancel();
			}
		}
	}
}
