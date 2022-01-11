package party.lemons.biomemakeover.util.data.wiki;

import org.jetbrains.annotations.Nullable;

public interface ItemPage extends WikiPage
{
    String name();
    String id();
    String description();
    @Nullable FoodWiki food();
}
