package party.lemons.biomemakeover.util;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.apache.commons.lang3.text.WordUtils;
import party.lemons.biomemakeover.Constants;

public class DebugUtil
{
	public static void printMissingLangKeys()
	{
		final String[] s = {""};

		BuiltInRegistries.BLOCK.stream().filter(b -> BuiltInRegistries.BLOCK.getKey(b).getNamespace().equals(Constants.MOD_ID)).forEach(b ->
		{
			if (!I18n.exists(b.getDescriptionId()) && b.asItem() == Items.AIR) {
				s[0] += "\"" + b.getDescriptionId() + "\":\n";
			}
		});

		BuiltInRegistries.ITEM.stream().filter(b -> BuiltInRegistries.ITEM.getKey(b).getNamespace().equals(Constants.MOD_ID)).forEach(b ->
		{
			if (!I18n.exists(b.getDescriptionId())) {
				s[0] += "\"" + b.getDescriptionId() + "\":\n";
			}
		});

		BuiltInRegistries.ENTITY_TYPE.stream().filter(b -> BuiltInRegistries.ENTITY_TYPE.getKey(b).getNamespace().equals(Constants.MOD_ID)).forEach(b ->
		{
			if (!I18n.exists(b.getDescriptionId())) {
				s[0] += "\"" + b.getDescriptionId() + "\":\n";
			}
		});

		BuiltInRegistries.ENCHANTMENT.stream().filter(b -> BuiltInRegistries.ENCHANTMENT.getKey(b).getNamespace().equals(Constants.MOD_ID)).forEach((b) -> {
			if (!I18n.exists(b.getDescriptionId())) {
				s[0] += "\"" + b.getDescriptionId() + "\":\n";
			}
		});

		System.out.println(s[0]);
	}

	public static void printMissingLangKeysWithNames()
	{
		final String[] s = {""};

		BuiltInRegistries.BLOCK.stream().filter(b->BuiltInRegistries.BLOCK.getKey(b).getNamespace().equals(Constants.MOD_ID)).forEach(b->
		{
			if(!I18n.exists(b.getDescriptionId()) && b.asItem() == Items.AIR)
			{
				s[0] += "\"" + b.getDescriptionId() + "\": \"" + locationToEnglish(BuiltInRegistries.BLOCK.getKey(b)) + "\",\n";
			}
		});

		BuiltInRegistries.ITEM.stream().filter(b->BuiltInRegistries.ITEM.getKey(b).getNamespace().equals(Constants.MOD_ID)).forEach(b->
		{
			if(!I18n.exists(b.getDescriptionId()))
			{
				s[0] += "\"" + b.getDescriptionId() + "\": \"" + locationToEnglish(BuiltInRegistries.ITEM.getKey(b)) + "\"," +
						"\n";
			}
		});

		BuiltInRegistries.ENTITY_TYPE.stream().filter(b->BuiltInRegistries.ENTITY_TYPE.getKey(b).getNamespace().equals(Constants.MOD_ID)).forEach(b->
		{
			if(!I18n.exists(b.getDescriptionId()))
			{
				s[0] += "\"" + b.getDescriptionId() + "\": \"" + locationToEnglish(BuiltInRegistries.ENTITY_TYPE.getKey(b)) + "\",\n";
			}
		});

		BuiltInRegistries.ENCHANTMENT.stream().filter(b->BuiltInRegistries.ENCHANTMENT.getKey(b).getNamespace().equals(Constants.MOD_ID)).forEach((b)->{
			if(!I18n.exists(b.getDescriptionId()))
			{
				s[0] += "\"" + b.getDescriptionId() + "\": \"" + locationToEnglish(BuiltInRegistries.ENCHANTMENT.getKey(b)) + "\",\n";
			}
		});

		System.out.println(s[0]);
	}

	public static void printBlocksWithoutTools()
	{
		final String[] s = {""};

		BuiltInRegistries.BLOCK.stream().filter(b->BuiltInRegistries.BLOCK.getKey(b).getNamespace().equals(Constants.MOD_ID)).forEach(b->{
			if(!b.defaultBlockState().is(BlockTags.MINEABLE_WITH_AXE) && !b.defaultBlockState().is(BlockTags.MINEABLE_WITH_HOE) && !b.defaultBlockState().is(BlockTags.MINEABLE_WITH_PICKAXE) && !b.defaultBlockState().is(BlockTags.MINEABLE_WITH_SHOVEL))
				s[0] += "\"" + BuiltInRegistries.BLOCK.getKey(b) + "\"\n";
		});
		System.out.println(s[0]);

	}

	public static String locationToEnglish(ResourceLocation location)
	{
		String name = location.getPath();
		name = name.replace("_", " ");
		name = WordUtils.capitalizeFully(name);

		return name;
	}

	public static void printBlockColors()
	{
		int grass = Minecraft.getInstance().getBlockColors().getColor(Blocks.GRASS_BLOCK.defaultBlockState(), Minecraft.getInstance().level, Minecraft.getInstance().player.getOnPos(), 1);
		int foliage = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.defaultBlockState(), Minecraft.getInstance().level, Minecraft.getInstance().player.getOnPos(), 1);

		System.out.println("Grass: " + grass  +", " + "Foliage: " + foliage);
	}

	public static void printBlocksWithoutLoot()
	{
		final String[] s = {"---BLOCKS WITHOUT LOOT---\n"};
		BuiltInRegistries.BLOCK.stream().filter(b->BuiltInRegistries.BLOCK.getKey(b).getNamespace().equals(Constants.MOD_ID)).forEach(b->{
			if(b.getLootTable() != BuiltInLootTables.EMPTY && Minecraft.getInstance().getSingleplayerServer().getLootData().getLootTable(b.getLootTable()) == LootTable.EMPTY)
				s[0] += b.getDescriptionId() + "\n";
		});

		System.out.println(s[0]);
	}

	public static void init()
	{
		if(false && Platform.isDevelopmentEnvironment() && Platform.getEnvironment() == Env.CLIENT) {
			InteractionEvent.RIGHT_CLICK_BLOCK.register((e, v, p, f)->{
				if(v == InteractionHand.OFF_HAND)
					return EventResult.pass();

				System.out.println(" ");
				System.out.println("---- loot----");
				 DebugUtil.printBlocksWithoutLoot();

				System.out.println(" ");
				System.out.println("---- lang ----");
				DebugUtil.printMissingLangKeysWithNames();
				// DebugUtil.printBlockColors();

				System.out.println(" ");
				System.out.println("---- Block no tool----");
				printBlocksWithoutTools();
				return EventResult.pass();
			});
		}
	}
}