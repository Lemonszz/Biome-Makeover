package party.lemons.biomemakeover;

import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import party.lemons.biomemakeover.block.BigFlowerPotBlock;
import party.lemons.biomemakeover.crafting.witch.data.QuestCategoryReloadListener;
import party.lemons.biomemakeover.crafting.witch.data.reward.QuestRewardItem;
import party.lemons.biomemakeover.crafting.witch.data.reward.RewardTableReloadListener;
import party.lemons.biomemakeover.entity.CowboyEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorRoomListener;
import party.lemons.biomemakeover.entity.camel.CamelExtension;
import party.lemons.biomemakeover.entity.camel.EquipmentCamelEntity;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.level.BMWorldEvents;
import party.lemons.biomemakeover.mixin.PatrolSpawnerInvoker;
import party.lemons.biomemakeover.util.DebugUtil;
import party.lemons.taniwha.util.ItemUtil;

public class BiomeMakeover {

    public static void init()
    {
        BMConfig.load();

        BMEffects.init();

        BMAi.init();
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
        BMCrafting.init();

        AdjudicatorRoomListener.init();
        BMWorldEvents.init();

        LifecycleEvent.SERVER_LEVEL_LOAD.register(world -> BigFlowerPotBlock.bootstrap());
        ReloadListenerRegistry.register(PackType.SERVER_DATA, new QuestCategoryReloadListener());
        ReloadListenerRegistry.register(PackType.SERVER_DATA, new RewardTableReloadListener());

        //TODO: somewhere else :)
        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {   //Camel Upgrading
            if(entity.getType() == EntityType.CAMEL)
            {
                ItemStack item = player.getItemInHand(hand);
                if(((Camel)entity).isSaddled() && entity.getPassengers().size() <= 1)
                {
                    EntityType<?> type = null;
                    if(item.is(BMItems.CAMEL_CHESTS))
                    {
                        type = BMEntities.CHEST_CAMEL.get();
                    }
                    else if(item.getItem() instanceof BannerItem)
                    {
                        type = BMEntities.BANNER_CAMEL.get();
                    }

                    if(type == null)
                        return EventResult.pass();

                    if(entity.level().isClientSide())
                        return EventResult.interruptTrue();

                    Camel camel = (Camel) entity;
                    CompoundTag tag = camel.saveWithoutId(new CompoundTag());
                    tag.remove("UUID");
                    EquipmentCamelEntity newCamel = (EquipmentCamelEntity) type.create(entity.level());
                    newCamel.load(tag);
                    entity.level().addFreshEntity(newCamel);

                    newCamel.setEquipmentItem(item.copyWithCount(1));
                    newCamel.playSound(SoundEvents.DONKEY_CHEST, 1.0F, (newCamel.getRandom().nextFloat() - newCamel.getRandom().nextFloat()) * 0.2F + 1.0F);

                    if(camel.isVehicle())
                        for(Entity passenger : camel.getPassengers())
                        {
                            passenger.stopRiding();
                            passenger.startRiding(newCamel);
                        }

                    //TODO: this doesn't seem to do anything
                    ((CamelExtension)newCamel).setAnimationStates(camel.sitAnimationState, camel.sitPoseAnimationState, camel.sitUpAnimationState, camel.idleAnimationState, camel.dashAnimationState);
                    ItemUtil.shrinkStack(item, player);
                    entity.setRemoved(Entity.RemovalReason.DISCARDED);

                    return EventResult.interruptTrue();
                }


            }

            return EventResult.pass();
        });

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
        //TODO: move this from this class so it can be safely be called before full class loading
        return new ResourceLocation(Constants.MOD_ID, path);
    }
}
