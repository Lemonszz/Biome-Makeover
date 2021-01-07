package party.lemons.biomemakeover.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.world.BMWorldEvents;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin
{
	@Inject(at = @At("HEAD"), method = "useOnGround", cancellable = true)
	private static void useOnGround(ItemStack stack, World world, BlockPos pos, Direction facing, CallbackInfoReturnable<Boolean> cbi)
	{
		if(world.getBiome(pos).getCategory() == Biome.Category.SWAMP && world.getBlockState(pos).isOf(Blocks.WATER) && world.getFluidState(pos).getLevel() == 8)
		{
			if(!(world instanceof ServerWorld))
			{
				cbi.setReturnValue(true);
				return;
			}
			BMWorldEvents.handleSwampBoneMeal(world, pos, world.random);
			cbi.setReturnValue(true);
		}
	}
}
