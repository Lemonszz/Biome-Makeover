package party.lemons.biomemakeover.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.block.BarrelCactusBlock;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;

@Mixin(FlowerPotBlock.class)
public class FlowerPotBlockMixin
{
	@Shadow @Final private Block content;

	@Inject(at = @At(value = "HEAD"), method = "onUse", cancellable = true)
	private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cbi)
	{
		ItemStack held = player.getStackInHand(hand);
		boolean isAir = this.content == Blocks.AIR;
		if(isAir)
		{
			if(!held.isEmpty() && held.getItem() == BMBlocks.BARREL_CACTUS.asItem())
			{
				world.setBlockState(pos, BMBlocks.POTTED_BARREL_CACTUS.getDefaultState(), 2);
				player.incrementStat(Stats.POT_FLOWER);
				if (!player.abilities.creativeMode) {
					held.decrement(1);
				}

				cbi.setReturnValue(ActionResult.SUCCESS);
			}
		}
		else
		{
			Item it = null;
			if((Object)this == BMBlocks.POTTED_BARREL_CACTUS)
				it = BMBlocks.BARREL_CACTUS.asItem();
			else if((Object)this == BMBlocks.POTTED_FLOWERED_BARREL_CACTUS)
				it = BMItems.BARREL_CACTUS_FLOWERED;

			if(it != null)
			{
				world.setBlockState(pos, Blocks.FLOWER_POT.getDefaultState(), 2);

				ItemStack st = new ItemStack(it);
				if (held.isEmpty()) {
					player.setStackInHand(hand, st);
				} else if (!player.giveItemStack(st)) {
					player.dropItem(st, false);
				}

				cbi.setReturnValue(ActionResult.SUCCESS);
			}
		}
	}
}
