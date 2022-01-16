package party.lemons.biomemakeover.util.loot;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import com.google.common.collect.Lists;
import java.util.List;

public class BMLootTableInjection
{
    static List<InjectedItem> insertedEntries = Lists.newArrayList();

    public static void inject(ResourceLocation table, BinomialDistributionGenerator rolls, ItemLike item)
    {
        insertedEntries.add(new InjectedItem(table, rolls, item));
    }

    public static List<InjectedItem> getInsertedEntries()
    {
        return insertedEntries;
    }

    public record InjectedItem(ResourceLocation table, BinomialDistributionGenerator rolls, ItemLike itemLike)
    {

    }
}
