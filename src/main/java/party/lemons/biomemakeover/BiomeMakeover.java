package party.lemons.biomemakeover;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.loot.BinomialLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.PillagerSpawner;
import party.lemons.biomemakeover.crafting.itemgroup.BiomeMakeoverItemGroup;
import party.lemons.biomemakeover.crafting.witch.WitchQuestHandler;
import party.lemons.biomemakeover.entity.LightningBugEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorRoomListener;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.util.NetworkUtil;
import party.lemons.biomemakeover.util.extensions.SlideEntity;
import party.lemons.biomemakeover.util.access.PillagerSpawnerAccess;
import party.lemons.biomemakeover.util.boat.BoatTypes;
import party.lemons.biomemakeover.world.BMWorldEvents;
import party.lemons.biomemakeover.world.PoltergeistHandler;
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
		BMEnchantments.init();
		BMStructures.init();
		BMWorldGen.init();
		BMCriterion.init();
		BMScreens.init();
		BMWorldEvents.init();

		BMNetwork.initCommon();
		PoltergeistHandler.init();
		WitchQuestHandler.init();
		AdjudicatorRoomListener.init();

		ServerTickEvents.END_SERVER_TICK.register((e)->WindSystem.update());
		ServerTickEvents.END_WORLD_TICK.register(TumbleweedSpawner::update);

		//TODO: Move
		CommandRegistrationCallback.EVENT.register((d, ded)->d.register(CommandManager.literal("pillager").requires((serverCommandSource)->serverCommandSource.hasPermissionLevel(2)).then(CommandManager.argument("pos", BlockPosArgumentType.blockPos()).then(CommandManager.argument("leader", BoolArgumentType.bool()).executes(c->
		{
			((PillagerSpawnerAccess) new PillagerSpawner()).spawn(c.getSource().getWorld(), BlockPosArgumentType.getBlockPos(c, "pos"), BoolArgumentType.getBool(c, "leader"));
			return 1;
		})))));

		ServerPlayConnectionEvents.JOIN.register((ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server)->
		{
			NetworkUtil.sendSlideTime(handler.player, ((SlideEntity) handler.player).getSlideTime());
		});

		UseBlockCallback.EVENT.register((player, world, hand, hitResult)->
		{

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

								if(!player.isCreative()) stack.decrement(1);
							}
							return ActionResult.SUCCESS;
						}
					}
				}
			}

			return ActionResult.PASS;
		});

		UseEntityCallback.EVENT.register((pl, world, hand, entity, hr)->
		{
			ItemStack stack = pl.getStackInHand(hand);

			if(!stack.isEmpty() && (stack.getItem() == Items.GLASS_BOTTLE || stack.getItem() == Items.EXPERIENCE_BOTTLE))
			{
				if(entity instanceof LightningBugEntity)
				{
					if(!world.isClient())
					{
						ItemStack result = ItemUsage.method_30012(stack, pl, new ItemStack(stack.getItem() == Items.GLASS_BOTTLE ? BMBlocks.LIGHTNING_BUG_BOTTLE : BMItems.LIGHTNING_BOTTLE));
						pl.setStackInHand(hand, result);
						entity.remove();
						pl.playSound(SoundEvents.ITEM_BOTTLE_FILL, 1F, 1F);
					}
					return ActionResult.SUCCESS;
				}
			}
			return ActionResult.PASS;

		});

		final Identifier BAT_LT_ID = new Identifier("minecraft", "entities/bat");
		LootTableLoadingCallback.EVENT.register((rm, lm, id, supplier, setter)->{
			if(id.equals(BAT_LT_ID))
			{
				FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder().rolls(BinomialLootTableRange.create(2, 0.5F)).withEntry(ItemEntry.builder(BMItems.BAT_WING).build());
				supplier.withPool(builder.build());
			}
		});
	}

	public static Identifier ID(String path)
	{
		return new Identifier(MODID, path);
	}
}
