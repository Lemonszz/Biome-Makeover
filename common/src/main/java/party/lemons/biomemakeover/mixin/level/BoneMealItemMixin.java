package party.lemons.biomemakeover.mixin.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.level.BMWorldEvents;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin
{
    @Inject(at = @At("HEAD"), method = "growWaterPlant", cancellable = true)
    private static void growCrop(ItemStack itemStack, Level level, BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cbi)
    {
        if(level.getBiome(pos).is(BMWorldGen.SWAMP_BIOMES) && level.getBlockState(pos).is(Blocks.WATER) && level.getFluidState(pos).isSource())
        {
            if(!(level instanceof ServerLevel))
            {
                cbi.setReturnValue(true);
                return;
            }
            BMWorldEvents.handleSwampBoneMeal(level, pos, level.random);
            cbi.setReturnValue(true);
        }
    }
}
