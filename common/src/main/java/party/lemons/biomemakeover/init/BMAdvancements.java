package party.lemons.biomemakeover.init;

import dev.architectury.registry.level.advancement.CriteriaTriggersRegistry;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.util.FieldConsumer;
import party.lemons.taniwha.data.criterion.SimpleCriterion;
import party.lemons.taniwha.data.criterion.WearArmorCriterion;

public class BMAdvancements
{
    public static final SimpleCriterion ECTOPLASM_COMPOST = new SimpleCriterion(BiomeMakeover.ID("ectoplasm_compost"));
    public static final WearArmorCriterion WEAR_ARMOUR = new WearArmorCriterion(Constants.MOD_ID);
    public static final SimpleCriterion ARM_GOLEM = new SimpleCriterion(BiomeMakeover.ID("arm_golem"));
    public static final SimpleCriterion WITCH_TRADE = new SimpleCriterion(BiomeMakeover.ID("witch_trade"));
    public static final SimpleCriterion GLOWFISH_SAVE = new SimpleCriterion(BiomeMakeover.ID("glowfish_bucket_save"));
    public static final SimpleCriterion ANTIDOTE = new SimpleCriterion(BiomeMakeover.ID("antidote"));

    public static void init()
    {
        FieldConsumer.run(BMAdvancements.class, SimpleCriterionTrigger.class, (t)->CriteriaTriggersRegistry.register((CriterionTrigger)t));
    }
}
