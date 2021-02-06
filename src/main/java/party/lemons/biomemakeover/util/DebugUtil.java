package party.lemons.biomemakeover.util;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

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

		System.out.println(s[0]);
	}
}
