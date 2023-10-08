package party.lemons.biomemakeover.init;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.util.FieldConsumer;
import party.lemons.taniwha.data.criterion.SimpleCriterion;

public class BMAdvancements
{
    public static final SimpleCriterion ECTOPLASM_COMPOST = new SimpleCriterion(BiomeMakeover.ID("ectoplasm_compost"));
    public static final SimpleCriterion ARM_GOLEM = new SimpleCriterion(BiomeMakeover.ID("arm_golem"));
    public static final SimpleCriterion WITCH_TRADE = new SimpleCriterion(BiomeMakeover.ID("witch_trade"));
    public static final SimpleCriterion GLOWFISH_SAVE = new SimpleCriterion(BiomeMakeover.ID("glowfish_bucket_save"));
    public static final SimpleCriterion POLTERGEIST_YOURSELF = new SimpleCriterion(BiomeMakeover.ID("poltergeist_yourself"));
    public static final SimpleCriterion ANTIDOTE = new SimpleCriterion(BiomeMakeover.ID("antidote"));

    public static void init()
    {
        FieldConsumer.run(BMAdvancements.class, SimpleCriterionTrigger.class, (t)-> CriteriaTriggers.register((CriterionTrigger)t));
    }
}
