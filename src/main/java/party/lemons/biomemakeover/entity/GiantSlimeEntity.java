package party.lemons.biomemakeover.entity;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeKeys;
import party.lemons.biomemakeover.init.BMEntities;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class GiantSlimeEntity extends SlimeEntity
{
	private AttributeContainer attributeContainer;

	public GiantSlimeEntity(World world)
	{
		super(BMEntities.GIANT_SLIME, world);
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag)
	{
		EntityData superData = super.initialize(world, difficulty, spawnReason, entityData, entityTag);

		setSize(8, true);
		this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(64);
		return superData;
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer =  new AttributeContainer(
					HostileEntity.createHostileAttributes().build());
		return attributeContainer;
	}

	public static boolean canSpawnGiantSlime(EntityType<GiantSlimeEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random)
	{
		if (world.getDifficulty() != Difficulty.PEACEFUL && world.getMoonSize() == 1F) {
			if (Objects.equals(world.method_31081(pos), Optional.of(BiomeKeys.SWAMP)) && pos.getY() > 50 && pos.getY() < 70 && random.nextFloat() < 0.8F && world.getLightLevel(pos) <= random.nextInt(8)) {
				return canMobSpawn(type, world, spawnReason, pos, random);
			}

			if (!(world instanceof StructureWorldAccess)) {
				return false;
			}
		}

		return false;
	}

}
