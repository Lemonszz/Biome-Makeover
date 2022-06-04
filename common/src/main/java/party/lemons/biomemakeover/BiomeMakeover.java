package party.lemons.biomemakeover;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.level.entity.EntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.utils.Env;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import party.lemons.biomemakeover.block.blockentity.render.AltarRenderer;
import party.lemons.biomemakeover.block.blockentity.render.LightningBugBottleRenderer;
import party.lemons.biomemakeover.block.blockentity.render.TapestryRenderer;
import party.lemons.biomemakeover.crafting.AltarScreen;
import party.lemons.biomemakeover.crafting.DirectionDataScreen;
import party.lemons.biomemakeover.crafting.witch.WitchQuestHandler;
import party.lemons.biomemakeover.crafting.witch.menu.WitchScreen;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorRoomListener;
import party.lemons.biomemakeover.entity.render.BMBoatRender;
import party.lemons.biomemakeover.entity.render.TumbleweedRender;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.level.BMWorldEvents;
import party.lemons.biomemakeover.level.TumbleweedSpawner;
import party.lemons.biomemakeover.level.WindSystem;
import party.lemons.biomemakeover.level.particle.LightningSparkParticle;
import party.lemons.biomemakeover.mixin.PatrolSpawnerInvoker;
import party.lemons.biomemakeover.mixin.PatrolSpawnerMixin_Cowboy;
import party.lemons.biomemakeover.util.color.ColorProviderHelper;
import party.lemons.biomemakeover.util.color.FoliageBlockColorProvider;
import party.lemons.biomemakeover.util.color.FoliageShiftBlockColorProvider;
import party.lemons.biomemakeover.util.color.StaticBlockColorProvider;
import party.lemons.biomemakeover.util.data.wiki.WikiGenerator;
import party.lemons.biomemakeover.util.loot.BMLootTableInjection;
import party.lemons.biomemakeover.util.registry.boat.BoatTypes;
import party.lemons.biomemakeover.util.task.TaskManager;

import java.lang.reflect.Constructor;

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
            WitchQuestHandler.init();
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
