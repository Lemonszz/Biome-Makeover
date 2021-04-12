package party.lemons.biomemakeover.mixin;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.entity.StoneGolemEntity;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.access.CarvedPumpkinBlockAccess;
import party.lemons.biomemakeover.world.BMWorldEvents;

import java.util.Iterator;
import java.util.function.Predicate;

@Mixin(CarvedPumpkinBlock.class)
public class CarvedPumpkinBlockMixin implements CarvedPumpkinBlockAccess
{
	@Shadow @Final private static Predicate<BlockState> IS_GOLEM_HEAD_PREDICATE;

	@Inject(at = @At("HEAD"), cancellable = true, method = "canDispense")
	private void canDispense(WorldView worldView, BlockPos pos, CallbackInfoReturnable<Boolean> cbi)
	{
		if(BMWorldEvents.getStoneGolemDispenserPattern().searchAround(worldView, pos) != null)
			cbi.setReturnValue(true);
	}

	//TODO: make more general system for golems to allow this to be easier to add later?

	@Inject(at = @At("HEAD"), method = "trySpawnEntity", cancellable = true)
	private void trySpawnEntity(World world, BlockPos pos, CallbackInfo cbi)
	{
		BlockPattern pattern = BMWorldEvents.getStoneGolemPattern();
		BlockPattern.Result result = pattern.searchAround(world, pos);
		if (result != null) {
			for(int x = 0; x < pattern.getWidth(); ++x) {
				for(int y = 0; y < pattern.getHeight(); ++y) {
					CachedBlockPosition golPos = result.translate(x, y, 0);
					world.setBlockState(golPos.getBlockPos(), Blocks.AIR.getDefaultState(), 2);
					world.syncWorldEvent(2001, golPos.getBlockPos(), Block.getRawIdFromState(golPos.getBlockState()));
				}
			}

			BlockPos spawnPos = result.translate(1, 2, 0).getBlockPos();
			StoneGolemEntity stoneGolem = BMEntities.STONE_GOLEM.create(world);
			stoneGolem.setPlayerCreated(true);
			stoneGolem.refreshPositionAndAngles((double)spawnPos.getX() + 0.5D, (double)spawnPos.getY() + 0.05D, (double)spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
			world.spawnEntity(stoneGolem);
			Iterator<ServerPlayerEntity> playersNearby = world.getNonSpectatingEntities(ServerPlayerEntity.class, stoneGolem.getBoundingBox().expand(5.0D)).iterator();

			while(playersNearby.hasNext()) {
				ServerPlayerEntity pl = playersNearby.next();
				Criteria.SUMMONED_ENTITY.trigger(pl, stoneGolem);
			}

			for(int x = 0; x < pattern.getWidth(); ++x) {
				for(int y = 0; y < pattern.getHeight(); ++y) {
					CachedBlockPosition golemPos = result.translate(x, y, 0);
					world.updateNeighbors(golemPos.getBlockPos(), Blocks.AIR);
				}
			}
		}
	}

	@Override
	public Predicate<BlockState> bm_isGolemHeadBlock()
	{
		return IS_GOLEM_HEAD_PREDICATE;
	}
}
