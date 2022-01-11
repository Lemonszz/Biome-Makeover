package party.lemons.biomemakeover.init;

import dev.architectury.registry.level.advancement.CriteriaTriggersRegistry;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import party.lemons.biomemakeover.util.FieldConsumer;
import party.lemons.biomemakeover.util.criterion.*;

public class BMAdvancements
{
    public static final SimpleCriterion ECTOPLASM_COMPOST = new SimpleCriterion("ectoplasm_compost");
    public static final WearArmorCriterion WEAR_ARMOUR = new WearArmorCriterion();
    public static final SimpleCriterion ARM_GOLEM = new SimpleCriterion("arm_golem");
    public static final SimpleCriterion WITCH_TRADE = new SimpleCriterion("witch_trade");
    public static final SimpleCriterion GLOWFISH_SAVE = new SimpleCriterion("glowfish_bucket_save");
    public static final SimpleCriterion ANTIDOTE = new SimpleCriterion("antidote");

    public static void init()
    {
        FieldConsumer.run(BMAdvancements.class, SimpleCriterionTrigger.class, (t)->CriteriaTriggersRegistry.register((CriterionTrigger)t));
    }
}
