package party.lemons.biomemakeover;

import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import party.lemons.biomemakeover.block.DirectionalDataBlock;
import party.lemons.biomemakeover.crafting.witch.data.QuestCategoryReloadListener;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorRoomListener;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.level.BMWorldEvents;
import party.lemons.biomemakeover.mixin.PatrolSpawnerInvoker;
import party.lemons.biomemakeover.util.loot.BMLootTableInjection;

public class BiomeMakeover {

    public static final CreativeTabRegistry.TabSupplier TAB = CreativeTabRegistry.create(ID(Constants.MOD_ID), ()->new ItemStack(BMItems.ICON_ITEM.get()));

    public static void init()
    {
        BMConfig.load();

        BMEffects.init();
        BMEntities.init();

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

        AdjudicatorRoomListener.init();
        BMWorldEvents.init();

        ReloadListenerRegistry.register(PackType.SERVER_DATA, new QuestCategoryReloadListener());

        //TODO: Find somewhere else for this
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> dispatcher.register(Commands.literal("pillager").requires((serverCommandSource)->serverCommandSource.hasPermission(2)).then(Commands.argument("pos", BlockPosArgument.blockPos()).then(Commands.argument("leader", BoolArgumentType.bool()).executes(c->
        {
            ((PatrolSpawnerInvoker)new PatrolSpawner()).callSpawnPatrolMember(c.getSource().getLevel(), BlockPosArgument.getLoadedBlockPos(c, "pos"), c.getSource().getLevel().random, BoolArgumentType.getBool(c, "leader"));
            return 1;
        })))));

        LifecycleEvent.SETUP.register(()->{
            BMBlocks.BLOCK_ITEMS.forEach((block, item) -> {
                Item.BY_BLOCK.put(block.get(), item.get());
            });

            BuiltInRegistries.ITEM.forEach(it->{
                if(it != BMItems.ICON_ITEM.get() && BuiltInRegistries.ITEM.getKey(it).getNamespace().equals(Constants.MOD_ID))
                    CreativeTabRegistry.append(BiomeMakeover.TAB, it);
            });

            BuiltInRegistries.BLOCK.forEach(it->{
                if(it.asItem() != Items.AIR && !(it instanceof DirectionalDataBlock) &&BuiltInRegistries.BLOCK.getKey(it).getNamespace().equals(Constants.MOD_ID))
                    CreativeTabRegistry.append(BiomeMakeover.TAB, it);
            });

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
