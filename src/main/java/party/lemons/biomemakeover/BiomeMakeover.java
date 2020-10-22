package party.lemons.biomemakeover;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.gen.PillagerSpawner;
import party.lemons.biomemakeover.crafting.itemgroup.BiomeMakeoverItemGroup;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.util.access.BiomeEffectsAccessor;
import party.lemons.biomemakeover.util.access.PillagerSpawnerAccess;
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
		BMEntities.init();
		BMBlocks.init();
		BMItems.init();
		BMStructures.init();
		BMWorldGen.init();

		ServerTickEvents.END_SERVER_TICK.register((e)->WindSystem.update());

		//TOOD: Move
		BiomeEffectsAccessor.setWaterColor(BuiltinRegistries.BIOME.get(BiomeKeys.MUSHROOM_FIELDS), 0xad3fe4);
		BiomeEffectsAccessor.setWaterColor(BuiltinRegistries.BIOME.get(BiomeKeys.MUSHROOM_FIELD_SHORE), 0x5d3fe4);

		//TOOD: Move
		CommandRegistrationCallback.EVENT.register((d, ded)->{
			d.register(CommandManager.literal("pillager").requires((serverCommandSource) ->serverCommandSource.hasPermissionLevel(2))
					.then(CommandManager.argument("pos", BlockPosArgumentType.blockPos()).then(CommandManager.argument("leader", BoolArgumentType.bool()).executes(c->{
						((PillagerSpawnerAccess)new PillagerSpawner()).spawn(c.getSource().getWorld(), BlockPosArgumentType.getBlockPos(c, "pos"), BoolArgumentType.getBool(c, "leader"));
						return 1;
					}))));
		});
	}

	public static Identifier ID(String path)
	{
		return new Identifier(MODID, path);
	}
}
