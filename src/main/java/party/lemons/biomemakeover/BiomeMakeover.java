package party.lemons.biomemakeover;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.PillagerSpawner;
import party.lemons.biomemakeover.crafting.itemgroup.BiomeMakeoverItemGroup;
import party.lemons.biomemakeover.crafting.witch.WitchQuestHandler;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.util.boat.BoatTypes;
import party.lemons.biomemakeover.world.PoltergeistHandler;
import party.lemons.biomemakeover.util.access.PillagerSpawnerAccess;
import party.lemons.biomemakeover.world.TumbleweedSpawner;
import party.lemons.biomemakeover.world.WindSystem;

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
		BMStructures.init();
		BMWorldGen.init();
		BMCriterion.init();

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
	}

	public static Identifier ID(String path)
	{
		return new Identifier(MODID, path);
	}
}
