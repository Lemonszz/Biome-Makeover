package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;
import party.lemons.biomemakeover.init.BMEffects;

public class FangAttackingPhase extends AttackingPhase
{
    public FangAttackingPhase(ResourceLocation id, AdjudicatorEntity adjudicator)
    {
        super(id, adjudicator);
    }

    @Override
    public void onEnterPhase()
    {
        super.onEnterPhase();
        adjudicator.setState(AdjudicatorState.SUMMONING);
        adjudicator.playSound(BMEffects.ADJUDICATOR_SPELL_1, 1F, 1F);
    }

    @Override
    protected Goal getAttackGoal()
    {
        return new AdjudicatorFangGoal(adjudicator);
    }

    static class AdjudicatorFangGoal extends Goal
    {
        protected int spellCooldown;
        protected int startTime;
        private AdjudicatorEntity adjudicator;

        public AdjudicatorFangGoal(AdjudicatorEntity adjudicator)
        {
            this.adjudicator = adjudicator;
        }

        public boolean canUse() {
            LivingEntity livingEntity = adjudicator.getTarget();
            return livingEntity != null && livingEntity.isAlive();
        }

        public boolean canContinueToUse() {
            LivingEntity livingEntity = adjudicator.getTarget();
            return livingEntity != null && livingEntity.isAlive() && this.spellCooldown > 0;
        }

        public void start() {
            this.spellCooldown = 20;
            this.startTime = adjudicator.tickCount + this.startTimeDelay();
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
            float f = (float)Mth.atan2(target.getZ() - adjudicator.getZ(), target.getX() - adjudicator.getX());
            int j;
            if (adjudicator.distanceTo(target) < 24.0D) {
                float h;
                for(j = 0; j < 5; ++j) {
                    h = (float) (f + (float)j * Math.PI * 0.4F);
                    this.conjureFangs(adjudicator.getX() + (double)Mth.cos(h) * 1.5D, adjudicator.getZ() + (double)Mth.sin(h) * 1.5D, minY, maxY, h, 0);
                }

                for(j = 0; j < 8; ++j) {
                    h = (float) (f + (float)j * Math.PI * 2.0F / 8.0F + 1.2566371F);
                    this.conjureFangs(adjudicator.getX() + (double)Mth.cos(h) * 2.5D, adjudicator.getZ() + (double)Mth.sin(h) * 2.5D, minY, maxY, h, 3);
                }
            } else {
                for(j = 0; j < 16; ++j) {
                    double l = 1.25D * (double)(j + 1);
                    int m = 1 * j;
                    this.conjureFangs(adjudicator.getX() + (double)Mth.cos(f) * l, adjudicator.getZ() + (double)Mth.sin(f) * l, minY, maxY, f, m);
                }
            }

        }

        private void conjureFangs(double x, double z, double maxY, double y, float yaw, int warmup) {
            BlockPos blockPos = new BlockPos(x, y, z);
            boolean bl = false;
            double d = 0.0D;

            do {
                BlockPos blockPos2 = blockPos.below();
                BlockState blockState = adjudicator.level.getBlockState(blockPos2);
                if (blockState.isFaceSturdy(adjudicator.level, blockPos2, Direction.UP)) {
                    if (!adjudicator.level.isEmptyBlock(blockPos)) {
                        BlockState blockState2 = adjudicator.level.getBlockState(blockPos);
                        VoxelShape voxelShape = blockState2.getCollisionShape(adjudicator.level, blockPos);
                        if (!voxelShape.isEmpty()) {
                            d = voxelShape.max(Direction.Axis.Y);
                        }
                    }

                    bl = true;
                    break;
                }

                blockPos = blockPos.below();
            } while(blockPos.getY() >= Mth.floor(maxY) - 1);

            if (bl) {
                adjudicator.level.addFreshEntity(new EvokerFangs(adjudicator.level, x, (double)blockPos.getY() + d, z, yaw, warmup, adjudicator));
            }

        }
    }
}