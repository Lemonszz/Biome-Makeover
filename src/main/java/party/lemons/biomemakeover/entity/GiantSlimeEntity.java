package party.lemons.biomemakeover.entity;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeKeys;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.mixin.SlimeEntityMixin;
import party.lemons.biomemakeover.util.access.SlimeEntityAccess;

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

	public void remove() {
		int size = this.getSize();
		if (!this.world.isClient && size > 1 && this.isDead()) {
			Text text = this.getCustomName();
			boolean hasDisabledAI = this.isAiDisabled();

			DecayedEntity decayed = BMEntities.DECAYED.create(this.world);
			if (this.isPersistent())
			{
				decayed.setPersistent();
			}

			decayed.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.AIR));
			decayed.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.AIR));
			decayed.setCustomName(text);
			decayed.setAiDisabled(hasDisabledAI);
			decayed.setInvulnerable(this.isInvulnerable());
			decayed.refreshPositionAndAngles(this.getX(), this.getY() + 0.5D, this.getZ(), yaw, pitch);
			this.world.spawnEntity(decayed);
		}

		if (!this.world.isClient && size > 1 && this.isDead())
		{
			Text text = this.getCustomName();
			boolean bl = this.isAiDisabled();
			float quarterSize = (float)size / 4.0F;
			int halfSize = size / 2;
			int count = 2 + this.random.nextInt(3);

			for(int in = 0; in < count; ++in) {
				float offsetX = ((float)(in % 2) - 0.5F) * quarterSize;
				float offsetY = ((float)(in / 2) - 0.5F) * quarterSize;
				SlimeEntity slimeEntity = EntityType.SLIME.create(this.world);
				if (this.isPersistent()) {
					slimeEntity.setPersistent();
				}

				slimeEntity.setCustomName(text);
				slimeEntity.setAiDisabled(bl);
				slimeEntity.setInvulnerable(this.isInvulnerable());
				((SlimeEntityAccess)slimeEntity).setSlimeSize(halfSize, true);
				slimeEntity.refreshPositionAndAngles(this.getX() + (double)offsetX, this.getY() + 0.5D, this.getZ() + (double)offsetY, this.random.nextFloat() * 360.0F, 0.0F);
				this.world.spawnEntity(slimeEntity);
			}
		}
		this.removed = true;
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
