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
	public String info = "You've found the config file for Biome Makeover!. This mod is made to be customised via datapack, so options in here may be limited. We have added additional functionality to datapacks, so you may find what you're looking for in our docs.";

	public boolean strictAltarCursing = false;

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
