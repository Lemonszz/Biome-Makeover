package party.lemons.biomemakeover.util.loot;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;

import java.util.List;

public class BMLootTableInjection
{
    static List<InjectedItem> insertedEntries = Lists.newArrayList();

    public static void inject(ResourceLocation table, BinomialDistributionGenerator rolls, ItemLike item, boolean playerOnly)
    {
        insertedEntries.add(new InjectedItem(table, rolls, item, playerOnly));
    }

    public static void inject(ResourceLocation table, BinomialDistributionGenerator rolls, ItemLike item)
    {
        inject(table, rolls, item, false);
    }

    public static List<InjectedItem> getInsertedEntries()
    {
        return insertedEntries;
    }

    public record InjectedItem(ResourceLocation table, BinomialDistributionGenerator rolls, ItemLike itemLike, boolean playerOnly)
    {

    }
}
