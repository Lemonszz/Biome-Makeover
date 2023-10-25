package party.lemons.biomemakeover.util.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import party.lemons.biomemakeover.init.BMItems;

import java.util.function.Consumer;

public class BetterLootTableReference extends LootPoolSingletonContainer {
    final ResourceLocation name;

    BetterLootTableReference(ResourceLocation resourceLocation, int i, int j, LootItemCondition[] lootItemConditions, LootItemFunction[] lootItemFunctions) {
        super(i, j, lootItemConditions, lootItemFunctions);
        this.name = resourceLocation;
    }

    public LootPoolEntryType getType() {
        return BMItems.BETTER_LOOTTABLE_REFERENCE.get();
    }

    public void createItemStack(Consumer<ItemStack> consumer, LootContext lootContext) {
        LootTable lootTable = lootContext.getResolver().getLootTable(this.name);
        lootTable.getRandomItems(lootContext, consumer);
    }

    public void validate(ValidationContext validationContext) {
        LootDataId<LootTable> lootDataId = new LootDataId(LootDataType.TABLE, this.name);
        if (validationContext.hasVisitedElement(lootDataId)) {
            validationContext.reportProblem("Table " + this.name + " is recursively called");
        } else {
            super.validate(validationContext);
            validationContext.resolver().getElementOptional(lootDataId).ifPresentOrElse((lootTable) -> {
                lootTable.validate(validationContext.enterElement("->{" + this.name + "}", lootDataId));
            }, () -> {
                validationContext.reportProblem("Unknown loot table called " + this.name);
            });
        }
    }

    public static LootPoolSingletonContainer.Builder<?> lootTableReference(ResourceLocation resourceLocation) {
        return simpleBuilder((i, j, lootItemConditions, lootItemFunctions) -> {
            return new BetterLootTableReference(resourceLocation, i, j, lootItemConditions, lootItemFunctions);
        });
    }

    public static class Serializer extends LootPoolSingletonContainer.Serializer<BetterLootTableReference> {
        public Serializer() {
        }

        public void serializeCustom(JsonObject jsonObject, BetterLootTableReference lootTableReference, JsonSerializationContext jsonSerializationContext) {
            super.serializeCustom(jsonObject, lootTableReference, jsonSerializationContext);
            jsonObject.addProperty("name", lootTableReference.name.toString());
        }

        protected BetterLootTableReference deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, int i, int j, LootItemCondition[] lootItemConditions, LootItemFunction[] lootItemFunctions) {
            ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(jsonObject, "name"));
            return new BetterLootTableReference(resourceLocation, i, j, lootItemConditions, lootItemFunctions);
        }
    }
}
