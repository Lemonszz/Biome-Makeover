package party.lemons.biomemakeover;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import dev.architectury.platform.Platform;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class BMConfig
{
	public String info = "You have found the config file for Biome Makeover!. This mod is made to be customised via datapack, so options in here may be limited. We have added additional functionality to datapacks, so you may find what you're looking for in our docs.";

	public boolean strictAltarCursing = false;
	public EnchantmentsConfig enchantmentConfig = new EnchantmentsConfig();



	public static class EnchantmentsConfig
	{
		public final EnchantConfig DECAY = new EnchantConfig(5, 25, 50, true, false, false);
		public final EnchantConfig INSOMNIA = new EnchantConfig(5, 25, 50, true, false, false);
		public final EnchantConfig CONDUCTIVITY = new EnchantConfig(5, 25, 50, true, false, false);
		public final EnchantConfig ENFEEBLEMENT = new EnchantConfig(5, 25, 50, true, false, false);
		public final EnchantConfig DEPTHS = new EnchantConfig(3, 25, 50, true, false, false);
		public final EnchantConfig FLAMMABILITY = new EnchantConfig(3, 25, 50, true, false, false);
		public final EnchantConfig SUFFOCATION = new EnchantConfig(3, 25, 50, true, false, false);
		public final EnchantConfig UNWIELDINESS = new EnchantConfig(3, 25, 50, true, false, false);
		public final EnchantConfig INACCURACY = new EnchantConfig(3, 25, 50, true, false, false);
		public final EnchantConfig BUCKLING = new EnchantConfig(3, 25, 50, true, false, false);
	}

	public static class EnchantConfig
	{
		public final int maxLevel, minCost, maxCost;
		public final boolean isTreasureOnly, isDiscoverable, isTradeable;

		public EnchantConfig(int maxLevel, int minCost, int maxCost, boolean isTreasureOnly, boolean isDiscoverable, boolean isTradeable)
		{
			this.maxLevel = maxLevel;
			this.minCost = minCost;
			this.maxCost = maxCost;
			this.isTreasureOnly = isTreasureOnly;
			this.isDiscoverable = isDiscoverable;
			this.isTradeable = isTradeable;
		}
	}


	public static BMConfig INSTANCE;
	public static void load()
	{
		Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
		File cfgFile = getConfigFile().toFile();
		INSTANCE = new BMConfig();

		if(cfgFile.exists())
		{
			try(FileReader fileReader = new FileReader(cfgFile))
			{
				try(JsonReader reader = new JsonReader(fileReader))
				{
					INSTANCE = gson.fromJson(reader, BMConfig.class);
					writeConfig(gson, cfgFile, INSTANCE);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			writeConfig(gson, cfgFile, INSTANCE);
		}
	}

	public static void writeConfig(Gson gson, File cfgFile, BMConfig config)
	{
		try(FileWriter writer = new FileWriter(cfgFile)){
			gson.toJson(config, writer);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static Path getConfigFile()
	{
		return Platform.getConfigFolder().resolve(Constants.MOD_ID + ".json");
	}
}
