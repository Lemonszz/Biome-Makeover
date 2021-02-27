package party.lemons.biomemakeover.entity.adjudicator.ai;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

public class AdjudicatorFangGoal extends Goal
{
	protected int spellCooldown;
	protected int startTime;
	private AdjudicatorEntity adjudicator;

	public AdjudicatorFangGoal(AdjudicatorEntity adjudicator)
	{
		this.adjudicator = adjudicator;
	}

	public boolean canStart() {
		LivingEntity livingEntity = adjudicator.getTarget();
		return livingEntity != null && livingEntity.isAlive();
	}

	public boolean shouldContinue() {
		LivingEntity livingEntity = adjudicator.getTarget();
		return livingEntity != null && livingEntity.isAlive() && this.spellCooldown > 0;
	}

	public void start() {
		this.spellCooldown = 20;
		this.startTime = adjudicator.age + this.startTimeDelay();
	}

	public void tick() {
		--this.spellCooldown;
		if (this.spellCooldown == 0) {
			this.castSpell();
			spellCooldown = startTimeDelay();
		}
	}

	protected int getSpellTicks() {
		return 20;
	}

	protected int startTimeDelay() {
		return 40;
	}

	protected void castSpell() {
		LivingEntity target = adjudicator.getTarget();
		double minY = Math.min(target.getY(), adjudicator.getY());
		double maxY = Math.max(target.getY(), adjudicator.getY()) + 1.0D;
		float f = (float)MathHelper.atan2(target.getZ() - adjudicator.getZ(), target.getX() - adjudicator.getX());
		int j;
		if (adjudicator.squaredDistanceTo(target) < 24.0D) {
			float h;
			for(j = 0; j < 5; ++j) {
				h = (float) (f + (float)j * Math.PI * 0.4F);
				this.conjureFangs(adjudicator.getX() + (double)MathHelper.cos(h) * 1.5D, adjudicator.getZ() + (double)MathHelper.sin(h) * 1.5D, minY, maxY, h, 0);
			}

			for(j = 0; j < 8; ++j) {
				h = (float) (f + (float)j * Math.PI * 2.0F / 8.0F + 1.2566371F);
				this.conjureFangs(adjudicator.getX() + (double)MathHelper.cos(h) * 2.5D, adjudicator.getZ() + (double)MathHelper.sin(h) * 2.5D, minY, maxY, h, 3);
			}
		} else {
			for(j = 0; j < 16; ++j) {
				double l = 1.25D * (double)(j + 1);
				int m = 1 * j;
				this.conjureFangs(adjudicator.getX() + (double)MathHelper.cos(f) * l, adjudicator.getZ() + (double)MathHelper.sin(f) * l, minY, maxY, f, m);
			}
		}

	}

	private void conjureFangs(double x, double z, double maxY, double y, float yaw, int warmup) {
		BlockPos blockPos = new BlockPos(x, y, z);
		boolean bl = false;
		double d = 0.0D;

		do {
			BlockPos blockPos2 = blockPos.down();
			BlockState blockState = adjudicator.world.getBlockState(blockPos2);
			if (blockState.isSideSolidFullSquare(adjudicator.world, blockPos2, Direction.UP)) {
				if (!adjudicator.world.isAir(blockPos)) {
					BlockState blockState2 = adjudicator.world.getBlockState(blockPos);
					VoxelShape voxelShape = blockState2.getCollisionShape(adjudicator.world, blockPos);
					if (!voxelShape.isEmpty()) {
						d = voxelShape.getMax(Direction.Axis.Y);
					}
				}

				bl = true;
				break;
			}

			blockPos = blockPos.down();
		} while(blockPos.getY() >= MathHelper.floor(maxY) - 1);

		if (bl) {
			adjudicator.world.spawnEntity(new EvokerFangsEntity(adjudicator.world, x, (double)blockPos.getY() + d, z, yaw, warmup, adjudicator));
		}

	}
}
