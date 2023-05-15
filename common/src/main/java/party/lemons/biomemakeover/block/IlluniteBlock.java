package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.taniwha.block.types.TBlock;

public class IlluniteBlock extends TBlock
{
	public IlluniteBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	public void onProjectileHit(Level level, BlockState blockState, BlockHitResult blockHitResult, Projectile projectile) {
		if (!level.isClientSide) {
			BlockPos blockPos = blockHitResult.getBlockPos();
			level.playSound(null, blockPos, BMEffects.ILLUNITE_HIT.get(), SoundSource.BLOCKS, 1.0F, 0.5F + level.random.nextFloat() * 1.2F);
			level.playSound(null, blockPos, BMEffects.ILLUNITE_PLACE.get(), SoundSource.BLOCKS, 1.0F, 0.5F + level.random.nextFloat() * 1.2F);
		}
	}
}
