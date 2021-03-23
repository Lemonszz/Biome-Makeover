package party.lemons.biomemakeover.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorRoomListener;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin
{
	//TOOD: move to place event what that's around.
	@Unique
	private BlockState tempBlockPlaceState;

	@Inject(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"), cancellable = true)
	private void beforeBlockPlace(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> info) {
		final Item item = stack.getItem();

		// TODO: Do we need to cover buckets placing fluids?
		if (item instanceof BlockItem) {
			ItemUsageContext usageContext = new ItemUsageContext(player, hand, blockHitResult);
			ItemPlacementContext placementContext = new ItemPlacementContext(usageContext);
			BlockState futureState = ((BlockItem) item).getBlock().getPlacementState(placementContext);

			this.tempBlockPlaceState = futureState;
		}
	}

	@Inject(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;", shift = At.Shift.AFTER))
	private void afterBlockPlace(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> info) {
		Item item = stack.getItem();

		if (item instanceof BlockItem && this.tempBlockPlaceState != null)
		{
			AdjudicatorRoomListener.onPlaceBlock(blockHitResult.getBlockPos());
		}

		this.tempBlockPlaceState = null;
	}
}
