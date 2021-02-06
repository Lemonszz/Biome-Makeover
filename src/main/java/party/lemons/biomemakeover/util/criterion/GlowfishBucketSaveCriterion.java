package party.lemons.biomemakeover.util.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluids;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.BiomeMakeover;

public class GlowfishBucketSaveCriterion extends AbstractCriterion<GlowfishBucketSaveCriterion.Conditions>
{
	private static final Identifier ID = BiomeMakeover.ID("glowfish_bucket_save");

	public Identifier getId()
	{
		return ID;
	}

	public GlowfishBucketSaveCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer)
	{
		return new GlowfishBucketSaveCriterion.Conditions(extended);
	}

	public void trigger(ServerPlayerEntity player)
	{
		this.test(player, (conditions)->
		{
			int fallDamage = computeFallDamage(player);
			if(fallDamage >= player.getHealth())
			{
				return player.world.getFluidState(player.getBlockPos()).getFluid() == Fluids.WATER || player.world.getFluidState(player.getBlockPos().down()).getFluid() == Fluids.WATER;
			}

			return false;
		});
	}

	protected int computeFallDamage(ServerPlayerEntity playerEntity)
	{
		StatusEffectInstance statusEffectInstance = playerEntity.getStatusEffect(StatusEffects.JUMP_BOOST);
		float f = statusEffectInstance == null ? 0.0F : (float) (statusEffectInstance.getAmplifier() + 1);
		return MathHelper.ceil((playerEntity.fallDistance - 3.0F - f) * 1);
	}

	public static class Conditions extends AbstractCriterionConditions
	{
		public Conditions(EntityPredicate.Extended player)
		{
			super(GlowfishBucketSaveCriterion.ID, player);
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer)
		{
			JsonObject jsonObject = super.toJson(predicateSerializer);
			return jsonObject;
		}
	}
}
