package party.lemons.biomemakeover;

import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import party.lemons.biomemakeover.crafting.witch.WitchQuestHandler;
import party.lemons.biomemakeover.crafting.witch.data.QuestCategoryReloadListener;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorRoomListener;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.level.BMWorldEvents;
import party.lemons.biomemakeover.mixin.PatrolSpawnerInvoker;
import party.lemons.biomemakeover.util.data.wiki.WikiGenerator;
import party.lemons.biomemakeover.util.loot.BMLootTableInjection;
import party.lemons.biomemakeover.util.task.TaskManager;

public class BiomeMakeover {

    public static final CreativeModeTab TAB = CreativeTabRegistry.create(ID(Constants.MOD_ID), ()->new ItemStack(BMItems.ICON_ITEM.get()));
    private static final boolean ENABLE_WIKI = false;

    public static void init()
    {
        BMEffects.init();
        BMEntities.init();

        BMBlocks.init();
        BMBlockEntities.init();
        BMItems.init();
        BMNetwork.init();
        BMPotions.init();
        BMFeatures.init();
        BMScreens.init();
        BMAdvancements.init();
        BMEnchantments.init();

        AdjudicatorRoomListener.init();
        BMWorldEvents.init();
        TaskManager.init();

        ReloadListenerRegistry.register(PackType.SERVER_DATA, new QuestCategoryReloadListener());

        if(Platform.isDevelopmentEnvironment() && ENABLE_WIKI)
            PlayerEvent.DROP_ITEM.register((Player player, ItemEntity entity)->{
                WikiGenerator.generate();

                return EventResult.pass();
            });

        //TODO: Find somewhere else for this
        CommandRegistrationEvent.EVENT.register((dispatcher, selection) -> dispatcher.register(Commands.literal("pillager").requires((serverCommandSource)->serverCommandSource.hasPermission(2)).then(Commands.argument("pos", BlockPosArgument.blockPos()).then(Commands.argument("leader", BoolArgumentType.bool()).executes(c->
        {
            ((PatrolSpawnerInvoker)new PatrolSpawner()).callSpawnPatrolMember(c.getSource().getLevel(), BlockPosArgument.getLoadedBlockPos(c, "pos"), c.getSource().getLevel().random, BoolArgumentType.getBool(c, "leader"));
            return 1;
        })))));

        LifecycleEvent.SETUP.register(()->{
            BMWorldGen.init();
            BMEntities.initSpawnsAndAttributes();

            //TODO: Find somewhere else for this
            BMLootTableInjection.inject(new ResourceLocation("minecraft", "entities/evoker"), BinomialDistributionGenerator.binomial(3, 0.15F), BMItems.ILLUNITE_SHARD.get());
        });
    }
    public static ResourceLocation ID(String path)
    {
        return new ResourceLocation(Constants.MOD_ID, path);
    }
}
