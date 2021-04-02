package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMEntities;

public class BlackThistleBlock extends BMTallFlowerBlock
{
	public BlackThistleBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
	{
		if (entity instanceof LivingEntity && entity.getType() != BMEntities.ROOTLING && entity.getType() != BMEntities.OWL && entity.getType() != EntityType.BEE) {
			if (!world.isClient && state.get(HALF) == DoubleBlockHalf.UPPER) {
				((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 110, 0));
			}
		}
	}
}
