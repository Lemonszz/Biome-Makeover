package party.lemons.biomemakeover.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.extension.HorseHat;

import java.util.Random;

@Mixin(PatrolSpawner.class)
public abstract class PatrolSpawnerMixin_Cowboy {

    @Inject(at = @At("HEAD"), method = "spawnPatrolMember", cancellable = true)
    private void spawnPatrolMember(ServerLevel level, BlockPos pos, Random random, boolean isLeader, CallbackInfoReturnable<Boolean> cbi)
    {
        Holder<Biome> biome = level.getBiome(pos);
        if(biome.is(BiomeTags.IS_BADLANDS))
        {
            BlockState blockState = level.getBlockState(pos);
            if (!NaturalSpawner.isValidEmptySpawnBlock(level, pos, blockState, blockState.getFluidState(), BMEntities.COWBOY.get())) {
                cbi.setReturnValue(false);
            }
            if (!PatrollingMonster.checkPatrollingMonsterSpawnRules(EntityType.PILLAGER, level, MobSpawnType.PATROL, pos, random)) {
                cbi.setReturnValue(false);
            }
            PatrollingMonster patrollingMonster = BMEntities.COWBOY.get().create(level);
            if (patrollingMonster != null)
            {
                Horse horse = EntityType.HORSE.create(level);
                horse.setPos(pos.getX(), pos.getY(), pos.getZ());
                horse.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.PATROL, null, null);
                patrollingMonster.startRiding(horse);
                ((HorseHat)horse).setCowboySpawned();

                if (isLeader) {
                    patrollingMonster.setPatrolLeader(true);
                    patrollingMonster.findPatrolTarget();

                    ((HorseHat)horse).setHat();
                }
                patrollingMonster.setPos(pos.getX(), pos.getY(), pos.getZ());
                patrollingMonster.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.PATROL, null, null);
                level.addFreshEntityWithPassengers(horse);
                cbi.setReturnValue(true);
            }
            cbi.setReturnValue(false);
        }
    }
}