package party.lemons.biomemakeover.util;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTables;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMItems;

public class DebugUtil
{
	public static void printMissingLangKeys()
	{
		final String[] s = {""};

		Registry.BLOCK.forEach(b->
		{
			if(!I18n.hasTranslation(b.getTranslationKey()) && b.asItem() == Items.AIR)
			{
				s[0] += "\"" + b.getTranslationKey() + "\":\n";
			}
		});

		Registry.ITEM.forEach(b->
		{
			if(!I18n.hasTranslation(b.getTranslationKey()))
			{
				s[0] += "\"" + b.getTranslationKey() + "\":\n";
			}
		});

		Registry.ENTITY_TYPE.forEach(b->
		{
			if(!I18n.hasTranslation(b.getTranslationKey()))
			{
				s[0] += "\"" + b.getTranslationKey() + "\":\n";
			}
		});

		Registry.ENCHANTMENT.forEach((b)->{
			if(!I18n.hasTranslation(b.getTranslationKey()))
			{
				s[0] += "\"" + b.getTranslationKey() + "\":\n";
			}
		});

		System.out.println(s[0]);
	}

	public static void printUntaggedItems()
	{
		final String[] s = {""};

		Registry.ITEM.forEach((i)->{
			Identifier id = Registry.ITEM.getId(i);
			if(id.getNamespace().equals(BiomeMakeover.MODID))
			{
				if(!BMUtil.isInAny(i, BMItems.BADLANDS, BMItems.DARK_FOREST, BMItems.SWAMP, BMItems.MUSHROOM_FIELDS))
					s[0] += "\"" + id.toString() + "\",\n";
			}
		});

		System.out.println(s[0]);
	}

	public static void printMissingBlockLoot()
	{
		final String[] s = {""};
		Registry.BLOCK.forEach((i)-> {
			Identifier id = Registry.BLOCK.getId(i);
			if (id.getNamespace().equals(BiomeMakeover.MODID)) {
				if(i.getLootTableId() == LootTables.EMPTY)
					s[0] += id + "\n";
			}
		});

		System.out.println(s[0]);
	}
}
