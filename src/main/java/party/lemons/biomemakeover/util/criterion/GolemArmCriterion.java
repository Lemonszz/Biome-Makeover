package party.lemons.biomemakeover.util.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;

public class GolemArmCriterion extends AbstractCriterion<GolemArmCriterion.Conditions>
{
	private static final Identifier ID = BiomeMakeover.ID("arm_golem");

	public Identifier getId()
	{
		return ID;
	}

	public GolemArmCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer)
	{
		return new GolemArmCriterion.Conditions(extended);
	}

	public void trigger(ServerPlayerEntity player)
	{
		this.test(player, (conditions)->true);
	}

	public static class Conditions extends AbstractCriterionConditions
	{
		public Conditions(EntityPredicate.Extended player)
		{
			super(GolemArmCriterion.ID, player);
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer)
		{
			JsonObject jsonObject = super.toJson(predicateSerializer);
			return jsonObject;
		}
	}
}
