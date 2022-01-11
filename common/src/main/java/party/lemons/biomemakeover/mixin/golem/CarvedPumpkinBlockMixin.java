package party.lemons.biomemakeover.mixin.golem;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.level.golem.GolemHandler;
import party.lemons.biomemakeover.util.access.CarvedPumpkinAccess;

import java.util.function.Predicate;

@Mixin(CarvedPumpkinBlock.class)
public class CarvedPumpkinBlockMixin implements CarvedPumpkinAccess {

    @Inject(at = @At("HEAD"), method = "canSpawnGolem", cancellable = true)
    public void canSpawnGolem(LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cbi) {
        if (GolemHandler.canDispenseGolem(levelReader, blockPos))
            cbi.setReturnValue(true);
    }

    @Inject(at = @At("HEAD"), method = "trySpawnGolem", cancellable = true)
    private void trySpawnGolem(Level level, BlockPos blockPos, CallbackInfo cbi) {
        if(GolemHandler.checkAndCreateGolem(level, blockPos))
            cbi.cancel();
    }

    @Shadow @Final private static Predicate<BlockState> PUMPKINS_PREDICATE;

    @Override
    public Predicate<BlockState> bm_isGolemHeadBlock() {
        return PUMPKINS_PREDICATE;
    }
}
