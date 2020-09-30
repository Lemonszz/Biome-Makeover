package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.tag.Tag;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.item.BMItem;
import party.lemons.biomemakeover.item.BMMusicDiskItem;
import party.lemons.biomemakeover.util.RegistryHelper;

public class BMItems
{
	public static final FoodComponent GLOWSHROOM_SOUP_FOOD = new FoodComponent.Builder().alwaysEdible().saturationModifier(0.6F).hunger(5).statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1200, 0), 1).statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 1200, 0), 1).build();

	public static final BMMusicDiskItem BUTTON_MUSHROOMS_MUSIC_DISK = new BMMusicDiskItem(14, BMEffects.BUTTON_MUSHROOMS, settings().rarity(Rarity.RARE));
	public static final MushroomStewItem GLOWSHROOM_STEW = new MushroomStewItem(settings().maxCount(1).recipeRemainder(Items.BOWL).food(GLOWSHROOM_SOUP_FOOD));

	public static void init()
	{
		RegistryHelper.register(Registry.ITEM, Item.class, BMItems.class);
	}

	public static Item.Settings settings()
	{
		return new Item.Settings().group(BiomeMakeover.GROUP);
	}

	public static final Tag<Item> MUSHROOM_FIELDS = TagRegistry.item(BiomeMakeover.ID("mushroom_fields"));
}
