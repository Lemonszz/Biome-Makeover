package party.lemons.biomemakeover.crafting.witch.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.crafting.witch.QuestCategory;
import party.lemons.biomemakeover.crafting.witch.QuestItem;

import java.util.Map;

public class QuestCategoryReloadListener extends SimpleJsonResourceReloadListener
{
	public static final String QUEST_CATEGORY = "quest_category";
	private static final Gson GSON = new Gson();

	public QuestCategoryReloadListener()
	{
		super(GSON, QUEST_CATEGORY);
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager resourceManager, ProfilerFiller profilerFiller)
	{
		QuestCategories.clearCategories();

		elements.forEach((location, json)->{
			JsonObject object = json.getAsJsonObject();

			if(!object.entrySet().isEmpty())
			{
				int weight = object.get("weight").getAsInt();

				QuestCategory category = new QuestCategory(weight);

				JsonArray requests = object.getAsJsonArray("requests");
				requests.forEach(r->{
					JsonObject requestObject = r.getAsJsonObject();

					String itemName = requestObject.get("item").getAsString();
					float points = requestObject.get("points").getAsFloat();
					int maxCount = requestObject.get("max_count").getAsInt();

					ResourceLocation itemLocation = new ResourceLocation(itemName);
					Item item = Registry.ITEM.get(itemLocation);
					if(item == Items.AIR)
						Constants.LOG.warn("Air or Unknown item found in witch category: " + location + " | " + itemLocation);

					category.addItem(QuestItem.of(item, points, maxCount));
				});

				if(!category.isEmpty())
					QuestCategories.addCategory(category);
			}
		});
	}
}
