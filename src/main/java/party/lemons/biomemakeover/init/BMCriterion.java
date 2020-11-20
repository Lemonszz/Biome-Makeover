package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;
import party.lemons.biomemakeover.util.criterion.EctoplasmCompostCriterion;
import party.lemons.biomemakeover.util.criterion.GlowfishBucketSaveCriterion;
import party.lemons.biomemakeover.util.criterion.WearArmourCriterion;

public class BMCriterion
{
	public static final WearArmourCriterion WEAR_ARMOUR = new WearArmourCriterion();
	public static final GlowfishBucketSaveCriterion GLOWFISH_SAVE = new GlowfishBucketSaveCriterion();
	public static final EctoplasmCompostCriterion ECTOPLASM_COMPOST = new EctoplasmCompostCriterion();

	public static void init()
	{
		CriterionRegistry.register(WEAR_ARMOUR);
		CriterionRegistry.register(GLOWFISH_SAVE);
		CriterionRegistry.register(ECTOPLASM_COMPOST);
	}
}
