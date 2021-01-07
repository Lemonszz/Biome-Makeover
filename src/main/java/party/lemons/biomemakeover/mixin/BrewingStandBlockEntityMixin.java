package party.lemons.biomemakeover.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMItems;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin extends BlockEntity
{
	@Shadow private DefaultedList<ItemStack> inventory;

	@Shadow private int fuel;

	public BrewingStandBlockEntityMixin(BlockEntityType<?> type)
	{
		super(type);
		//NOFU
	}

	@Inject(at = @At("HEAD"), method = "tick")
	public void tick(CallbackInfo cbi)
	{
		ItemStack itemStack = this.inventory.get(4);
		if (this.fuel <= 0 && itemStack.getItem() == BMItems.SOUL_EMBERS) {
			this.fuel = 20;
			itemStack.decrement(1);
			this.markDirty();
		}
	}

	@Inject(at = @At("HEAD"), method = "isValid(ILnet/minecraft/item/ItemStack;)Z", cancellable = true)
	public void isValid(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cbi)
	{
		if(slot == 4 && !stack.isEmpty() && stack.getItem() == BMItems.SOUL_EMBERS)
		{
			cbi.setReturnValue(true);
		}
	}
}
