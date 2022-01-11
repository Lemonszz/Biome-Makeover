package party.lemons.biomemakeover.util.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import party.lemons.biomemakeover.BiomeMakeover;

public class WearArmorCriterion extends SimpleCriterionTrigger<WearArmorCriterion.Conditions>
{
    private static final ResourceLocation ID = BiomeMakeover.ID("wear_armor");

    @Override
    protected WearArmorCriterion.Conditions createInstance(JsonObject jsonObject, EntityPredicate.Composite composite, DeserializationContext deserializationContext) {

        ItemPredicate itemPredicates = ItemPredicate.fromJson(jsonObject.get("item"));
        return new WearArmorCriterion.Conditions(ID, composite, itemPredicates);
    }

    public void trigger(ServerPlayer player)
    {
        this.trigger(player, (conditions)->
                conditions.matches(player.getArmorSlots()));
    }


    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public class Conditions extends AbstractCriterionTriggerInstance
    {
        private final ItemPredicate item;


        public Conditions(ResourceLocation resourceLocation, EntityPredicate.Composite composite, ItemPredicate itemPredicate) {
            super(resourceLocation, composite);

            this.item = itemPredicate;
        }

        public boolean matches(Iterable<ItemStack> armorItems)
        {
            for(ItemStack st : armorItems)
            {
                if(item.matches(st)) return true;
            }
            return false;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext) {
            JsonObject jsonObject = super.serializeToJson(serializationContext);
            jsonObject.add("item", item.serializeToJson());
            return jsonObject;
        }
    }
}
