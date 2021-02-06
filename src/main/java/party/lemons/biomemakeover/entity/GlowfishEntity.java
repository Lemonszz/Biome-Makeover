package party.lemons.biomemakeover.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;

import java.util.Optional;
import java.util.Random;

public class GlowfishEntity extends SalmonEntity
{
	public AttributeContainer attributeContainer;

	public GlowfishEntity(World world)
	{
		super(BMEntities.GLOWFISH, world);
	}

	protected ItemStack getFishBucketItem()
	{
		return new ItemStack(BMItems.GLOWFISH_BUCKET);
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer = new AttributeContainer(SalmonEntity.createFishAttributes().build());
		return attributeContainer;
	}

	public static boolean canSpawn(EntityType<DrownedEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random)
	{
		Optional<RegistryKey<Biome>> optional = world.method_31081(pos);
		return world.getFluidState(pos).isIn(FluidTags.WATER);
	}


	public boolean canSpawn(WorldView world)
	{
		return world.intersectsEntities(this);
	}
}
