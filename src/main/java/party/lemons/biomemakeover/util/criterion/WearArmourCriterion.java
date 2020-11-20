package party.lemons.biomemakeover.util.criterion;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import party.lemons.biomemakeover.BiomeMakeover;

import java.util.List;

public class WearArmourCriterion extends AbstractCriterion<WearArmourCriterion.Conditions>
{
	private static final Identifier ID = BiomeMakeover.ID("wear_armor");

	public Identifier getId() {
		return ID;
	}

	public WearArmourCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer)
	{
		ItemPredicate itemPredicates = ItemPredicate.fromJson(jsonObject.get("item"));
		return new WearArmourCriterion.Conditions(extended, itemPredicates);
	}

	public void trigger(ServerPlayerEntity player)
	{
		this.test(player, (conditions)->
		{
			return conditions.matches(player.getArmorItems());
		});
	}

	public static class Conditions extends AbstractCriterionConditions
	{
		private final ItemPredicate item;

		public Conditions(EntityPredicate.Extended player, ItemPredicate item) {
			super(WearArmourCriterion.ID, player);
			this.item = item;
		}

		public boolean matches(Iterable<ItemStack> armorItems)
		{
			for(ItemStack st : armorItems)
			{
				if(item.test(st))
					return true;
			}
			return false;
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("item", item.toJson());
			return jsonObject;
		}
	}
}
