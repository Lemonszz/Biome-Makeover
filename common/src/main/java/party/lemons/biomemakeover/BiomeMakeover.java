package party.lemons.biomemakeover;

import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import party.lemons.biomemakeover.crafting.witch.data.QuestCategoryReloadListener;
import party.lemons.biomemakeover.crafting.witch.data.reward.QuestRewardItem;
import party.lemons.biomemakeover.crafting.witch.data.reward.RewardTableReloadListener;
import party.lemons.biomemakeover.entity.CowboyEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorRoomListener;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.level.BMWorldEvents;
import party.lemons.biomemakeover.mixin.PatrolSpawnerInvoker;
import party.lemons.biomemakeover.util.DebugUtil;
import party.lemons.taniwha.item.ItemHelper;
import party.lemons.taniwha.item.types.FakeItem;

public class BiomeMakeover {

    public static void init()
    {
        BMConfig.load();

        BMEffects.init();
        BMEntities.init();

        BMTab.init();
        BMBlocks.init();
        BMBlockEntities.init();
        BMItems.init();
        BMNetwork.init();
        BMPotions.init();
        BMFeatures.init();
        BMStructures.init();
        BMScreens.init();
        BMAdvancements.init();
        BMEnchantments.init();
        BMBoats.init();
        QuestRewardItem.init();

        AdjudicatorRoomListener.init();
        BMWorldEvents.init();

        ReloadListenerRegistry.register(PackType.SERVER_DATA, new QuestCategoryReloadListener());
        ReloadListenerRegistry.register(PackType.SERVER_DATA, new RewardTableReloadListener());

        //TODO: Find somewhere else for this
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> dispatcher.register(Commands.literal("pillager").requires((serverCommandSource)->serverCommandSource.hasPermission(2)).then(Commands.argument("pos", BlockPosArgument.blockPos()).then(Commands.argument("leader", BoolArgumentType.bool()).executes(c->
        {
            ((PatrolSpawnerInvoker)new PatrolSpawner()).callSpawnPatrolMember(c.getSource().getLevel(), BlockPosArgument.getLoadedBlockPos(c, "pos"), c.getSource().getLevel().random, BoolArgumentType.getBool(c, "leader"));
            return 1;
        })))));

        LifecycleEvent.SETUP.register(()-> {
            BMBlocks.BLOCK_ITEMS.forEach((block, item) -> {
                Item.BY_BLOCK.put(block.get(), item.get());
            });

            for (RegistrySupplier<Item> item : ItemHelper.getItems(Constants.MOD_ID)) {
                if (item.get() instanceof FakeItem || (item.get() instanceof BlockItem bi && bi.getBlock() == BMBlocks.DIRECTIONAL_DATA.get()) || BMItems.HIDDEN_ITEMS.contains(item))
                    continue;

                CreativeTabRegistry.append(BMTab.TAB, item.get());
            }
            CreativeTabRegistry.appendStack(BMTab.TAB, CowboyEntity.getOminousBanner());

            BMEntities.initSpawnsAndAttributes();

            //TODO: Find somewhere else for this
        });

        final ResourceLocation evokerTable = new ResourceLocation("minecraft", "entities/evoker");
        final ResourceLocation pillagerOutpostTable = new ResourceLocation("minecraft", "chests/pillager_outpost");
        LootEvent.MODIFY_LOOT_TABLE.register((lootTables, id, context, builtin) -> {
            if (builtin)
            {
                if(id.equals(evokerTable)) {
                    LootPool.Builder pool = LootPool.lootPool().add(
                            LootItem.lootTableItem(BMItems.ILLUNITE_SHARD.get())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)))
                    ).when(LootItemKilledByPlayerCondition.killedByPlayer()).when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.25F, 0.05F));
                    context.addPool(pool);
                }else if(id.equals(pillagerOutpostTable)){
                    LootPool.Builder pool = LootPool.lootPool().add(
                            LootTableReference.lootTableReference(BiomeMakeover.ID("pillager_outpost_additional"))
                    );
                    context.addPool(pool);

                }
            }
        });

        DebugUtil.init();

    }
    public static ResourceLocation ID(String path)
    {
        return new ResourceLocation(Constants.MOD_ID, path);
    }
}
