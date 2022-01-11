package party.lemons.biomemakeover.util.data.wiki;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class WikiGenerator
{
    private static final List<ItemPage> ITEMS = Lists.newArrayList();
    private static String ITEM_TEMPLATE;

    public static void generate()
    {
        for(Item item : Registry.ITEM)
        {
            if(isValidItem(item))
            {
                generateItem(item);
            }
        }

        doOutput();
    }

    private static void doOutput()
    {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try
        {
            Files.createDirectories(Paths.get("wiki/template/"));

            StringBuilder builder = new StringBuilder();
            Files.readAllLines(Paths.get("wiki/template/item_template.md")).forEach(s-> builder.append(s).append("\n"));
            ITEM_TEMPLATE = builder.toString();

            Files.createDirectories(Paths.get("wiki/items/pages/"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ITEMS.forEach(i ->{
            try
            {
                FileWriter writer = new FileWriter("wiki/items/" + idToFileName(i.id()) + ".json");
                gson.toJson(i, writer);
                writer.flush();
                writer.close();

                FileWriter fileWriter = new FileWriter("wiki/items/pages/" + idToFileName(i.id()) + ".md");
                PrintWriter printWriter = new PrintWriter(fileWriter);
                String pageString = ITEM_TEMPLATE.replace("[ITEM_NAME]", i.name()).replace("[ITEM_FILE]", idToFileName(i.id()));
                printWriter.println(pageString);
                fileWriter.flush();
                printWriter.close();
                fileWriter.close();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

    }

    private static String getItemDescription()
    {
        return "This item does not have a description yet!";
    }


    private static void generateItem(Item item)
    {
        if(item instanceof RecordItem record)
        {
            ITEMS.add(new RecordItemPageImpl(record));
        }
        else
        {
            ITEMS.add(new ItemPageImpl(item));
        }
    }

    private static boolean isValidItem(Item item)
    {
        return Registry.ITEM.getKey(item).getNamespace().equals(Constants.MOD_ID) &&
                item.getClass() != BlockItem.class &&
                !(item instanceof StandingAndWallBlockItem);
    }

    private static String idToFileName(String id)
    {
        return id.split(":")[1];
    }

    public static class ItemPageImpl implements ItemPage
    {
        protected String name;
        protected String id;
        protected String description;
        protected @Nullable FoodWiki food;
        protected @Nullable WikiGenerator.ArmorWikiImpl armor;

        protected List<String> tags = Lists.newArrayList();

        public ItemPageImpl(Item item)
        {
            this.name = I18n.get(item.getDescriptionId());
            this.id = Registry.ITEM.getKey(item).toString();
            this.description = WikiGenerator.getItemDescription();

            if(item.getFoodProperties() != null) {
                food = new FoodWikiImpl(item.getFoodProperties());
                tags.add("food");
            }

            if(item instanceof ArmorItem armor)
            {
                this.armor = new ArmorWikiImpl(armor);
                tags.add("armor");
                tags.add(armor.getMaterial().getName());
            }
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public String description() {
            return description;
        }

        @Override
        public @Nullable FoodWiki food() {
            return food;
        }

        @Override
        public List<String> getTags() {
            return tags;
        }
    }

    private WikiGenerator()
    {

    }

    private static class FoodWikiImpl implements FoodWiki
    {
        protected float saturation;
        protected float nutrition;
        protected EffectWiki[] effects;
        protected boolean alwaysEat;


        public FoodWikiImpl(FoodProperties food)
        {
            this.alwaysEat = food.canAlwaysEat();
            saturation = food.getSaturationModifier();
            nutrition = food.getNutrition();

            if(!food.getEffects().isEmpty())
            {
                effects = new EffectWiki[food.getEffects().size()];
                for(int i = 0; i < food.getEffects().size(); i++)
                {
                    Pair<MobEffectInstance, Float> effect = food.getEffects().get(i);
                    effects[i] = new EffectWikiImpl(effect.getFirst(), effect.getSecond());
                }
            }
        }

        @Override
        public float saturation() {
            return saturation;
        }

        @Override
        public float nutrition() {
            return nutrition;
        }

        @Override
        public boolean alwaysEat() {
            return alwaysEat;
        }
    }

    private static class EffectWikiImpl implements EffectWiki
    {
        protected String name;
        protected float length;
        protected int level;
        protected float chance;

        public EffectWikiImpl(MobEffectInstance first, Float chance)
        {
            this.name = I18n.get(first.getDescriptionId());
            this.length = (float)first.getDuration();
            this.level = first.getAmplifier();
            this.chance = chance;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public float length() {
            return length;
        }

        @Override
        public int level() {
            return level;
        }

        @Override
        public float chance() {
            return chance;
        }
    }

    private static class ArmorWikiImpl
    {
        protected String armorName;
        protected float defence;
        protected float toughness;
        protected float enchantment;
        protected float knockbackResistance;
        protected String slot;
        protected String[] repairItems;

        public ArmorWikiImpl(ArmorItem armor)
        {
            ArmorMaterial material = armor.getMaterial();
            this.armorName = WordUtils.capitalize(material.getName());
            this.defence = armor.getDefense();
            this.toughness = armor.getEnchantmentValue();
            this.knockbackResistance = material.getKnockbackResistance();
            this.slot = WordUtils.capitalize(armor.getSlot().getName());

            int repairLength = material.getRepairIngredient().getItems().length;
            if(repairLength != 0)
            {
                repairItems = new String[repairLength];
                for(int i = 0; i < repairLength; i++)
                {
                    repairItems[i] = I18n.get(material.getRepairIngredient().getItems()[i].getDescriptionId());
                }
            }
        }
    }

    private static class RecordItemPageImpl extends ItemPageImpl
    {
        public RecordItemPageImpl(RecordItem record)
        {
            super(record);

            name = name() + " (" + I18n.get(record.getDescriptionId() + ".desc") + ")";
        }
    }
}
