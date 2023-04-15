package party.lemons.biomemakeover.mixin.additional_loot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.spongepowered.asm.mixin.Mixin;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.util.extension.LootBlocker;

@Mixin(Pillager.class)
public abstract class PillagerMixin extends AbstractIllager
{
	private static final ResourceLocation ADDITIONAL_LOOT_LEADER = BiomeMakeover.ID("entities/pillager_leader_additional");
	private static final ResourceLocation ADDITIONAL_LOOT = BiomeMakeover.ID("entities/pillager_additional");


	@Override
	protected void dropFromLootTable(DamageSource damageSource, boolean causedByPlayer)
	{
		super.dropFromLootTable(damageSource, causedByPlayer);

		if(!causedByPlayer || LootBlocker.isBlocked(this))
			return;

		//If is leader and not in raid, use leader table, otherwise use regular table
		ResourceLocation tableLocation = (isPatrolLeader() && !hasActiveRaid()) ? ADDITIONAL_LOOT_LEADER : ADDITIONAL_LOOT;

		LootTable lootTable = level.getServer().getLootTables().get(tableLocation);
		LootContext.Builder builder = createLootContext(true, damageSource);
		lootTable.getRandomItems(builder.create(LootContextParamSets.ENTITY), this::spawnAtLocation);
	}

	private PillagerMixin(EntityType<? extends AbstractIllager> entityType, Level level)
	{
		super(entityType, level);
	}
}
