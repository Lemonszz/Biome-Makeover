package party.lemons.biomemakeover.util.data.wiki;

public interface BlockPage extends WikiPage
{
    String name();
    String id();
    String description();
    BlockSettingsWiki settings();
    BlockTagsWiki tags();
    BlockModifiersWiki modifiers();
}
