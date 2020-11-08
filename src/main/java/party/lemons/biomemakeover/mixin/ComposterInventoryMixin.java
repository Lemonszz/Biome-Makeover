package party.lemons.biomemakeover.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;

@Mixin(ComposterBlock.ComposterInventory.class)
public abstract class ComposterInventoryMixin extends SimpleInventory
{
	@Shadow private boolean dirty;
	@Shadow @Final private BlockState state;
	@Shadow @Final private WorldAccess world;
	@Shadow @Final private BlockPos pos;

	@Inject(at = @At("RETURN"), method = "canInsert", cancellable = true)
	void canInsert(int slot, ItemStack stack, Direction dir, CallbackInfoReturnable<Boolean> cbi)
	{
		cbi.setReturnValue(!this.dirty && dir == Direction.UP &&
				(ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.containsKey(stack.getItem()) || (state.getBlock() == Blocks.COMPOSTER && state.get(ComposterBlock.LEVEL) > 0)));
	}

	@Inject(at = @At("HEAD"), method = "markDirty")
	void markDirty(CallbackInfo cbi)
	{
		ItemStack stack = getStack(0);
		if(!stack.isEmpty() && stack.getItem() == BMItems.ECTOPLASM)
		{
			this.dirty = true;
			world.syncWorldEvent(1500, this.pos, 1);
			this.removeStack(0);

			world.setBlockState(pos, BMBlocks.ECTOPLASM_COMPOSTER.getDefaultState().with(ComposterBlock.LEVEL, state.get(ComposterBlock.LEVEL)), 3);
		}
	}
}
