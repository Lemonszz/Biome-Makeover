package party.lemons.biomemakeover;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.data.server.BarterLootTableGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.PillagerSpawner;
import party.lemons.biomemakeover.crafting.itemgroup.BiomeMakeoverItemGroup;
import party.lemons.biomemakeover.crafting.witch.WitchQuestHandler;
import party.lemons.biomemakeover.entity.LightningBugEntity;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.util.boat.BoatTypes;
import party.lemons.biomemakeover.world.PoltergeistHandler;
import party.lemons.biomemakeover.util.access.PillagerSpawnerAccess;
import party.lemons.biomemakeover.world.TumbleweedSpawner;
import party.lemons.biomemakeover.world.WindSystem;

import java.util.List;

public class BiomeMakeover implements ModInitializer
{
	public static final String MODID = "biomemakeover";
	public static ItemGroup GROUP;

	@Override
	public void onInitialize()
	{
		GROUP = new BiomeMakeoverItemGroup(new Identifier(MODID, MODID));


		BMEffects.init();
		BoatTypes.init();
		BMBlocks.init();
		BMItems.init();
		BMEntities.init();
		BMBlockEntities.init();
		BMPotions.init();
		BMEnchantments.init();
		BMStructures.init();
		BMWorldGen.init();
		BMCriterion.init();
		BMScreens.init();

		BMNetwork.initCommon();
		PoltergeistHandler.init();
		WitchQuestHandler.init();

		ServerTickEvents.END_SERVER_TICK.register((e)->WindSystem.update());
		ServerTickEvents.END_WORLD_TICK.register(TumbleweedSpawner::update);

		//TOOD: Move
		CommandRegistrationCallback.EVENT.register((d, ded)->
				d.register(CommandManager.literal("pillager").requires((serverCommandSource) ->serverCommandSource.hasPermissionLevel(2))
				.then(CommandManager.argument("pos", BlockPosArgumentType.blockPos()).then(CommandManager.argument("leader", BoolArgumentType.bool()).executes(c->{
					((PillagerSpawnerAccess)new PillagerSpawner()).spawn(c.getSource().getWorld(), BlockPosArgumentType.getBlockPos(c, "pos"), BoolArgumentType.getBool(c, "leader"));
					return 1;
				})))));

		UseBlockCallback.EVENT.register((player, world, hand, hitResult)->{

			if(!player.isSpectator())
			{
				ItemStack stack = player.getStackInHand(hand);
				if(!stack.isEmpty() && stack.getItem() == BMItems.ECTOPLASM)
				{
					BlockPos pos = hitResult.getBlockPos();
					BlockState state = world.getBlockState(pos);
					if(state.getBlock() == Blocks.COMPOSTER)
					{
						int level = state.get(ComposterBlock.LEVEL);
						if(level > 0)
						{
							if(!world.isClient())
							{
								world.syncWorldEvent(1500, pos, 1);
								world.setBlockState(pos, BMBlocks.ECTOPLASM_COMPOSTER.getDefaultState().with(ComposterBlock.LEVEL, level));

								if(!player.isCreative())
									stack.decrement(1);
							}
							return ActionResult.SUCCESS;
						}
					}
				}
			}

			return ActionResult.PASS;
		});

		UseEntityCallback.EVENT.register((pl, world, hand, entity, hr)->{
			ItemStack stack = pl.getStackInHand(hand);

			if(!stack.isEmpty() && stack.getItem() == Items.GLASS_BOTTLE)
			{
				if(entity instanceof LightningBugEntity)
				{
					if(!world.isClient())
					{
						ItemUsage.method_30012(stack, pl, new ItemStack(BMBlocks.LIGHTNING_BUG_BOTTLE));
						entity.remove();
					}
					return ActionResult.SUCCESS;
				}
			}
			return ActionResult.PASS;

		});
	}

	public static Identifier ID(String path)
	{
		return new Identifier(MODID, path);
	}
}
