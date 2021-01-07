package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;
import net.minecraft.advancement.criterion.CriterionConditions;
import party.lemons.biomemakeover.util.criterion.*;

public class BMCriterion
{
	public static final WearArmourCriterion WEAR_ARMOUR = new WearArmourCriterion();
	public static final GlowfishBucketSaveCriterion GLOWFISH_SAVE = new GlowfishBucketSaveCriterion();
	public static final EctoplasmCompostCriterion ECTOPLASM_COMPOST = new EctoplasmCompostCriterion();
	public static final WitchTradeCriterion WITCH_TRADE = new WitchTradeCriterion();
	public static final AntidoteCriterion ANTIDOTE = new AntidoteCriterion();

	public static void init()
	{
		CriterionRegistry.register(WEAR_ARMOUR);
		CriterionRegistry.register(GLOWFISH_SAVE);
		CriterionRegistry.register(ECTOPLASM_COMPOST);
		CriterionRegistry.register(WITCH_TRADE);
		CriterionRegistry.register(ANTIDOTE);
	}
}
