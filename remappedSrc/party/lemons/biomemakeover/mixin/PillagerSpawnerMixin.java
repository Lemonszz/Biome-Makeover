package party.lemons.biomemakeover.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.PillagerSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.HorseHat;
import party.lemons.biomemakeover.util.access.PillagerSpawnerAccess;

import java.util.Random;

@Mixin(PillagerSpawner.class)
public abstract class PillagerSpawnerMixin implements PillagerSpawnerAccess
{
	@Shadow protected abstract boolean spawnPillager(ServerWorld world, BlockPos pos, Random random, boolean captain);

	@Inject(at = @At("HEAD"), method = "spawnPillager", cancellable = true)
	private void spawnMixin(ServerWorld world, BlockPos pos, Random random, boolean captain, CallbackInfoReturnable<Boolean> cbi)
	{
		Biome biome = world.getBiome(pos);
		Biome.Category category = biome.getCategory();
		if(category == Biome.Category.MESA)
		{
			BlockState blockState = world.getBlockState(pos);
			if (!SpawnHelper.isClearForSpawn(world, pos, blockState, blockState.getFluidState(), BMEntities.COWBOY)) {
				cbi.setReturnValue(false);
				return;
			} else if (!PatrolEntity.canSpawn(BMEntities.COWBOY, world, SpawnReason.PATROL, pos, random))
			{
				cbi.setReturnValue(false);
				return;
			}
			else {
				PatrolEntity patrolEntity = BMEntities.COWBOY.create(world);
				if (patrolEntity != null)
				{
					HorseEntity horseEntity = EntityType.HORSE.create(world);
					horseEntity.updatePosition(pos.getX(), pos.getY(), pos.getZ());
					horseEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.PATROL, null, null);
					patrolEntity.startRiding(horseEntity);
					if (captain) {
						patrolEntity.setPatrolLeader(true);
						patrolEntity.setRandomPatrolTarget();

						((HorseHat)horseEntity).setHat();
					}

					patrolEntity.updatePosition(pos.getX(), pos.getY(), pos.getZ());
					patrolEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.PATROL, null, null);
					world.spawnEntityAndPassengers(horseEntity);

					cbi.setReturnValue(true);
					return;
				}
				else {
					cbi.setReturnValue(false);
					return;
				}
			}
		}
	}

	@Override
	public void spawn(ServerWorld world, BlockPos pos, boolean captain)
	{
		spawnPillager(world, pos, world.random, captain);
	}
}