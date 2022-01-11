package party.lemons.biomemakeover.mixin.soulembers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.util.access.BrewingStandFuelAccess;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin extends BlockEntity implements BrewingStandFuelAccess {
    public BrewingStandBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Shadow
    private int fuel;

    @Override
    public int getFuel() {
        return fuel;
    }

    @Override
    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    @Inject(at = @At("HEAD"), method = "serverTick")
    private static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BrewingStandBlockEntity brew, CallbackInfo cbi) {
        ItemStack itemStack = brew.getItem(4);

        BrewingStandFuelAccess access = ((BrewingStandFuelAccess) brew);

        if(access.getFuel() <= 0 && itemStack.getItem() == BMItems.SOUL_EMBERS)
        {
            access.setFuel(20);
            itemStack.shrink(1);
            BrewingStandBlockEntity.setChanged(level, blockPos, blockState);
        }
    }
}
